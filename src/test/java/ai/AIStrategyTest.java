package ai;

import java.util.Arrays;
import java.util.ArrayList;
import model.Meld;
import model.Hand;
import model.Tile;
import model.Game;

import ai.AIStrategy1;
import ai.AIStrategy2;
import ai.AIStrategy3;

import model.observable.ObservableTile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.containsInAnyOrder;

public class AIStrategyTest {

	ObservableTile t4 = new ObservableTile(3, Tile.Colours.BLUE);
	ObservableTile t5 = new ObservableTile(4, Tile.Colours.GREEN);
	ObservableTile t6 = new ObservableTile(7, Tile.Colours.RED);
	ObservableTile t7 = new ObservableTile(10, Tile.Colours.ORANGE);
	ObservableTile t8 = new ObservableTile(12, Tile.Colours.BLUE);
	ObservableTile t9 = new ObservableTile(5, Tile.Colours.BLUE);
	ObservableTile t10 = new ObservableTile(9, Tile.Colours.ORANGE);
	ObservableTile t11 = new ObservableTile(11, Tile.Colours.GREEN);
	ObservableTile t12 = new ObservableTile(11, Tile.Colours.BLUE);
	ObservableTile t13 = new ObservableTile(1, Tile.Colours.GREEN);
	ObservableTile t14 = new ObservableTile(11, Tile.Colours.RED);

	private AIPlayer ai1;
	private AIPlayer ai2;
	private AIPlayer ai3;
	private Hand ai1Hand;
	private Hand ai2Hand;
	private Hand ai3Hand;
	private ArrayList<Meld> melds;
	Game game;

	@BeforeEach
	public void setUp() {
		game = new Game();
		ai1Hand = new Hand();
		ai2Hand = new Hand();
		ai3Hand = new Hand();
		melds = new ArrayList<Meld>();
		ai1 = new AIPlayer(1, game, ai1Hand);
		ai2 = new AIPlayer(2, game, ai2Hand);
		ai3 = new AIPlayer(3, game, ai3Hand);
	}

	@Test
	public void testFindRunsInHand() {
		ObservableTile t1 = new ObservableTile(3, Tile.Colours.RED);
		ObservableTile t2 = new ObservableTile(5, Tile.Colours.RED);
		ObservableTile t3 = new ObservableTile(4, Tile.Colours.RED);
		ObservableTile t4 = new ObservableTile(4, Tile.Colours.BLUE);
		ObservableTile t5 = new ObservableTile(3, Tile.Colours.GREEN);
		ai1Hand.addTile(t1);
		ai1Hand.addTile(t2);
		ai1Hand.addTile(t3);
		ai1Hand.addTile(t4);
		ai1Hand.addTile(t5);

		melds = ((AIStrategy1)ai1.getAiStrategy()).findRunsInHand(ai1.getHand());
		assertThat(melds.size(), equalTo(1));
		assertThat(melds.get(0).getMeld(), containsInAnyOrder(Arrays.asList(equalTo(t1), equalTo(t2), equalTo(t3))));
	}

	@Test
	public void testFindSetsInHand() {
		ObservableTile t1 = new ObservableTile(3, Tile.Colours.RED);
		ObservableTile t2 = new ObservableTile(5, Tile.Colours.RED);
		ObservableTile t3 = new ObservableTile(4, Tile.Colours.RED);
		ObservableTile t4 = new ObservableTile(3, Tile.Colours.BLUE);
		ObservableTile t5 = new ObservableTile(3, Tile.Colours.GREEN);
		ai1Hand.addTile(t1);
		ai1Hand.addTile(t2);
		ai1Hand.addTile(t3);
		ai1Hand.addTile(t4);
		ai1Hand.addTile(t5);

		melds = ((AIStrategy1)ai1.getAiStrategy()).findSetsInHand(ai1.getHand());
		assertThat(melds.size(), equalTo(1));
		assertThat(melds.get(0).getMeld(), containsInAnyOrder(Arrays.asList(equalTo(t1), equalTo(t4), equalTo(t5))));
	}

