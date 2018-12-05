package model;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import model.observable.ObservableTile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import parser.FileParser;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

public class GameTest {
	private Game game;

	@BeforeEach
	public void setUp() {
		game = new Game();
		game.setStock(new Stock().shuffle());
	}

	@Test
	public void testDealInitialTiles() {
		assertThat(game.getStockSize(), is(106));
		game.dealInitialTiles();
		assertThat(game.getStockSize(), is(50));
	}
	
	@Test
	public void testTimerWorks() {
		Timeline timer = new Timeline(new KeyFrame(
		Duration.millis(3000),
		ae -> {}));
		assertThat(timer.getCurrentTime(), is(equalTo(new Duration(0))));
	}

	@Test
	public void testAddTileToTableAllAtBackOfRunMeld() {
		var t1 = new ObservableTile(1, Tile.Colours.RED);
		var t2 = new ObservableTile(2, Tile.Colours.RED);
		var t3 = new ObservableTile(3, Tile.Colours.RED);
		game.addTileToTable(t1, 0, 0);
		game.addTileToTable(t2, 0, 1);
		game.addTileToTable(t3, 0, 2);
		assertThat(game.getTable().size(), is(1));
		assertThat(game.getTable().get(0).getRow(), is(0));
		assertThat(game.getTable().get(0).getCol(), is(0));
		assertThat(game.getTable().get(0).getSize(), is(3));
		assertThat(game.getTable().get(0).getMeld(), contains(Arrays.asList(equalTo(t1), equalTo(t2), equalTo(t3))));
		assertThat(game.getTable().get(0).getType(), is(Meld.Types.RUN));
	}

	@Test
	public void testPenalizePlayerAndReturnToValidState() {
		int numPlayers = 4;
		game = new Game(new OptionChoices()
			.setNumPlayers(numPlayers)
			.setPlayer1(OptionChoices.Type.HUMAN.ordinal())
			.setPlayer2(OptionChoices.Type.HUMAN.ordinal())
			.setPlayer3(OptionChoices.Type.HUMAN.ordinal())
			.setPlayer4(OptionChoices.Type.HUMAN.ordinal()));
		game.setStock(new Stock().shuffle());
		game.dealInitialTiles();
		assertThat(game.getNumPlayers(), is(numPlayers));
		assertThat(game.getPlayerTurn(), is(0));
		assertThat(game.getPlayer1Hand().getTiles().size(), is(14));
		game.addTileToTable(game.getPlayer1Hand().getTiles().get(0), 0, 0);
		game.addTileToTable(game.getPlayer1Hand().getTiles().get(1), 0, 2);
		assertThat(game.getTable().size(), is(2));
		game.getPlayer1Hand().removeTile(game.getPlayer1Hand().getTiles().get(0));
		game.getPlayer1Hand().removeTile(game.getPlayer1Hand().getTiles().get(1));
		assertThat(game.getPlayer1Hand().getTiles().size(), is(12));
		game.endTurn(game.getPlayer1Hand()); // INVALID! Penalize and revert board.
		assertThat(game.getTable().size(), is(0));
		assertThat(game.getPlayer1Hand().getTiles().size(), is(17)); // Penalty of 3 tiles.
		assertThat(game.getPlayerTurn(), is(1));
	}

	@Test
	public void testAddTileToTableAllAtFrontOfRunMeld() {
		var t1 = new ObservableTile(1, Tile.Colours.RED);
		var t2 = new ObservableTile(2, Tile.Colours.RED);
		var t3 = new ObservableTile(3, Tile.Colours.RED);
		game.addTileToTable(t3, 0, 2);
		game.addTileToTable(t2, 0, 1);
		game.addTileToTable(t1, 0, 0);
		assertThat(game.getTable().size(), is(1));
		assertThat(game.getTable().get(0).getRow(), is(0));
		assertThat(game.getTable().get(0).getCol(), is(0));
		assertThat(game.getTable().get(0).getSize(), is(3));
		assertThat(game.getTable().get(0).getMeld(), contains(Arrays.asList(equalTo(t1), equalTo(t2), equalTo(t3))));
		assertThat(game.getTable().get(0).getType(), is(Meld.Types.RUN));
	}

	@Test
	public void testAddTileToTableBackThenFrontOfRunMeld() {
		var t1 = new ObservableTile(1, Tile.Colours.RED);
		var t2 = new ObservableTile(2, Tile.Colours.RED);
		var t3 = new ObservableTile(3, Tile.Colours.RED);
		game.addTileToTable(t2, 0, 1);
		game.addTileToTable(t3, 0, 2);
		game.addTileToTable(t1, 0, 0);
		assertThat(game.getTable().size(), is(1));
		assertThat(game.getTable().get(0).getRow(), is(0));
		assertThat(game.getTable().get(0).getCol(), is(0));
		assertThat(game.getTable().get(0).getSize(), is(3));
		assertThat(game.getTable().get(0).getMeld(), contains(Arrays.asList(equalTo(t1), equalTo(t2), equalTo(t3))));
		assertThat(game.getTable().get(0).getType(), is(Meld.Types.RUN));
	}

	@Test
	public void testAddTileToTableFrontThenBackOfRunMeld() {
		var t1 = new ObservableTile(1, Tile.Colours.RED);
		var t2 = new ObservableTile(2, Tile.Colours.RED);
		var t3 = new ObservableTile(3, Tile.Colours.RED);
		game.addTileToTable(t2, 0, 1);
		game.addTileToTable(t1, 0, 0);
		game.addTileToTable(t3, 0, 2);
		assertThat(game.getTable().size(), is(1));
		assertThat(game.getTable().get(0).getRow(), is(0));
		assertThat(game.getTable().get(0).getCol(), is(0));
		assertThat(game.getTable().get(0).getSize(), is(3));
		assertThat(game.getTable().get(0).getMeld(), contains(Arrays.asList(equalTo(t1), equalTo(t2), equalTo(t3))));
		assertThat(game.getTable().get(0).getType(), is(Meld.Types.RUN));
	}

	@Test
	public void testAddTileToTableAllAtBackOfSetMeld() {
		var t1 = new ObservableTile(3, Tile.Colours.RED);
		var t2 = new ObservableTile(3, Tile.Colours.ORANGE);
		var t3 = new ObservableTile(3, Tile.Colours.BLUE);
		game.addTileToTable(t1, 0, 0);
		game.addTileToTable(t2, 0, 1);
		game.addTileToTable(t3, 0, 2);
		assertThat(game.getTable().size(), is(1));
		assertThat(game.getTable().get(0).getRow(), is(0));
		assertThat(game.getTable().get(0).getCol(), is(0));
		assertThat(game.getTable().get(0).getSize(), is(3));
		assertThat(game.getTable().get(0).getMeld(), contains(Arrays.asList(equalTo(t1), equalTo(t2), equalTo(t3))));
		assertThat(game.getTable().get(0).getType(), is(Meld.Types.SET));
	}

	@Test
	public void testAddTileToTableAllAtFrontOfSetMeld() {
		var t1 = new ObservableTile(3, Tile.Colours.RED);
		var t2 = new ObservableTile(3, Tile.Colours.ORANGE);
		var t3 = new ObservableTile(3, Tile.Colours.BLUE);
		game.addTileToTable(t3, 0, 2);
		game.addTileToTable(t2, 0, 1);
		game.addTileToTable(t1, 0, 0);
		assertThat(game.getTable().size(), is(1));
		assertThat(game.getTable().get(0).getRow(), is(0));
		assertThat(game.getTable().get(0).getCol(), is(0));
		assertThat(game.getTable().get(0).getSize(), is(3));
		assertThat(game.getTable().get(0).getMeld(), contains(Arrays.asList(equalTo(t1), equalTo(t2), equalTo(t3))));
		assertThat(game.getTable().get(0).getType(), is(Meld.Types.SET));
	}

	@Test
	public void testAddTileToTableBackThenFrontOfSetMeld() {
		var t1 = new ObservableTile(3, Tile.Colours.RED);
		var t2 = new ObservableTile(3, Tile.Colours.ORANGE);
		var t3 = new ObservableTile(3, Tile.Colours.BLUE);
		game.addTileToTable(t2, 0, 1);
		game.addTileToTable(t3, 0, 2);
		game.addTileToTable(t1, 0, 0);
		assertThat(game.getTable().size(), is(1));
		assertThat(game.getTable().get(0).getRow(), is(0));
		assertThat(game.getTable().get(0).getCol(), is(0));
		assertThat(game.getTable().get(0).getSize(), is(3));
		assertThat(game.getTable().get(0).getMeld(), contains(Arrays.asList(equalTo(t1), equalTo(t2), equalTo(t3))));
		assertThat(game.getTable().get(0).getType(), is(Meld.Types.SET));
	}

