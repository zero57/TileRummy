package model;

import model.observable.ObservableTile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
		assertThat(game.getStockSize(), is(104));
		game.dealInitialTiles();
		assertThat(game.getStockSize(), is(48));
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
}