	@Test
	public void testP1PlaysOneMeldOnFirstTurn() {
		ObservableTile t1 = new ObservableTile(10, Tile.Colours.RED);
		ObservableTile t2 = new ObservableTile(10, Tile.Colours.GREEN);
		ObservableTile t3 = new ObservableTile(10, Tile.Colours.ORANGE);
		ai1Hand.addTile(t1);
		ai1Hand.addTile(t2);
		ai1Hand.addTile(t3);

		boolean playedOneMeld = ((AIStrategy1)ai1.getAiStrategy()).firstHandStrategy();
		assertThat(playedOneMeld, is(true));
	}

	@Test
	public void testP1PlaysOneMeldOnSubsequentTurn() {
		ObservableTile t1 = new ObservableTile(3, Tile.Colours.RED);
		ObservableTile t2 = new ObservableTile(3, Tile.Colours.GREEN);
		ObservableTile t3 = new ObservableTile(3, Tile.Colours.ORANGE);
		ai1Hand.addTile(t1);
		ai1Hand.addTile(t2);
		ai1Hand.addTile(t3);
		
		//boolean playedOneMeld = ((AIStrategy1)ai1.getAiStrategy()).attemptRegularStrategy();
		//assertThat(playedOneMeld, is(true));
	}

	@Test
	public void testP1PlaysMultipleMeldsOnFirstTurn() {
		ObservableTile t1 = new ObservableTile(3, Tile.Colours.RED);
		ObservableTile t2 = new ObservableTile(5, Tile.Colours.RED);
		ObservableTile t3 = new ObservableTile(4, Tile.Colours.RED);
		ObservableTile t4 = new ObservableTile(9, Tile.Colours.BLUE);
		ObservableTile t5 = new ObservableTile(9, Tile.Colours.GREEN);
		ObservableTile t6 = new ObservableTile(9, Tile.Colours.ORANGE);		
		ai1Hand.addTile(t1);
		ai1Hand.addTile(t2);
		ai1Hand.addTile(t3);
		ai1Hand.addTile(t4);
		ai1Hand.addTile(t5);
		ai1Hand.addTile(t6);

		boolean playedMultipleMelds = ((AIStrategy1)ai1.getAiStrategy()).firstHandStrategy();
		assertThat(playedMultipleMelds, is(true));
	}

	@Test
	public void testP1PlaysMultipleMeldsOnSubsequentTurn() {
		ObservableTile t1 = new ObservableTile(3, Tile.Colours.RED);
		ObservableTile t2 = new ObservableTile(5, Tile.Colours.RED);
		ObservableTile t3 = new ObservableTile(4, Tile.Colours.RED);
		ObservableTile t4 = new ObservableTile(2, Tile.Colours.BLUE);
		ObservableTile t5 = new ObservableTile(3, Tile.Colours.BLUE);
		ObservableTile t6 = new ObservableTile(4, Tile.Colours.BLUE);		
		ai1Hand.addTile(t1);
		ai1Hand.addTile(t2);
		ai1Hand.addTile(t3);
		ai1Hand.addTile(t4);
		ai1Hand.addTile(t5);
		ai1Hand.addTile(t6);

		//boolean playedMultipleMelds = ((AIStrategy1)ai1.getAiStrategy()).attemptRegularStrategy();
		//assertThat(playedMultipleMelds, is(true));
	}

	@Test
	public void testP1DrawsOnFirstTurn() {
		ObservableTile t1 = new ObservableTile(3, Tile.Colours.RED);
		ObservableTile t2 = new ObservableTile(5, Tile.Colours.RED);
		ObservableTile t3 = new ObservableTile(4, Tile.Colours.RED);		
		ai1Hand.addTile(t1);
		ai1Hand.addTile(t2);
		ai1Hand.addTile(t3);

		boolean ableToPlayMeld = ((AIStrategy1)ai1.getAiStrategy()).firstHandStrategy();
		assertThat(ableToPlayMeld, is(false));
	}