	@Test
	public void testAddTileToTableFrontThenBackOfSetMeld() {
		var t1 = new ObservableTile(3, Tile.Colours.RED);
		var t2 = new ObservableTile(3, Tile.Colours.ORANGE);
		var t3 = new ObservableTile(3, Tile.Colours.BLUE);
		game.addTileToTable(t2, 0, 1);
		game.addTileToTable(t1, 0, 0);
		game.addTileToTable(t3, 0, 2);
		assertThat(game.getTable().size(), is(1));
		assertThat(game.getTable().get(0).getRow(), is(0));
		assertThat(game.getTable().get(0).getCol(), is(0));
		assertThat(game.getTable().get(0).getSize(), is(3));
		assertThat(game.getTable().get(0).getMeld(), contains(Arrays.asList(equalTo(t1), equalTo(t2), equalTo(t3))));
		assertThat(game.getTable().get(0).getType(), is(Meld.Types.SET));
	}

	@Test
	public void testRemoveTileFromTableFromBackOfRunMeld() {
		var t1 = new ObservableTile(1, Tile.Colours.RED);
		var t2 = new ObservableTile(2, Tile.Colours.RED);
		var t3 = new ObservableTile(3, Tile.Colours.RED);
		game.addTileToTable(t1, 0, 0);
		game.addTileToTable(t2, 0, 1);
		game.addTileToTable(t3, 0, 2);
		assertThat(game.getTable().size(), is(1));
		assertThat(game.getTable().get(0).getRow(), is(0));
		assertThat(game.getTable().get(0).getCol(), is(0));
		assertThat(game.getTable().get(0).getSize(), is(3));
		assertThat(game.getTable().get(0).getMeld(), contains(Arrays.asList(equalTo(t1), equalTo(t2), equalTo(t3))));
		assertThat(game.getTable().get(0).getType(), is(Meld.Types.RUN));

		game.removeTileFromTable(t3, 0, 2);
		assertThat(game.getTable().size(), is(1));
		assertThat(game.getTable().get(0).getRow(), is(0));
		assertThat(game.getTable().get(0).getCol(), is(0));
		assertThat(game.getTable().get(0).getSize(), is(2));
		assertThat(game.getTable().get(0).getMeld(), contains(Arrays.asList(equalTo(t1), equalTo(t2))));
	}

	@Test
	public void testRemoveTileFromTableFromFrontOfRunMeld() {
		var t1 = new ObservableTile(1, Tile.Colours.RED);
		var t2 = new ObservableTile(2, Tile.Colours.RED);
		var t3 = new ObservableTile(3, Tile.Colours.RED);
		game.addTileToTable(t1, 0, 0);
		game.addTileToTable(t2, 0, 1);
		game.addTileToTable(t3, 0, 2);
		assertThat(game.getTable().size(), is(1));
		assertThat(game.getTable().get(0).getRow(), is(0));
		assertThat(game.getTable().get(0).getCol(), is(0));
		assertThat(game.getTable().get(0).getSize(), is(3));
		assertThat(game.getTable().get(0).getMeld(), contains(Arrays.asList(equalTo(t1), equalTo(t2), equalTo(t3))));
		assertThat(game.getTable().get(0).getType(), is(Meld.Types.RUN));

		game.removeTileFromTable(t1, 0, 0);
		assertThat(game.getTable().size(), is(1));
		assertThat(game.getTable().get(0).getRow(), is(0));
		assertThat(game.getTable().get(0).getCol(), is(1));
		assertThat(game.getTable().get(0).getSize(), is(2));
		assertThat(game.getTable().get(0).getMeld(), contains(Arrays.asList(equalTo(t2), equalTo(t3))));
	}

	@Test
	public void testShouldRemoveRunMeldFromTableIfEmpty() {
		var t1 = new ObservableTile(1, Tile.Colours.RED);
		var t2 = new ObservableTile(2, Tile.Colours.RED);
		var t3 = new ObservableTile(3, Tile.Colours.RED);
		game.addTileToTable(t1, 0, 0);
		game.addTileToTable(t2, 0, 1);
		game.addTileToTable(t3, 0, 2);

		game.removeTileFromTable(t1, 0, 0);
		game.removeTileFromTable(t2, 0, 1);
		game.removeTileFromTable(t3, 0, 2);
		assertThat(game.getTable().size(), is(0));
	}

	@Test
	public void testShouldRemoveSetMeldFromTableIfEmpty() {
		var t1 = new ObservableTile(3, Tile.Colours.RED);
		var t2 = new ObservableTile(3, Tile.Colours.RED);
		var t3 = new ObservableTile(3, Tile.Colours.RED);
		game.addTileToTable(t1, 0, 0);
		game.addTileToTable(t2, 0, 1);
		game.addTileToTable(t3, 0, 2);

		game.removeTileFromTable(t1, 0, 0);
		game.removeTileFromTable(t2, 0, 1);
		game.removeTileFromTable(t3, 0, 2);
		assertThat(game.getTable().size(), is(0));
	}

	@Test
	public void testCannotRemoveTileFromMiddleOfMeldOnTable() {
		var t1 = new ObservableTile(1, Tile.Colours.RED);
		var t2 = new ObservableTile(2, Tile.Colours.RED);
		var t3 = new ObservableTile(3, Tile.Colours.RED);
		game.addTileToTable(t1, 0, 0);
		game.addTileToTable(t2, 0, 1);
		game.addTileToTable(t3, 0, 2);

		game.removeTileFromTable(t2, 0, 1);
		assertThat(game.getTable().get(0).getSize(), is(3));
		assertThat(game.getTable().get(0).getMeld(), contains(Arrays.asList(equalTo(t1), equalTo(t2), equalTo(t3))));
		assertThat(game.getTable().get(0).getType(), is(Meld.Types.RUN));
	}

	@Test
	public void testCannotAddInvalidTilesToRunMeldOnTable() {
		var t1 = new ObservableTile(1, Tile.Colours.RED);
		var t2 = new ObservableTile(2, Tile.Colours.RED);
		var t3 = new ObservableTile(3, Tile.Colours.RED);
		game.addTileToTable(t1, 0, 1);
		game.addTileToTable(t2, 0, 2);
		game.addTileToTable(t3, 0, 3);

		// Attempt to add to end of meld
		game.addTileToTable(new ObservableTile(5, Tile.Colours.RED), 0, 4);
		game.addTileToTable(new ObservableTile(5, Tile.Colours.BLUE), 0, 4);
		game.addTileToTable(new ObservableTile(5, Tile.Colours.GREEN), 0, 4);
		game.addTileToTable(new ObservableTile(5, Tile.Colours.ORANGE), 0, 4);

		// Attempt to add to start of meld
		game.addTileToTable(new ObservableTile(5, Tile.Colours.RED), 0, 0);
		game.addTileToTable(new ObservableTile(5, Tile.Colours.BLUE), 0, 0);
		game.addTileToTable(new ObservableTile(5, Tile.Colours.GREEN), 0, 0);
		game.addTileToTable(new ObservableTile(5, Tile.Colours.ORANGE), 0, 0);

		assertThat(game.getTable().size(), is(1));
		assertThat(game.getTable().get(0).getRow(), is(0));
		assertThat(game.getTable().get(0).getCol(), is(1));
		assertThat(game.getTable().get(0).getSize(), is(3));
		assertThat(game.getTable().get(0).getType(), is(Meld.Types.RUN));
		assertThat(game.getTable().get(0).getMeld(), contains(Arrays.asList(equalTo(t1), equalTo(t2), equalTo(t3))));
	}

	@Test
	public void testCannotAddInvalidTilesToSetMeldOnTable() {
		var t1 = new ObservableTile(3, Tile.Colours.RED);
		var t2 = new ObservableTile(3, Tile.Colours.ORANGE);
		var t3 = new ObservableTile(3, Tile.Colours.BLUE);
		game.addTileToTable(t1, 0, 1);
		game.addTileToTable(t2, 0, 2);
		game.addTileToTable(t3, 0, 3);

		// Attempt to add to end of meld
		game.addTileToTable(new ObservableTile(4, Tile.Colours.RED), 0, 0);
		game.addTileToTable(new ObservableTile(4, Tile.Colours.BLUE), 0, 4);
		game.addTileToTable(new ObservableTile(4, Tile.Colours.GREEN), 0, 4);
		game.addTileToTable(new ObservableTile(4, Tile.Colours.ORANGE), 0, 4);

		// Attempt to add to start of meld
		game.addTileToTable(new ObservableTile(4, Tile.Colours.RED), 0, 0);
		game.addTileToTable(new ObservableTile(4, Tile.Colours.BLUE), 0, 0);
		game.addTileToTable(new ObservableTile(4, Tile.Colours.GREEN), 0, 0);
		game.addTileToTable(new ObservableTile(4, Tile.Colours.ORANGE), 0, 0);

		assertThat(game.getTable().size(), is(1));
		assertThat(game.getTable().get(0).getRow(), is(0));
		assertThat(game.getTable().get(0).getCol(), is(1));
		assertThat(game.getTable().get(0).getSize(), is(3));
		assertThat(game.getTable().get(0).getType(), is(Meld.Types.SET));
		assertThat(game.getTable().get(0).getMeld(), contains(Arrays.asList(equalTo(t1), equalTo(t2), equalTo(t3))));
	}

