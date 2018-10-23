package model;

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
	}

	@Test
	public void testDealInitialTiles() {
		assertThat(game.getStockSize(), is(104));
		game.dealInitialTiles();
		assertThat(game.getStockSize(), is(48));
	}

	@Test
	public void testAddTileToTableAllAtBackOfRunMeld() {
		Tile t1 = new Tile(1, Tile.Colours.RED);
		Tile t2 = new Tile(2, Tile.Colours.RED);
		Tile t3 = new Tile(3, Tile.Colours.RED);
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
		Tile t1 = new Tile(1, Tile.Colours.RED);
		Tile t2 = new Tile(2, Tile.Colours.RED);
		Tile t3 = new Tile(3, Tile.Colours.RED);
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
		Tile t1 = new Tile(1, Tile.Colours.RED);
		Tile t2 = new Tile(2, Tile.Colours.RED);
		Tile t3 = new Tile(3, Tile.Colours.RED);
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
		Tile t1 = new Tile(1, Tile.Colours.RED);
		Tile t2 = new Tile(2, Tile.Colours.RED);
		Tile t3 = new Tile(3, Tile.Colours.RED);
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
		Tile t1 = new Tile(3, Tile.Colours.RED);
		Tile t2 = new Tile(3, Tile.Colours.ORANGE);
		Tile t3 = new Tile(3, Tile.Colours.BLUE);
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
		Tile t1 = new Tile(3, Tile.Colours.RED);
		Tile t2 = new Tile(3, Tile.Colours.ORANGE);
		Tile t3 = new Tile(3, Tile.Colours.BLUE);
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
		Tile t1 = new Tile(3, Tile.Colours.RED);
		Tile t2 = new Tile(3, Tile.Colours.ORANGE);
		Tile t3 = new Tile(3, Tile.Colours.BLUE);
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
		Tile t1 = new Tile(3, Tile.Colours.RED);
		Tile t2 = new Tile(3, Tile.Colours.ORANGE);
		Tile t3 = new Tile(3, Tile.Colours.BLUE);
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
		Tile t1 = new Tile(1, Tile.Colours.RED);
		Tile t2 = new Tile(2, Tile.Colours.RED);
		Tile t3 = new Tile(3, Tile.Colours.RED);
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
		Tile t1 = new Tile(1, Tile.Colours.RED);
		Tile t2 = new Tile(2, Tile.Colours.RED);
		Tile t3 = new Tile(3, Tile.Colours.RED);
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
		Tile t1 = new Tile(1, Tile.Colours.RED);
		Tile t2 = new Tile(2, Tile.Colours.RED);
		Tile t3 = new Tile(3, Tile.Colours.RED);
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
		Tile t1 = new Tile(3, Tile.Colours.RED);
		Tile t2 = new Tile(3, Tile.Colours.RED);
		Tile t3 = new Tile(3, Tile.Colours.RED);
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
		Tile t1 = new Tile(1, Tile.Colours.RED);
		Tile t2 = new Tile(2, Tile.Colours.RED);
		Tile t3 = new Tile(3, Tile.Colours.RED);
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
		Tile t1 = new Tile(1, Tile.Colours.RED);
		Tile t2 = new Tile(2, Tile.Colours.RED);
		Tile t3 = new Tile(3, Tile.Colours.RED);
		game.addTileToTable(t1, 0, 1);
		game.addTileToTable(t2, 0, 2);
		game.addTileToTable(t3, 0, 3);

		// Attempt to add to end of meld
		game.addTileToTable(new Tile(5, Tile.Colours.RED), 0, 4);
		game.addTileToTable(new Tile(5, Tile.Colours.BLUE), 0, 4);
		game.addTileToTable(new Tile(5, Tile.Colours.GREEN), 0, 4);
		game.addTileToTable(new Tile(5, Tile.Colours.ORANGE), 0, 4);

		// Attempt to add to start of meld
		game.addTileToTable(new Tile(5, Tile.Colours.RED), 0, 0);
		game.addTileToTable(new Tile(5, Tile.Colours.BLUE), 0, 0);
		game.addTileToTable(new Tile(5, Tile.Colours.GREEN), 0, 0);
		game.addTileToTable(new Tile(5, Tile.Colours.ORANGE), 0, 0);

		assertThat(game.getTable().size(), is(1));
		assertThat(game.getTable().get(0).getRow(), is(0));
		assertThat(game.getTable().get(0).getCol(), is(1));
		assertThat(game.getTable().get(0).getSize(), is(3));
		assertThat(game.getTable().get(0).getType(), is(Meld.Types.RUN));
		assertThat(game.getTable().get(0).getMeld(), contains(Arrays.asList(equalTo(t1), equalTo(t2), equalTo(t3))));
	}

	@Test
	public void testCannotAddInvalidTilesToSetMeldOnTable() {
		Tile t1 = new Tile(3, Tile.Colours.RED);
		Tile t2 = new Tile(3, Tile.Colours.ORANGE);
		Tile t3 = new Tile(3, Tile.Colours.BLUE);
		game.addTileToTable(t1, 0, 1);
		game.addTileToTable(t2, 0, 2);
		game.addTileToTable(t3, 0, 3);

		// Attempt to add to end of meld
		game.addTileToTable(new Tile(4, Tile.Colours.RED), 0, 0);
		game.addTileToTable(new Tile(4, Tile.Colours.BLUE), 0, 4);
		game.addTileToTable(new Tile(4, Tile.Colours.GREEN), 0, 4);
		game.addTileToTable(new Tile(4, Tile.Colours.ORANGE), 0, 4);

		// Attempt to add to start of meld
		game.addTileToTable(new Tile(4, Tile.Colours.RED), 0, 0);
		game.addTileToTable(new Tile(4, Tile.Colours.BLUE), 0, 0);
		game.addTileToTable(new Tile(4, Tile.Colours.GREEN), 0, 0);
		game.addTileToTable(new Tile(4, Tile.Colours.ORANGE), 0, 0);

		assertThat(game.getTable().size(), is(1));
		assertThat(game.getTable().get(0).getRow(), is(0));
		assertThat(game.getTable().get(0).getCol(), is(1));
		assertThat(game.getTable().get(0).getSize(), is(3));
		assertThat(game.getTable().get(0).getType(), is(Meld.Types.SET));
		assertThat(game.getTable().get(0).getMeld(), contains(Arrays.asList(equalTo(t1), equalTo(t2), equalTo(t3))));
	}

	@Test
	public void testCannotAddMeldInBetweenTwoMeldsOnTable() {
		Tile t1 = new Tile(1, Tile.Colours.RED);
		Tile t2 = new Tile(2, Tile.Colours.RED);
		Tile t3 = new Tile(3, Tile.Colours.RED);
		game.addTileToTable(t1, 0, 0);
		game.addTileToTable(t2, 0, 1);
		game.addTileToTable(t3, 0, 2);

		Tile t4 = new Tile(4, Tile.Colours.RED);
		Tile t5 = new Tile(4, Tile.Colours.BLUE);
		Tile t6 = new Tile(4, Tile.Colours.GREEN);
		game.addTileToTable(t4, 0, 4);
		game.addTileToTable(t5, 0, 5);
		game.addTileToTable(t6, 0, 6);

		game.addTileToTable(new Tile(4, Tile.Colours.RED), 0, 3);

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
		Tile t1 = new Tile(1, Tile.Colours.RED);
		Tile t2 = new Tile(2, Tile.Colours.RED);
		Tile t3 = new Tile(3, Tile.Colours.RED);
		Tile t4 = new Tile(4, Tile.Colours.RED);
		game.addTileToTable(t1, 0, 0);
		game.addTileToTable(t2, 0, 1);
		game.addTileToTable(t3, 0, 2);
		game.addTileToTable(t4, 0, 3);

		Tile t5 = new Tile(4, Tile.Colours.ORANGE);
		Tile t6 = new Tile(4, Tile.Colours.BLUE);
		Tile t7 = new Tile(4, Tile.Colours.GREEN);
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