	@Test
	public void testP1DrawsOnSubsequentTurn() {
		ObservableTile t1 = new ObservableTile(13, Tile.Colours.RED);
		ObservableTile t2 = new ObservableTile(5, Tile.Colours.GREEN);
		ObservableTile t3 = new ObservableTile(4, Tile.Colours.BLUE);	
		ObservableTile t4 = new ObservableTile(8, Tile.Colours.GREEN);
		ObservableTile t5 = new ObservableTile(10, Tile.Colours.ORANGE);	
		ai1Hand.addTile(t1);
		ai1Hand.addTile(t2);
		ai1Hand.addTile(t3);
		ai1Hand.addTile(t4);
		ai1Hand.addTile(t5);

		//boolean ableToPlayMeld = ((AIStrategy1)ai1.getAiStrategy()).attemptRegularStrategy();
		//assertThat(ableToPlayMeld, is(false));
	}

	@Test
	public void testP3Plays30PointsOnFirstTurn() {
		ObservableTile t1 = new ObservableTile(10, Tile.Colours.RED);
		ObservableTile t2 = new ObservableTile(10, Tile.Colours.GREEN);
		ObservableTile t3 = new ObservableTile(10, Tile.Colours.ORANGE);
		ai3Hand.addTile(t1);
		ai3Hand.addTile(t2);
		ai3Hand.addTile(t3);

		boolean played30Points = ((AIStrategy3)ai3.getAiStrategy()).firstHandStrategy();
		assertThat(played30Points, is(true));
	}

	@Test
	public void testP3Plays30PointsOnSubsequentTurn() {
		ObservableTile t1 = new ObservableTile(10, Tile.Colours.RED);
		ObservableTile t2 = new ObservableTile(12, Tile.Colours.RED);
		ObservableTile t3 = new ObservableTile(11, Tile.Colours.RED);
		ai3Hand.addTile(t1);
		ai3Hand.addTile(t2);
		ai3Hand.addTile(t3);

		//boolean played30Points = ((AIStrategy3)ai3.getAiStrategy()).attemptRegularStrategy();
		//assertThat(played30Points, is(true));
	}

	@Test
	public void testP2CanPlays30PointsWithMeldOnTable() {
		ObservableTile t1 = new ObservableTile(10, Tile.Colours.RED);
		ObservableTile t2 = new ObservableTile(12, Tile.Colours.RED);
		ObservableTile t3 = new ObservableTile(11, Tile.Colours.RED);
		ai2Hand.addTile(t1);
		ai2Hand.addTile(t2);
		ai2Hand.addTile(t3);

		game.addTileToTable(new ObservableTile(10, Tile.Colours.BLUE), 0, 0);
		game.addTileToTable(new ObservableTile(10, Tile.Colours.BLUE), 0, 1);
		game.addTileToTable(new ObservableTile(10, Tile.Colours.BLUE), 0, 2);

		boolean played30Points = ((AIStrategy2)ai2.getAiStrategy()).firstHandStrategy();
		assertThat(played30Points, is(true));
	}

	@Test
	public void testP2CanNotPlays30PointsWithMeldOnTable() {
		ObservableTile t1 = new ObservableTile(5, Tile.Colours.RED);
		ObservableTile t2 = new ObservableTile(5, Tile.Colours.GREEN);
		ObservableTile t3 = new ObservableTile(5, Tile.Colours.ORANGE);
		ai2Hand.addTile(t1);
		ai2Hand.addTile(t2);
		ai2Hand.addTile(t3);

		game.addTileToTable(new ObservableTile(10, Tile.Colours.BLUE), 0, 0);
		game.addTileToTable(new ObservableTile(10, Tile.Colours.BLUE), 0, 1);
		game.addTileToTable(new ObservableTile(10, Tile.Colours.BLUE), 0, 2);

		boolean played30Points = ((AIStrategy2)ai2.getAiStrategy()).firstHandStrategy();
		assertThat(played30Points, is(false));
	}
}