	@Test
	public void testCannotAddMeldInBetweenTwoMeldsOnTable() {
		var t1 = new ObservableTile(1, Tile.Colours.RED);
		var t2 = new ObservableTile(2, Tile.Colours.RED);
		var t3 = new ObservableTile(3, Tile.Colours.RED);
		game.addTileToTable(t1, 0, 0);
		game.addTileToTable(t2, 0, 1);
		game.addTileToTable(t3, 0, 2);

		var t4 = new ObservableTile(4, Tile.Colours.RED);
		var t5 = new ObservableTile(4, Tile.Colours.BLUE);
		var t6 = new ObservableTile(4, Tile.Colours.GREEN);
		game.addTileToTable(t4, 0, 4);
		game.addTileToTable(t5, 0, 5);
		game.addTileToTable(t6, 0, 6);

		game.addTileToTable(new ObservableTile(4, Tile.Colours.RED), 0, 3);

		assertThat(game.getTable().size(), is(2));
		assertThat(game.getTable().get(0).getRow(), is(0));
		assertThat(game.getTable().get(0).getCol(), is(0));
		assertThat(game.getTable().get(0).getSize(), is(3));
		assertThat(game.getTable().get(0).getType(), is(Meld.Types.RUN));

		assertThat(game.getTable().get(1).getRow(), is(0));
		assertThat(game.getTable().get(1).getCol(), is(4));
		assertThat(game.getTable().get(1).getSize(), is(3));
		assertThat(game.getTable().get(1).getType(), is(Meld.Types.SET));
	}

	@Test
	public void testMoveTileFromOneMeldToAnotherMeld() {
		var t1 = new ObservableTile(1, Tile.Colours.RED);
		var t2 = new ObservableTile(2, Tile.Colours.RED);
		var t3 = new ObservableTile(3, Tile.Colours.RED);
		var t4 = new ObservableTile(4, Tile.Colours.RED);
		game.addTileToTable(t1, 0, 0);
		game.addTileToTable(t2, 0, 1);
		game.addTileToTable(t3, 0, 2);
		game.addTileToTable(t4, 0, 3);

		var t5 = new ObservableTile(4, Tile.Colours.ORANGE);
		var t6 = new ObservableTile(4, Tile.Colours.BLUE);
		var t7 = new ObservableTile(4, Tile.Colours.GREEN);
		game.addTileToTable(t5, 0, 5);
		game.addTileToTable(t6, 0, 6);
		game.addTileToTable(t7, 0, 7);

		game.removeTileFromTable(t4, 0, 3);
		game.addTileToTable(t4, 0, 4);

		assertThat(game.getTable().size(), is(2));
		assertThat(game.getTable().get(0).getRow(), is(0));
		assertThat(game.getTable().get(0).getCol(), is(0));
		assertThat(game.getTable().get(0).getSize(), is(3));
		assertThat(game.getTable().get(0).getType(), is(Meld.Types.RUN));

		assertThat(game.getTable().get(1).getRow(), is(0));
		assertThat(game.getTable().get(1).getCol(), is(4));
		assertThat(game.getTable().get(1).getSize(), is(4));
		assertThat(game.getTable().get(1).getType(), is(Meld.Types.SET));
	}

	@Test
	public void testPlayerCanPlayOneMeldOf30Points() {
		var t1 = new ObservableTile(10, Tile.Colours.RED);
		var t2 = new ObservableTile(10, Tile.Colours.BLUE);
		var t3 = new ObservableTile(10, Tile.Colours.GREEN);
		game.addTileToTable(t1, 0, 0);
		game.addTileToTable(t2, 0, 1);
		game.addTileToTable(t3, 0, 2);

		assertThat(game.getTable().size(), is(1));
		assertThat(game.getTable().get(0).isValidLength(), is(true));
		assertThat(game.getTable().get(0).getValue(), is(30));
	}

	@Test
	public void testPlayerCanPlayOneMeldOfMoreThan30Points() {
		var t1 = new ObservableTile(10, Tile.Colours.RED);
		var t2 = new ObservableTile(11, Tile.Colours.RED);
		var t3 = new ObservableTile(12, Tile.Colours.RED);
		game.addTileToTable(t1, 0, 0);
		game.addTileToTable(t2, 0, 1);
		game.addTileToTable(t3, 0, 2);

		assertThat(game.getTable().size(), is(1));
		assertThat(game.getTable().get(0).isValidLength(), is(true));
		assertThat(game.getTable().get(0).getValue(), is(33));
	}

	@Test
	public void testPlayerCanPlaySeveralMeldsAddingUpTo30Points() {
		var t1 = new ObservableTile(5, Tile.Colours.RED);
		var t2 = new ObservableTile(5, Tile.Colours.BLUE);
		var t3 = new ObservableTile(5, Tile.Colours.GREEN);
		game.addTileToTable(t1, 0, 0);
		game.addTileToTable(t2, 0, 1);
		game.addTileToTable(t3, 0, 2);

		var t4 = new ObservableTile(4, Tile.Colours.RED);
		var t5 = new ObservableTile(5, Tile.Colours.RED);
		var t6 = new ObservableTile(6, Tile.Colours.RED);
		game.addTileToTable(t4, 0, 4);
		game.addTileToTable(t5, 0, 5);
		game.addTileToTable(t6, 0, 6);

		assertThat(game.getTable().size(), is(2));
		assertThat(game.getTable().get(0).isValidLength(), is(true));
		assertThat(game.getTable().get(0).getValue(), is(15));
		assertThat(game.getTable().get(1).isValidLength(), is(true));
		assertThat(game.getTable().get(1).getValue(), is(15));
	}

	@Test
	public void testPlayerCanPlaySeveralRunMeldsAddingUpTo30Points() {
		var t1 = new ObservableTile(4, Tile.Colours.RED);
		var t2 = new ObservableTile(5, Tile.Colours.RED);
		var t3 = new ObservableTile(6, Tile.Colours.RED);
		game.addTileToTable(t1, 0, 0);
		game.addTileToTable(t2, 0, 1);
		game.addTileToTable(t3, 0, 2);

		var t4 = new ObservableTile(4, Tile.Colours.BLUE);
		var t5 = new ObservableTile(5, Tile.Colours.BLUE);
		var t6 = new ObservableTile(6, Tile.Colours.BLUE);
		game.addTileToTable(t4, 1, 0);
		game.addTileToTable(t5, 1, 1);
		game.addTileToTable(t6, 1, 2);

		assertThat(game.getTable().size(), is(2));
		assertThat(game.getTable().get(0).isValidLength(), is(true));
		assertThat(game.getTable().get(0).getValue(), is(15));
		assertThat(game.getTable().get(0).getType(), is(Meld.Types.RUN));
		assertThat(game.getTable().get(1).isValidLength(), is(true));
		assertThat(game.getTable().get(1).getValue(), is(15));
		assertThat(game.getTable().get(1).getType(), is(Meld.Types.RUN));
	}

	@Test
	public void testPlayerCanPlaySeveralSetMeldsAddingUpTo30Points() {
		var t1 = new ObservableTile(5, Tile.Colours.RED);
		var t2 = new ObservableTile(5, Tile.Colours.BLUE);
		var t3 = new ObservableTile(5, Tile.Colours.GREEN);
		game.addTileToTable(t1, 0, 0);
		game.addTileToTable(t2, 0, 1);
		game.addTileToTable(t3, 0, 2);

		var t4 = new ObservableTile(5, Tile.Colours.BLUE);
		var t5 = new ObservableTile(5, Tile.Colours.GREEN);
		var t6 = new ObservableTile(5, Tile.Colours.ORANGE);
		game.addTileToTable(t4, 1, 0);
		game.addTileToTable(t5, 1, 1);
		game.addTileToTable(t6, 1, 2);

		assertThat(game.getTable().size(), is(2));
		assertThat(game.getTable().get(0).isValidLength(), is(true));
		assertThat(game.getTable().get(0).getValue(), is(15));
		assertThat(game.getTable().get(0).getType(), is(Meld.Types.SET));
		assertThat(game.getTable().get(1).isValidLength(), is(true));
		assertThat(game.getTable().get(1).getValue(), is(15));
		assertThat(game.getTable().get(1).getType(), is(Meld.Types.SET));
	}

	@Test
	public void testPlayerCanPlaySeveralRunMeldsAddingUpToMoreThan30Points() {
		var t1 = new ObservableTile(7, Tile.Colours.RED);
		var t2 = new ObservableTile(8, Tile.Colours.RED);
		var t3 = new ObservableTile(9, Tile.Colours.RED);
		game.addTileToTable(t1, 0, 0);
		game.addTileToTable(t2, 0, 1);
		game.addTileToTable(t3, 0, 2);

		var t4 = new ObservableTile(10, Tile.Colours.BLUE);
		var t5 = new ObservableTile(11, Tile.Colours.BLUE);
		var t6 = new ObservableTile(12, Tile.Colours.BLUE);
		game.addTileToTable(t4, 0, 4);
		game.addTileToTable(t5, 0, 5);
		game.addTileToTable(t6, 0, 6);

		assertThat(game.getTable().size(), is(2));
		assertThat(game.getTable().get(0).isValidLength(), is(true));
		assertThat(game.getTable().get(0).getValue(), is(24));
		assertThat(game.getTable().get(0).getType(), is(Meld.Types.RUN));
		assertThat(game.getTable().get(1).isValidLength(), is(true));
		assertThat(game.getTable().get(1).getValue(), is(33));
		assertThat(game.getTable().get(1).getType(), is(Meld.Types.RUN));
	}

