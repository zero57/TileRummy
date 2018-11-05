package ai;

import java.util.Arrays;
import java.util.ArrayList;
import model.Meld;
import model.Hand;
import model.Tile;
import model.Game;

import ai.AIStrategy1;

import model.observable.ObservableTile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.contains;

public class AIStrategy1Test {

	ObservableTile t1 = new ObservableTile(3, Tile.Colours.RED);
	ObservableTile t2 = new ObservableTile(5, Tile.Colours.RED);
	ObservableTile t3 = new ObservableTile(4, Tile.Colours.RED);
	ObservableTile t4 = new ObservableTile(3, Tile.Colours.BLUE);
	ObservableTile t5 = new ObservableTile(3, Tile.Colours.GREEN);
	ObservableTile t6 = new ObservableTile(7, Tile.Colours.RED);
	ObservableTile t7 = new ObservableTile(10, Tile.Colours.ORANGE);
	ObservableTile t8 = new ObservableTile(12, Tile.Colours.BLUE);
	ObservableTile t9 = new ObservableTile(4, Tile.Colours.BLUE);
	ObservableTile t10 = new ObservableTile(9, Tile.Colours.ORANGE);
	ObservableTile t11 = new ObservableTile(11, Tile.Colours.GREEN);
	ObservableTile t12 = new ObservableTile(11, Tile.Colours.BLUE);
	ObservableTile t13 = new ObservableTile(1, Tile.Colours.GREEN);
	ObservableTile t14 = new ObservableTile(2, Tile.Colours.RED);

	private AIPlayer ai1;
	private Hand hand;
	private ArrayList<Meld> melds;

	@BeforeEach
	public void setUp() {
		hand = new Hand();
		hand.addTile(t1);
		hand.addTile(t2);
		hand.addTile(t3);
		hand.addTile(t4);
		hand.addTile(t5);
		hand.addTile(t6);
		hand.addTile(t7);
		hand.addTile(t8);
		hand.addTile(t9);
		hand.addTile(t10);
		hand.addTile(t11);
		hand.addTile(t12);
		hand.addTile(t13);
		hand.addTile(t14);
		melds = new ArrayList<Meld>();
		ai1 = new AIPlayer(1,new Game(), hand);
	}

	@Test
	public void testFindRunsInHand() {
		melds = ((AIStrategy1)ai1.getAiStrategy()).findRunsInHand(ai1.getHand());
		assertThat(melds.size(), equalTo(1));
		assertThat(melds.get(0).getMeld(), contains(Arrays.asList(equalTo(t14), equalTo(t1), equalTo(t3), equalTo(t2))));
	}

	@Test
	public void testFindSetsInHand() {
		melds = ((AIStrategy1)ai1.getAiStrategy()).findSetsInHand(ai1.getHand());
		assertThat(melds.size(), equalTo(1));
		assertThat(melds.get(0).getMeld(), contains(Arrays.asList(equalTo(t1), equalTo(t4), equalTo(t5))));
	}
}