	@Test
	public void testPlayerCanPlaySeveralSetMeldsAddingUpToMoreThan30Points() {
		var t1 = new ObservableTile(7, Tile.Colours.RED);
		var t2 = new ObservableTile(7, Tile.Colours.BLUE);
		var t3 = new ObservableTile(7, Tile.Colours.GREEN);
		game.addTileToTable(t1, 0, 0);
		game.addTileToTable(t2, 0, 1);
		game.addTileToTable(t3, 0, 2);

		var t4 = new ObservableTile(10, Tile.Colours.RED);
		var t5 = new ObservableTile(10, Tile.Colours.BLUE);
		var t6 = new ObservableTile(10, Tile.Colours.ORANGE);
		game.addTileToTable(t4, 0, 4);
		game.addTileToTable(t5, 0, 5);
		game.addTileToTable(t6, 0, 6);

		assertThat(game.getTable().size(), is(2));
		assertThat(game.getTable().get(0).isValidLength(), is(true));
		assertThat(game.getTable().get(0).getValue(), is(21));
		assertThat(game.getTable().get(0).getType(), is(Meld.Types.SET));
		assertThat(game.getTable().get(1).isValidLength(), is(true));
		assertThat(game.getTable().get(1).getValue(), is(30));
		assertThat(game.getTable().get(1).getType(), is(Meld.Types.SET));
	}

	@Test
	public void testPlayerCanPlaySeveralMeldsAddingUpToMoreThan30Points() {
		var t1 = new ObservableTile(6, Tile.Colours.RED);
		var t2 = new ObservableTile(6, Tile.Colours.BLUE);
		var t3 = new ObservableTile(6, Tile.Colours.GREEN);
		game.addTileToTable(t1, 0, 0);
		game.addTileToTable(t2, 0, 1);
		game.addTileToTable(t3, 0, 2);

		var t4 = new ObservableTile(4, Tile.Colours.RED);
		var t5 = new ObservableTile(5, Tile.Colours.RED);
		var t6 = new ObservableTile(6, Tile.Colours.RED);
		game.addTileToTable(t4, 0, 4);
		game.addTileToTable(t5, 0, 5);
		game.addTileToTable(t6, 0, 6);

		assertThat(game.getTable().size(), is(2));
		assertThat(game.getTable().get(0).isValidLength(), is(true));
		assertThat(game.getTable().get(0).getValue(), is(18));
		assertThat(game.getTable().get(1).isValidLength(), is(true));
		assertThat(game.getTable().get(1).getValue(), is(15));
	}

	@Test
	public void testPlayerDrawsOneTileIfTheyChooseNotToPlay() {
		game.endTurn(game.getPlayer1Hand());
		assertThat(game.getPlayer1Hand().getTiles().size(), is(1));
		game.endTurn(game.getPlayer2Hand());
		assertThat(game.getPlayer2Hand().getTiles().size(), is(1));
		game.endTurn(game.getPlayer3Hand());
		assertThat(game.getPlayer3Hand().getTiles().size(), is(1));
		game.endTurn(game.getPlayer4Hand());
		assertThat(game.getPlayer4Hand().getTiles().size(), is(1));
	}

	@Test
	public void testPlayerCanPlaySeveralRuns() {
		var t1 = new ObservableTile(1, Tile.Colours.RED);
		var t2 = new ObservableTile(2, Tile.Colours.RED);
		var t3 = new ObservableTile(3, Tile.Colours.RED);
		var t4 = new ObservableTile(1, Tile.Colours.BLUE);
		var t5 = new ObservableTile(2, Tile.Colours.BLUE);
		var t6 = new ObservableTile(3, Tile.Colours.BLUE);
		game.addTileToTable(t1, 0, 0);
		game.addTileToTable(t2, 0, 1);
		game.addTileToTable(t3, 0, 2);
		game.addTileToTable(t4, 1, 0);
		game.addTileToTable(t5, 1, 1);
		game.addTileToTable(t6, 1, 2);
		assertThat(game.getTable().size(), is(2));
		assertThat(game.getTable().get(0).getRow(), is(0));
		assertThat(game.getTable().get(0).getCol(), is(0));
		assertThat(game.getTable().get(0).getSize(), is(3));
		assertThat(game.getTable().get(0).getMeld(), contains(Arrays.asList(equalTo(t1), equalTo(t2), equalTo(t3))));
		assertThat(game.getTable().get(0).getType(), is(Meld.Types.RUN));
		assertThat(game.getTable().get(1).getRow(), is(1));
		assertThat(game.getTable().get(1).getCol(), is(0));
		assertThat(game.getTable().get(1).getSize(), is(3));
		assertThat(game.getTable().get(1).getMeld(), contains(Arrays.asList(equalTo(t4), equalTo(t5), equalTo(t6))));
		assertThat(game.getTable().get(1).getType(), is(Meld.Types.RUN));
	}

	@Test
	public void testPlayerCanPlaySeveralSets() {
		var t1 = new ObservableTile(1, Tile.Colours.RED);
		var t2 = new ObservableTile(1, Tile.Colours.BLUE);
		var t3 = new ObservableTile(1, Tile.Colours.GREEN);
		var t4 = new ObservableTile(2, Tile.Colours.RED);
		var t5 = new ObservableTile(2, Tile.Colours.BLUE);
		var t6 = new ObservableTile(2, Tile.Colours.GREEN);
		game.addTileToTable(t1, 0, 0);
		game.addTileToTable(t2, 0, 1);
		game.addTileToTable(t3, 0, 2);
		game.addTileToTable(t4, 1, 0);
		game.addTileToTable(t5, 1, 1);
		game.addTileToTable(t6, 1, 2);
		assertThat(game.getTable().size(), is(2));
		assertThat(game.getTable().get(0).getRow(), is(0));
		assertThat(game.getTable().get(0).getCol(), is(0));
		assertThat(game.getTable().get(0).getSize(), is(3));
		assertThat(game.getTable().get(0).getMeld(), contains(Arrays.asList(equalTo(t1), equalTo(t2), equalTo(t3))));
		assertThat(game.getTable().get(0).getType(), is(Meld.Types.SET));
		assertThat(game.getTable().get(1).getRow(), is(1));
		assertThat(game.getTable().get(1).getCol(), is(0));
		assertThat(game.getTable().get(1).getSize(), is(3));
		assertThat(game.getTable().get(1).getMeld(), contains(Arrays.asList(equalTo(t4), equalTo(t5), equalTo(t6))));
		assertThat(game.getTable().get(1).getType(), is(Meld.Types.SET));
	}

	@Test
	public void testPlayerCanPlaySeveralTilesToSeveralMeldsOnTable() {
		game.addTileToTable(new ObservableTile(4, Tile.Colours.RED), 0, 3);
		game.addTileToTable(new ObservableTile(5, Tile.Colours.RED), 0, 4);
		game.addTileToTable(new ObservableTile(6, Tile.Colours.RED), 0, 5);
		game.addTileToTable(new ObservableTile(2, Tile.Colours.RED), 1, 0);
		game.addTileToTable(new ObservableTile(2, Tile.Colours.BLUE), 1, 1);
		game.addTileToTable(new ObservableTile(2, Tile.Colours.GREEN), 1, 2);

		var t1 = new ObservableTile(1, Tile.Colours.RED);
		var t2 = new ObservableTile(2, Tile.Colours.RED);
		var t3 = new ObservableTile(3, Tile.Colours.RED);
		game.addTileToTable(t3, 0, 2);
		game.addTileToTable(t2, 0, 1);
		game.addTileToTable(t1, 0, 0);

		var t4 = new ObservableTile(2, Tile.Colours.ORANGE);
		game.addTileToTable(t4, 1, 3);

		assertThat(game.getTable().size(), is(2));
		assertThat(game.getTable().get(0).isValidLength(), is(true));
		assertThat(game.getTable().get(0).getValue(), is(21));
		assertThat(game.getTable().get(0).getType(), is(Meld.Types.RUN));
		assertThat(game.getTable().get(1).isValidLength(), is(true));
		assertThat(game.getTable().get(1).getValue(), is(8));
		assertThat(game.getTable().get(1).getType(), is(Meld.Types.SET));
	}

	@Test
	public void testPlayerCanPlayAMeldByReorganizingSeveralMeldsOnTable() {
		// setup
		var t1 = new ObservableTile(4, Tile.Colours.RED);
		game.addTileToTable(t1, 0, 0);
		game.addTileToTable(new ObservableTile(5, Tile.Colours.RED), 0, 1);
		game.addTileToTable(new ObservableTile(6, Tile.Colours.RED), 0, 2);
		game.addTileToTable(new ObservableTile(7, Tile.Colours.RED), 0, 3);

		var t2 = new ObservableTile(3, Tile.Colours.RED);
		game.addTileToTable(t2, 1, 0);
		game.addTileToTable(new ObservableTile(3, Tile.Colours.BLUE), 1, 1);
		game.addTileToTable(new ObservableTile(3, Tile.Colours.GREEN), 1, 2);
		game.addTileToTable(new ObservableTile(3, Tile.Colours.ORANGE), 1, 3);

		var t3 = new ObservableTile(5, Tile.Colours.RED);
		game.addTileToTable(t3, 2, 0);
		game.addTileToTable(new ObservableTile(5, Tile.Colours.BLUE), 2, 1);
		game.addTileToTable(new ObservableTile(5, Tile.Colours.GREEN), 2, 2);
		game.addTileToTable(new ObservableTile(5, Tile.Colours.ORANGE), 2, 3);

		// Now play a meld by reorganizing
		game.removeTileFromTable(t2, 0, 0);
		game.addTileToTable(t2, 3, 0);
		game.removeTileFromTable(t1, 1, 0);
		game.addTileToTable(t1, 3, 1);
		game.removeTileFromTable(t3, 2, 0);
		game.addTileToTable(t3, 3, 2);

		assertThat(game.getTable().size(), is(4));
		assertThat(game.getTable().get(0).isValidLength(), is(true));
		assertThat(game.getTable().get(0).getType(), is(Meld.Types.RUN));
		assertThat(game.getTable().get(1).isValidLength(), is(true));
		assertThat(game.getTable().get(1).getType(), is(Meld.Types.SET));
		assertThat(game.getTable().get(2).isValidLength(), is(true));
		assertThat(game.getTable().get(2).getType(), is(Meld.Types.SET));
		assertThat(game.getTable().get(3).isValidLength(), is(true));
		assertThat(game.getTable().get(3).getType(), is(Meld.Types.RUN));
	}

	@Test
	public void testCorrectPlayerOrder() {
		assertThat(game.getPlayerTurn(), is(0));
		game.endTurn(game.getPlayer1Hand());
		assertThat(game.getPlayerTurn(), is(1));
		game.endTurn(game.getPlayer2Hand());
		assertThat(game.getPlayerTurn(), is(2));
		game.endTurn(game.getPlayer3Hand());
		assertThat(game.getPlayerTurn(), is(3));
		game.endTurn(game.getPlayer4Hand());
		assertThat(game.getPlayerTurn(), is(0));
	}

	@Test
	public void test2PlayerCorrectOrder() {
		int numPlayers = 2;
		game = new Game(new OptionChoices()
				.setNumPlayers(numPlayers)
				.setPlayer1(OptionChoices.Type.HUMAN.ordinal())
				.setPlayer2(OptionChoices.Type.AI1.ordinal()));
		game.setStock(new Stock().shuffle());
		assertThat(game.getNumPlayers(), is(numPlayers));
		assertThat(game.getPlayerTurn(), is(0));
		game.endTurn(game.getPlayer1Hand());
		assertThat(game.getPlayerTurn(), is(1));
		game.endTurn(game.getPlayer2Hand());
		assertThat(game.getPlayerTurn(), is(0));
	}

	@Test
	public void test3PlayerCorrectOrder() {
		int numPlayers = 3;
		game = new Game(new OptionChoices()
				.setNumPlayers(numPlayers)
				.setPlayer1(OptionChoices.Type.HUMAN.ordinal())
				.setPlayer2(OptionChoices.Type.AI1.ordinal())
				.setPlayer3(OptionChoices.Type.AI2.ordinal()));
		game.setStock(new Stock().shuffle());
		assertThat(game.getNumPlayers(), is(numPlayers));
		assertThat(game.getPlayerTurn(), is(0));
		game.endTurn(game.getPlayer1Hand());
		assertThat(game.getPlayerTurn(), is(1));
		game.endTurn(game.getPlayer2Hand());
		assertThat(game.getPlayerTurn(), is(2));
		game.endTurn(game.getPlayer3Hand());
		assertThat(game.getPlayerTurn(), is(0));
	}

	@Test
	public void test4PlayerCorrectOrder() {
		int numPlayers = 4;
		game = new Game(new OptionChoices()
				.setNumPlayers(numPlayers)
				.setPlayer1(OptionChoices.Type.HUMAN.ordinal())
				.setPlayer2(OptionChoices.Type.AI1.ordinal())
				.setPlayer3(OptionChoices.Type.AI2.ordinal())
				.setPlayer4(OptionChoices.Type.AI3.ordinal()));
		game.setStock(new Stock().shuffle());
		assertThat(game.getNumPlayers(), is(numPlayers));

		assertThat(game.getPlayerTurn(), is(0));
		game.endTurn(game.getPlayer1Hand());
		assertThat(game.getPlayerTurn(), is(1));
		game.endTurn(game.getPlayer2Hand());
		assertThat(game.getPlayerTurn(), is(2));
		game.endTurn(game.getPlayer3Hand());
		assertThat(game.getPlayerTurn(), is(3));
		game.endTurn(game.getPlayer4Hand());
		assertThat(game.getPlayerTurn(), is(0));
	}

	@Test
	public void test2Player2AI() {
		int numPlayers = 2;
		game = new Game(new OptionChoices()
				.setNumPlayers(numPlayers)
				.setPlayer1(OptionChoices.Type.AI1.ordinal())
				.setPlayer2(OptionChoices.Type.AI2.ordinal()));
		game.setStock(new Stock().shuffle());
		assertThat(game.getNumPlayers(), is(numPlayers));

		assertThat(game.getPlayerTurn(), is(0));
		game.endTurn(game.getPlayer1Hand());
		assertThat(game.getPlayerTurn(), is(1));
		game.endTurn(game.getPlayer2Hand());
		assertThat(game.getPlayerTurn(), is(0));
	}

	@Test
	public void test2Player1AI1Human() {
		int numPlayers = 2;
		game = new Game(new OptionChoices()
				.setNumPlayers(numPlayers)
				.setPlayer1(OptionChoices.Type.AI1.ordinal())
				.setPlayer2(OptionChoices.Type.HUMAN.ordinal()));
		game.setStock(new Stock().shuffle());
		assertThat(game.getNumPlayers(), is(numPlayers));

		assertThat(game.getPlayerTurn(), is(0));
		game.endTurn(game.getPlayer1Hand());
		assertThat(game.getPlayerTurn(), is(1));
		game.endTurn(game.getPlayer2Hand());
		assertThat(game.getPlayerTurn(), is(0));
	}

	@Test
	public void test3Player3AI() {
		int numPlayers = 3;
		game = new Game(new OptionChoices()
				.setNumPlayers(numPlayers)
				.setPlayer1(OptionChoices.Type.AI1.ordinal())
				.setPlayer2(OptionChoices.Type.AI2.ordinal())
				.setPlayer3(OptionChoices.Type.AI3.ordinal()));
		game.setStock(new Stock().shuffle());
		assertThat(game.getNumPlayers(), is(numPlayers));

		assertThat(game.getPlayerTurn(), is(0));
		game.endTurn(game.getPlayer1Hand());
		assertThat(game.getPlayerTurn(), is(1));
		game.endTurn(game.getPlayer2Hand());
		assertThat(game.getPlayerTurn(), is(2));
		game.endTurn(game.getPlayer3Hand());
		assertThat(game.getPlayerTurn(), is(0));
	}

	@Test
	public void test3Player3Human() {
		int numPlayers = 3;
		game = new Game(new OptionChoices()
				.setNumPlayers(numPlayers)
				.setPlayer1(OptionChoices.Type.HUMAN.ordinal())
				.setPlayer2(OptionChoices.Type.HUMAN.ordinal())
				.setPlayer3(OptionChoices.Type.HUMAN.ordinal()));
		game.setStock(new Stock().shuffle());
		assertThat(game.getNumPlayers(), is(numPlayers));

		assertThat(game.getPlayerTurn(), is(0));
		game.endTurn(game.getPlayer1Hand());
		assertThat(game.getPlayerTurn(), is(1));
		game.endTurn(game.getPlayer2Hand());
		assertThat(game.getPlayerTurn(), is(2));
		game.endTurn(game.getPlayer3Hand());
		assertThat(game.getPlayerTurn(), is(0));
	}

	@Test
	public void test4Player4AI() {
		int numPlayers = 4;
		game = new Game(new OptionChoices()
				.setNumPlayers(numPlayers)
				.setPlayer1(OptionChoices.Type.AI2.ordinal())
				.setPlayer2(OptionChoices.Type.AI1.ordinal())
				.setPlayer3(OptionChoices.Type.AI2.ordinal())
				.setPlayer4(OptionChoices.Type.AI3.ordinal()));
		game.setStock(new Stock().shuffle());
		assertThat(game.getNumPlayers(), is(numPlayers));

		assertThat(game.getPlayerTurn(), is(0));
		game.endTurn(game.getPlayer1Hand());
		assertThat(game.getPlayerTurn(), is(1));
		game.endTurn(game.getPlayer2Hand());
		assertThat(game.getPlayerTurn(), is(2));
		game.endTurn(game.getPlayer3Hand());
		assertThat(game.getPlayerTurn(), is(3));
		game.endTurn(game.getPlayer4Hand());
		assertThat(game.getPlayerTurn(), is(0));
	}

	@Test
	public void test4Player4Human() {
		int numPlayers = 4;
		game = new Game(new OptionChoices()
				.setNumPlayers(numPlayers)
				.setPlayer1(OptionChoices.Type.HUMAN.ordinal())
				.setPlayer2(OptionChoices.Type.HUMAN.ordinal())
				.setPlayer3(OptionChoices.Type.HUMAN.ordinal())
				.setPlayer4(OptionChoices.Type.HUMAN.ordinal()));
		game.setStock(new Stock().shuffle());
		assertThat(game.getNumPlayers(), is(numPlayers));

		assertThat(game.getPlayerTurn(), is(0));
		game.endTurn(game.getPlayer1Hand());
		assertThat(game.getPlayerTurn(), is(1));
		game.endTurn(game.getPlayer2Hand());
		assertThat(game.getPlayerTurn(), is(2));
		game.endTurn(game.getPlayer3Hand());
		assertThat(game.getPlayerTurn(), is(3));
		game.endTurn(game.getPlayer4Hand());
		assertThat(game.getPlayerTurn(), is(0));
	}

	@Test
	public void test4Player2AI1Human1AI() {
		int numPlayers = 4;
		game = new Game(new OptionChoices()
				.setNumPlayers(numPlayers)
				.setPlayer1(OptionChoices.Type.AI1.ordinal())
				.setPlayer2(OptionChoices.Type.AI2.ordinal())
				.setPlayer3(OptionChoices.Type.HUMAN.ordinal())
				.setPlayer4(OptionChoices.Type.AI1.ordinal()));
		game.setStock(new Stock().shuffle());
		assertThat(game.getNumPlayers(), is(numPlayers));

		assertThat(game.getPlayerTurn(), is(0));
		game.endTurn(game.getPlayer1Hand());
		assertThat(game.getPlayerTurn(), is(1));
		game.endTurn(game.getPlayer2Hand());
		assertThat(game.getPlayerTurn(), is(2));
		game.endTurn(game.getPlayer3Hand());
		assertThat(game.getPlayerTurn(), is(3));
		game.endTurn(game.getPlayer4Hand());
		assertThat(game.getPlayerTurn(), is(0));
	}

	@Test
	public void testHandRigging2Players() {
		int numPlayers = 2;
		game = new Game(new OptionChoices()
				.setNumPlayers(numPlayers)
				.setPlayer1(OptionChoices.Type.HUMAN.ordinal())
				.setPlayer2(OptionChoices.Type.AI2.ordinal())
				.setHandRigFilePath("src/main/resources/handRiggingExampleFiles/twoPlayerHandRigging")
		);
		assertThat(game.getNumPlayers(), is(numPlayers));
		assertThat(game.getStockSize(), is(78));
		assertThat(game.getPlayer1Hand().getSizeProperty().getValue(), is(14));
		assertThat(game.getPlayer2Hand().getSizeProperty().getValue(), is(14));

		assertThat(game.getPlayer1Hand().getTiles().get(0).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer1Hand().getTiles().get(0).getRank(), is(1));
		assertThat(game.getPlayer1Hand().getTiles().get(1).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer1Hand().getTiles().get(1).getRank(), is(2));
		assertThat(game.getPlayer1Hand().getTiles().get(2).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer1Hand().getTiles().get(2).getRank(), is(3));
		assertThat(game.getPlayer1Hand().getTiles().get(3).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer1Hand().getTiles().get(3).getRank(), is(4));
		assertThat(game.getPlayer1Hand().getTiles().get(4).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer1Hand().getTiles().get(4).getRank(), is(5));
		assertThat(game.getPlayer1Hand().getTiles().get(5).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer1Hand().getTiles().get(5).getRank(), is(6));
		assertThat(game.getPlayer1Hand().getTiles().get(6).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer1Hand().getTiles().get(6).getRank(), is(7));
		assertThat(game.getPlayer1Hand().getTiles().get(7).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer1Hand().getTiles().get(7).getRank(), is(8));
		assertThat(game.getPlayer1Hand().getTiles().get(8).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer1Hand().getTiles().get(8).getRank(), is(9));
		assertThat(game.getPlayer1Hand().getTiles().get(9).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer1Hand().getTiles().get(9).getRank(), is(10));
		assertThat(game.getPlayer1Hand().getTiles().get(10).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer1Hand().getTiles().get(10).getRank(), is(11));
		assertThat(game.getPlayer1Hand().getTiles().get(11).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer1Hand().getTiles().get(11).getRank(), is(12));
		assertThat(game.getPlayer1Hand().getTiles().get(12).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer1Hand().getTiles().get(12).getRank(), is(13));
		assertThat(game.getPlayer1Hand().getTiles().get(13).getColour(), is(Tile.Colours.BLUE));
		assertThat(game.getPlayer1Hand().getTiles().get(13).getRank(), is(1));

		assertThat(game.getPlayer2Hand().getTiles().get(0).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer2Hand().getTiles().get(0).getRank(), is(1));
		assertThat(game.getPlayer2Hand().getTiles().get(1).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer2Hand().getTiles().get(1).getRank(), is(2));
		assertThat(game.getPlayer2Hand().getTiles().get(2).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer2Hand().getTiles().get(2).getRank(), is(3));
		assertThat(game.getPlayer2Hand().getTiles().get(3).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer2Hand().getTiles().get(3).getRank(), is(4));
		assertThat(game.getPlayer2Hand().getTiles().get(4).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer2Hand().getTiles().get(4).getRank(), is(5));
		assertThat(game.getPlayer2Hand().getTiles().get(5).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer2Hand().getTiles().get(5).getRank(), is(6));
		assertThat(game.getPlayer2Hand().getTiles().get(6).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer2Hand().getTiles().get(6).getRank(), is(7));
		assertThat(game.getPlayer2Hand().getTiles().get(7).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer2Hand().getTiles().get(7).getRank(), is(8));
		assertThat(game.getPlayer2Hand().getTiles().get(8).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer2Hand().getTiles().get(8).getRank(), is(9));
		assertThat(game.getPlayer2Hand().getTiles().get(9).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer2Hand().getTiles().get(9).getRank(), is(10));
		assertThat(game.getPlayer2Hand().getTiles().get(10).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer2Hand().getTiles().get(10).getRank(), is(11));
		assertThat(game.getPlayer2Hand().getTiles().get(11).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer2Hand().getTiles().get(11).getRank(), is(12));
		assertThat(game.getPlayer2Hand().getTiles().get(12).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer2Hand().getTiles().get(12).getRank(), is(13));
		assertThat(game.getPlayer2Hand().getTiles().get(13).getColour(), is(Tile.Colours.BLUE));
		assertThat(game.getPlayer2Hand().getTiles().get(13).getRank(), is(1));
	}

	@Test
	public void testHandRigging3Players() {
		int numPlayers = 3;
		game = new Game(new OptionChoices()
				.setNumPlayers(numPlayers)
				.setPlayer1(OptionChoices.Type.HUMAN.ordinal())
				.setPlayer2(OptionChoices.Type.AI2.ordinal())
				.setPlayer3(OptionChoices.Type.AI3.ordinal())
				.setHandRigFilePath("src/main/resources/handRiggingExampleFiles/threePlayerHandRigging")
		);
		assertThat(game.getNumPlayers(), is(numPlayers));
		assertThat(game.getStockSize(), is(64));
		assertThat(game.getPlayer1Hand().getSizeProperty().getValue(), is(14));
		assertThat(game.getPlayer2Hand().getSizeProperty().getValue(), is(14));
		assertThat(game.getPlayer3Hand().getSizeProperty().getValue(), is(14));

		assertThat(game.getPlayer1Hand().getTiles().get(0).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer1Hand().getTiles().get(0).getRank(), is(1));
		assertThat(game.getPlayer1Hand().getTiles().get(1).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer1Hand().getTiles().get(1).getRank(), is(2));
		assertThat(game.getPlayer1Hand().getTiles().get(2).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer1Hand().getTiles().get(2).getRank(), is(3));
		assertThat(game.getPlayer1Hand().getTiles().get(3).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer1Hand().getTiles().get(3).getRank(), is(4));
		assertThat(game.getPlayer1Hand().getTiles().get(4).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer1Hand().getTiles().get(4).getRank(), is(5));
		assertThat(game.getPlayer1Hand().getTiles().get(5).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer1Hand().getTiles().get(5).getRank(), is(6));
		assertThat(game.getPlayer1Hand().getTiles().get(6).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer1Hand().getTiles().get(6).getRank(), is(7));
		assertThat(game.getPlayer1Hand().getTiles().get(7).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer1Hand().getTiles().get(7).getRank(), is(8));
		assertThat(game.getPlayer1Hand().getTiles().get(8).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer1Hand().getTiles().get(8).getRank(), is(9));
		assertThat(game.getPlayer1Hand().getTiles().get(9).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer1Hand().getTiles().get(9).getRank(), is(10));
		assertThat(game.getPlayer1Hand().getTiles().get(10).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer1Hand().getTiles().get(10).getRank(), is(11));
		assertThat(game.getPlayer1Hand().getTiles().get(11).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer1Hand().getTiles().get(11).getRank(), is(12));
		assertThat(game.getPlayer1Hand().getTiles().get(12).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer1Hand().getTiles().get(12).getRank(), is(13));
		assertThat(game.getPlayer1Hand().getTiles().get(13).getColour(), is(Tile.Colours.BLUE));
		assertThat(game.getPlayer1Hand().getTiles().get(13).getRank(), is(1));

		assertThat(game.getPlayer2Hand().getTiles().get(0).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer2Hand().getTiles().get(0).getRank(), is(1));
		assertThat(game.getPlayer2Hand().getTiles().get(1).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer2Hand().getTiles().get(1).getRank(), is(2));
		assertThat(game.getPlayer2Hand().getTiles().get(2).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer2Hand().getTiles().get(2).getRank(), is(3));
		assertThat(game.getPlayer2Hand().getTiles().get(3).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer2Hand().getTiles().get(3).getRank(), is(4));
		assertThat(game.getPlayer2Hand().getTiles().get(4).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer2Hand().getTiles().get(4).getRank(), is(5));
		assertThat(game.getPlayer2Hand().getTiles().get(5).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer2Hand().getTiles().get(5).getRank(), is(6));
		assertThat(game.getPlayer2Hand().getTiles().get(6).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer2Hand().getTiles().get(6).getRank(), is(7));
		assertThat(game.getPlayer2Hand().getTiles().get(7).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer2Hand().getTiles().get(7).getRank(), is(8));
		assertThat(game.getPlayer2Hand().getTiles().get(8).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer2Hand().getTiles().get(8).getRank(), is(9));
		assertThat(game.getPlayer2Hand().getTiles().get(9).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer2Hand().getTiles().get(9).getRank(), is(10));
		assertThat(game.getPlayer2Hand().getTiles().get(10).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer2Hand().getTiles().get(10).getRank(), is(11));
		assertThat(game.getPlayer2Hand().getTiles().get(11).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer2Hand().getTiles().get(11).getRank(), is(12));
		assertThat(game.getPlayer2Hand().getTiles().get(12).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer2Hand().getTiles().get(12).getRank(), is(13));
		assertThat(game.getPlayer2Hand().getTiles().get(13).getColour(), is(Tile.Colours.BLUE));
		assertThat(game.getPlayer2Hand().getTiles().get(13).getRank(), is(1));

		assertThat(game.getPlayer3Hand().getTiles().get(0).getColour(), is(Tile.Colours.BLUE));
		assertThat(game.getPlayer3Hand().getTiles().get(0).getRank(), is(2));
		assertThat(game.getPlayer3Hand().getTiles().get(1).getColour(), is(Tile.Colours.ORANGE));
		assertThat(game.getPlayer3Hand().getTiles().get(1).getRank(), is(1));
		assertThat(game.getPlayer3Hand().getTiles().get(2).getColour(), is(Tile.Colours.ORANGE));
		assertThat(game.getPlayer3Hand().getTiles().get(2).getRank(), is(2));
		assertThat(game.getPlayer3Hand().getTiles().get(3).getColour(), is(Tile.Colours.ORANGE));
		assertThat(game.getPlayer3Hand().getTiles().get(3).getRank(), is(3));
		assertThat(game.getPlayer3Hand().getTiles().get(4).getColour(), is(Tile.Colours.ORANGE));
		assertThat(game.getPlayer3Hand().getTiles().get(4).getRank(), is(4));
		assertThat(game.getPlayer3Hand().getTiles().get(5).getColour(), is(Tile.Colours.ORANGE));
		assertThat(game.getPlayer3Hand().getTiles().get(5).getRank(), is(5));
		assertThat(game.getPlayer3Hand().getTiles().get(6).getColour(), is(Tile.Colours.ORANGE));
		assertThat(game.getPlayer3Hand().getTiles().get(6).getRank(), is(6));
		assertThat(game.getPlayer3Hand().getTiles().get(7).getColour(), is(Tile.Colours.ORANGE));
		assertThat(game.getPlayer3Hand().getTiles().get(7).getRank(), is(7));
		assertThat(game.getPlayer3Hand().getTiles().get(8).getColour(), is(Tile.Colours.ORANGE));
		assertThat(game.getPlayer3Hand().getTiles().get(8).getRank(), is(8));
		assertThat(game.getPlayer3Hand().getTiles().get(9).getColour(), is(Tile.Colours.ORANGE));
		assertThat(game.getPlayer3Hand().getTiles().get(9).getRank(), is(9));
		assertThat(game.getPlayer3Hand().getTiles().get(10).getColour(), is(Tile.Colours.ORANGE));
		assertThat(game.getPlayer3Hand().getTiles().get(10).getRank(), is(10));
		assertThat(game.getPlayer3Hand().getTiles().get(11).getColour(), is(Tile.Colours.ORANGE));
		assertThat(game.getPlayer3Hand().getTiles().get(11).getRank(), is(11));
		assertThat(game.getPlayer3Hand().getTiles().get(12).getColour(), is(Tile.Colours.ORANGE));
		assertThat(game.getPlayer3Hand().getTiles().get(12).getRank(), is(12));
		assertThat(game.getPlayer3Hand().getTiles().get(13).getColour(), is(Tile.Colours.ORANGE));
		assertThat(game.getPlayer3Hand().getTiles().get(13).getRank(), is(13));
	}

	@Test
	public void testHandRigging4Players() {
		int numPlayers = 4;
		game = new Game(new OptionChoices()
				.setNumPlayers(numPlayers)
				.setPlayer1(OptionChoices.Type.HUMAN.ordinal())
				.setPlayer2(OptionChoices.Type.AI2.ordinal())
				.setPlayer3(OptionChoices.Type.AI3.ordinal())
				.setPlayer4(OptionChoices.Type.HUMAN.ordinal())
				.setHandRigFilePath("src/main/resources/handRiggingExampleFiles/fourPlayerHandRigging")
		);
		assertThat(game.getNumPlayers(), is(numPlayers));
		assertThat(game.getStockSize(), is(50));
		assertThat(game.getPlayer1Hand().getSizeProperty().getValue(), is(14));
		assertThat(game.getPlayer2Hand().getSizeProperty().getValue(), is(14));
		assertThat(game.getPlayer3Hand().getSizeProperty().getValue(), is(14));
		assertThat(game.getPlayer4Hand().getSizeProperty().getValue(), is(14));

		assertThat(game.getPlayer1Hand().getTiles().get(0).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer1Hand().getTiles().get(0).getRank(), is(1));
		assertThat(game.getPlayer1Hand().getTiles().get(1).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer1Hand().getTiles().get(1).getRank(), is(2));
		assertThat(game.getPlayer1Hand().getTiles().get(2).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer1Hand().getTiles().get(2).getRank(), is(3));
		assertThat(game.getPlayer1Hand().getTiles().get(3).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer1Hand().getTiles().get(3).getRank(), is(4));
		assertThat(game.getPlayer1Hand().getTiles().get(4).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer1Hand().getTiles().get(4).getRank(), is(5));
		assertThat(game.getPlayer1Hand().getTiles().get(5).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer1Hand().getTiles().get(5).getRank(), is(6));
		assertThat(game.getPlayer1Hand().getTiles().get(6).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer1Hand().getTiles().get(6).getRank(), is(7));
		assertThat(game.getPlayer1Hand().getTiles().get(7).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer1Hand().getTiles().get(7).getRank(), is(8));
		assertThat(game.getPlayer1Hand().getTiles().get(8).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer1Hand().getTiles().get(8).getRank(), is(9));
		assertThat(game.getPlayer1Hand().getTiles().get(9).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer1Hand().getTiles().get(9).getRank(), is(10));
		assertThat(game.getPlayer1Hand().getTiles().get(10).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer1Hand().getTiles().get(10).getRank(), is(11));
		assertThat(game.getPlayer1Hand().getTiles().get(11).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer1Hand().getTiles().get(11).getRank(), is(12));
		assertThat(game.getPlayer1Hand().getTiles().get(12).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer1Hand().getTiles().get(12).getRank(), is(13));
		assertThat(game.getPlayer1Hand().getTiles().get(13).getColour(), is(Tile.Colours.BLUE));
		assertThat(game.getPlayer1Hand().getTiles().get(13).getRank(), is(1));

		assertThat(game.getPlayer2Hand().getTiles().get(0).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer2Hand().getTiles().get(0).getRank(), is(1));
		assertThat(game.getPlayer2Hand().getTiles().get(1).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer2Hand().getTiles().get(1).getRank(), is(2));
		assertThat(game.getPlayer2Hand().getTiles().get(2).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer2Hand().getTiles().get(2).getRank(), is(3));
		assertThat(game.getPlayer2Hand().getTiles().get(3).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer2Hand().getTiles().get(3).getRank(), is(4));
		assertThat(game.getPlayer2Hand().getTiles().get(4).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer2Hand().getTiles().get(4).getRank(), is(5));
		assertThat(game.getPlayer2Hand().getTiles().get(5).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer2Hand().getTiles().get(5).getRank(), is(6));
		assertThat(game.getPlayer2Hand().getTiles().get(6).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer2Hand().getTiles().get(6).getRank(), is(7));
		assertThat(game.getPlayer2Hand().getTiles().get(7).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer2Hand().getTiles().get(7).getRank(), is(8));
		assertThat(game.getPlayer2Hand().getTiles().get(8).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer2Hand().getTiles().get(8).getRank(), is(9));
		assertThat(game.getPlayer2Hand().getTiles().get(9).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer2Hand().getTiles().get(9).getRank(), is(10));
		assertThat(game.getPlayer2Hand().getTiles().get(10).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer2Hand().getTiles().get(10).getRank(), is(11));
		assertThat(game.getPlayer2Hand().getTiles().get(11).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer2Hand().getTiles().get(11).getRank(), is(12));
		assertThat(game.getPlayer2Hand().getTiles().get(12).getColour(), is(Tile.Colours.RED));
		assertThat(game.getPlayer2Hand().getTiles().get(12).getRank(), is(13));
		assertThat(game.getPlayer2Hand().getTiles().get(13).getColour(), is(Tile.Colours.BLUE));
		assertThat(game.getPlayer2Hand().getTiles().get(13).getRank(), is(1));

		assertThat(game.getPlayer3Hand().getTiles().get(0).getColour(), is(Tile.Colours.BLUE));
		assertThat(game.getPlayer3Hand().getTiles().get(0).getRank(), is(2));
		assertThat(game.getPlayer3Hand().getTiles().get(1).getColour(), is(Tile.Colours.ORANGE));
		assertThat(game.getPlayer3Hand().getTiles().get(1).getRank(), is(1));
		assertThat(game.getPlayer3Hand().getTiles().get(2).getColour(), is(Tile.Colours.ORANGE));
		assertThat(game.getPlayer3Hand().getTiles().get(2).getRank(), is(2));
		assertThat(game.getPlayer3Hand().getTiles().get(3).getColour(), is(Tile.Colours.ORANGE));
		assertThat(game.getPlayer3Hand().getTiles().get(3).getRank(), is(3));
		assertThat(game.getPlayer3Hand().getTiles().get(4).getColour(), is(Tile.Colours.ORANGE));
		assertThat(game.getPlayer3Hand().getTiles().get(4).getRank(), is(4));
		assertThat(game.getPlayer3Hand().getTiles().get(5).getColour(), is(Tile.Colours.ORANGE));
		assertThat(game.getPlayer3Hand().getTiles().get(5).getRank(), is(5));
		assertThat(game.getPlayer3Hand().getTiles().get(6).getColour(), is(Tile.Colours.ORANGE));
		assertThat(game.getPlayer3Hand().getTiles().get(6).getRank(), is(6));
		assertThat(game.getPlayer3Hand().getTiles().get(7).getColour(), is(Tile.Colours.ORANGE));
		assertThat(game.getPlayer3Hand().getTiles().get(7).getRank(), is(7));
		assertThat(game.getPlayer3Hand().getTiles().get(8).getColour(), is(Tile.Colours.ORANGE));
		assertThat(game.getPlayer3Hand().getTiles().get(8).getRank(), is(8));
		assertThat(game.getPlayer3Hand().getTiles().get(9).getColour(), is(Tile.Colours.ORANGE));
		assertThat(game.getPlayer3Hand().getTiles().get(9).getRank(), is(9));
		assertThat(game.getPlayer3Hand().getTiles().get(10).getColour(), is(Tile.Colours.ORANGE));
		assertThat(game.getPlayer3Hand().getTiles().get(10).getRank(), is(10));
		assertThat(game.getPlayer3Hand().getTiles().get(11).getColour(), is(Tile.Colours.ORANGE));
		assertThat(game.getPlayer3Hand().getTiles().get(11).getRank(), is(11));
		assertThat(game.getPlayer3Hand().getTiles().get(12).getColour(), is(Tile.Colours.ORANGE));
		assertThat(game.getPlayer3Hand().getTiles().get(12).getRank(), is(12));
		assertThat(game.getPlayer3Hand().getTiles().get(13).getColour(), is(Tile.Colours.ORANGE));
		assertThat(game.getPlayer3Hand().getTiles().get(13).getRank(), is(13));

		assertThat(game.getPlayer4Hand().getTiles().get(0).getColour(), is(Tile.Colours.BLUE));
		assertThat(game.getPlayer4Hand().getTiles().get(0).getRank(), is(2));
		assertThat(game.getPlayer4Hand().getTiles().get(1).getColour(), is(Tile.Colours.GREEN));
		assertThat(game.getPlayer4Hand().getTiles().get(1).getRank(), is(1));
		assertThat(game.getPlayer4Hand().getTiles().get(2).getColour(), is(Tile.Colours.GREEN));
		assertThat(game.getPlayer4Hand().getTiles().get(2).getRank(), is(2));
		assertThat(game.getPlayer4Hand().getTiles().get(3).getColour(), is(Tile.Colours.GREEN));
		assertThat(game.getPlayer4Hand().getTiles().get(3).getRank(), is(3));
		assertThat(game.getPlayer4Hand().getTiles().get(4).getColour(), is(Tile.Colours.GREEN));
		assertThat(game.getPlayer4Hand().getTiles().get(4).getRank(), is(4));
		assertThat(game.getPlayer4Hand().getTiles().get(5).getColour(), is(Tile.Colours.GREEN));
		assertThat(game.getPlayer4Hand().getTiles().get(5).getRank(), is(5));
		assertThat(game.getPlayer4Hand().getTiles().get(6).getColour(), is(Tile.Colours.GREEN));
		assertThat(game.getPlayer4Hand().getTiles().get(6).getRank(), is(6));
		assertThat(game.getPlayer4Hand().getTiles().get(7).getColour(), is(Tile.Colours.GREEN));
		assertThat(game.getPlayer4Hand().getTiles().get(7).getRank(), is(7));
		assertThat(game.getPlayer4Hand().getTiles().get(8).getColour(), is(Tile.Colours.GREEN));
		assertThat(game.getPlayer4Hand().getTiles().get(8).getRank(), is(8));
		assertThat(game.getPlayer4Hand().getTiles().get(9).getColour(), is(Tile.Colours.GREEN));
		assertThat(game.getPlayer4Hand().getTiles().get(9).getRank(), is(9));
		assertThat(game.getPlayer4Hand().getTiles().get(10).getColour(), is(Tile.Colours.GREEN));
		assertThat(game.getPlayer4Hand().getTiles().get(10).getRank(), is(10));
		assertThat(game.getPlayer4Hand().getTiles().get(11).getColour(), is(Tile.Colours.GREEN));
		assertThat(game.getPlayer4Hand().getTiles().get(11).getRank(), is(11));
		assertThat(game.getPlayer4Hand().getTiles().get(12).getColour(), is(Tile.Colours.GREEN));
		assertThat(game.getPlayer4Hand().getTiles().get(12).getRank(), is(12));
		assertThat(game.getPlayer4Hand().getTiles().get(13).getColour(), is(Tile.Colours.GREEN));
		assertThat(game.getPlayer4Hand().getTiles().get(13).getRank(), is(13));
	}

	@Test
	public void testHandRigging2PlayersButUsing4PlayerRiggedFile() {
		int numPlayers = 2;
		String filePath = "src/main/resources/handRiggingExampleFiles/fourPlayerHandRigging";
		FileParser fileParser = new FileParser();
		assertThat(fileParser.isValidFile(filePath, numPlayers), is(false));
	}

	@Test
	public void testHandRigging3PlayersButUsing4PlayerRiggedFile() {
		int numPlayers = 3;
		String filePath = "src/main/resources/handRiggingExampleFiles/fourPlayerHandRigging";
		FileParser fileParser = new FileParser();
		assertThat(fileParser.isValidFile(filePath, numPlayers), is(false));
	}

	@Test
	public void testHandRigging4PlayersButUsing2PlayerRiggedFile() {
		int numPlayers = 4;
		String filePath = "src/main/resources/handRiggingExampleFiles/twoPlayerHandRigging";
		FileParser fileParser = new FileParser();
		assertThat(fileParser.isValidFile(filePath, numPlayers), is(false));
	}

	@Test
	public void testHandRigging4PlayersButUsing3PlayerRiggedFile() {
		int numPlayers = 4;
		String filePath = "src/main/resources/handRiggingExampleFiles/threePlayerHandRigging";
		FileParser fileParser = new FileParser();
		assertThat(fileParser.isValidFile(filePath, numPlayers), is(false));
	}
}
