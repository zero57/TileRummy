package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class MeldTest {
	private Meld meld;

	@BeforeEach
	public void setUp() {
		meld = new Meld();
	}

	@Test
	public void testRunAddFirst() {
		Tile t1 = new Tile(5, Tile.Colours.RED);
		Tile t2 = new Tile(4, Tile.Colours.RED);
		Tile t3 = new Tile(3, Tile.Colours.RED);
		meld.addFirstTile(t1);
		meld.addFirstTile(t2);
		meld.addFirstTile(t3);
		assertThat(meld.getMeld(), contains(t3, t2, t1));//last tile added should be first tile in meld
		//Now attempting to add invalid tiles
		meld.addFirstTile(new Tile(2, Tile.Colours.BLUE));
		meld.addFirstTile(new Tile(3, Tile.Colours.RED));
		meld.addFirstTile(new Tile(1, Tile.Colours.RED));
		meld.addFirstTile(new Tile(7, Tile.Colours.RED));
		meld.addFirstTile(new Tile(3, Tile.Colours.GREEN));
		meld.addFirstTile(new Tile(4, Tile.Colours.GREEN));
		meld.addFirstTile(new Tile(5, Tile.Colours.GREEN));
		assertThat(meld.getMeld(), contains(t3, t2, t1));
	}

	@Test
	public void testRunAddLast() {
		Tile t1 = new Tile(3, Tile.Colours.RED);
		Tile t2 = new Tile(4, Tile.Colours.RED);
		Tile t3 = new Tile(5, Tile.Colours.RED);
		meld.addLastTile(t1);
		meld.addLastTile(t2);
		meld.addLastTile(t3);
		assertThat(meld.getMeld(), contains(t1, t2, t3));
		//Now attempting to add invalid tiles
		meld.addFirstTile(new Tile(6, Tile.Colours.BLUE));
		meld.addFirstTile(new Tile(5, Tile.Colours.RED));
		meld.addFirstTile(new Tile(7, Tile.Colours.RED));
		meld.addFirstTile(new Tile(1, Tile.Colours.RED));
		meld.addFirstTile(new Tile(3, Tile.Colours.GREEN));
		meld.addFirstTile(new Tile(4, Tile.Colours.GREEN));
		meld.addFirstTile(new Tile(5, Tile.Colours.GREEN));
		assertThat(meld.getMeld(), contains(t1, t2, t3));
	}

	@Test
	public void testSetAddFirst() {
		Tile t1 = new Tile(10, Tile.Colours.RED);
		Tile t2 = new Tile(10, Tile.Colours.GREEN);
		Tile t3 = new Tile(10, Tile.Colours.ORANGE);
		meld.addFirstTile(t1);
		meld.addFirstTile(t2);
		meld.addFirstTile(t3);
		assertThat(meld.getMeld(), contains(t3, t2, t1));
		//Now attempting to add invalid tiles
		meld.addFirstTile(new Tile(6, Tile.Colours.BLUE));
		meld.addFirstTile(new Tile(13, Tile.Colours.BLUE));
		meld.addFirstTile(new Tile(10, Tile.Colours.RED));
		meld.addFirstTile(new Tile(10, Tile.Colours.GREEN));
		meld.addFirstTile(new Tile(9, Tile.Colours.RED));
		assertThat(meld.getMeld(), contains(t3, t2, t1));
	}

	@Test
	public void testSetAddLast() {
		Tile t1 = new Tile(10, Tile.Colours.RED);
		Tile t2 = new Tile(10, Tile.Colours.GREEN);
		Tile t3 = new Tile(10, Tile.Colours.ORANGE);
		meld.addLastTile(t1);
		meld.addLastTile(t2);
		meld.addLastTile(t3);
		assertThat(meld.getMeld(), contains(t1, t2, t3));
		//Now attempting to add invalid tiles
		meld.addLastTile(new Tile(6, Tile.Colours.BLUE));
		meld.addLastTile(new Tile(13, Tile.Colours.BLUE));
		meld.addLastTile(new Tile(10, Tile.Colours.RED));
		meld.addLastTile(new Tile(10, Tile.Colours.GREEN));
		meld.addLastTile(new Tile(11, Tile.Colours.ORANGE));
		assertThat(meld.getMeld(), contains(t1, t2, t3));
	}

	@Test
	public void testMeldRemove() {
		Tile t1 = new Tile(4, Tile.Colours.RED);
		Tile t2 = new Tile(3, Tile.Colours.RED);
		Tile t3 = new Tile(5, Tile.Colours.RED);
		Tile t4 = new Tile(6, Tile.Colours.RED);
		Tile t5 = new Tile(7, Tile.Colours.RED);
		meld.addLastTile(t1);
		meld.addFirstTile(t2);
		meld.addLastTile(t3);
		meld.removeFirstTile();
		meld.addLastTile(t4);
		meld.addLastTile(t5);
		meld.removeLastTile();
		assertThat(meld.getMeld(), contains(t1, t3, t4));
		meld.removeLastTile();
		meld.removeLastTile();
		meld.removeLastTile();
		assertThat(meld.getMeld(), is(empty()));
		meld.removeLastTile();
		meld.removeFirstTile();
		assertThat(meld.getMeld(), is(empty()));
	}

	@Test
	public void testRunToSet() {
		assertEquals(meld.getType(), Meld.Types.UNDETERMINED);
		//create a run
		Tile t1 = new Tile(3, Tile.Colours.RED);
		meld.addLastTile(t1);
		assertEquals(meld.getType(), Meld.Types.UNDETERMINED);
		meld.addLastTile(new Tile(4, Tile.Colours.RED));
		assertEquals(meld.getType(), Meld.Types.RUN);
		meld.addLastTile(new Tile(5, Tile.Colours.RED));
		assertEquals(meld.getType(), Meld.Types.RUN);
		//remove all cards but one
		meld.removeLastTile();
		assertEquals(meld.getType(), Meld.Types.RUN);
		meld.removeLastTile();
		assertEquals(meld.getType(), Meld.Types.UNDETERMINED);
		//create a set
		Tile t2 = new Tile(3, Tile.Colours.ORANGE);
		Tile t3 = new Tile(3, Tile.Colours.GREEN);
		meld.addLastTile(t2);
		assertEquals(meld.getType(), Meld.Types.SET);
		meld.addLastTile(t3);
		assertEquals(meld.getType(), Meld.Types.SET);
		assertThat(meld.getMeld(), contains(t1, t2, t3));
		//shouldn't behave as a run
		meld.addFirstTile(new Tile(2, Tile.Colours.RED));
		meld.addLastTile(new Tile(4, Tile.Colours.GREEN));
		assertThat(meld.getMeld(), contains(t1, t2, t3));
	}

	@Test
	public void testSetToRun() {
		assertEquals(meld.getType(), Meld.Types.UNDETERMINED);
		//create a set
		Tile t1 = new Tile(3, Tile.Colours.RED);
		meld.addLastTile(t1);
		assertEquals(meld.getType(), Meld.Types.UNDETERMINED);
		meld.addLastTile(new Tile(3, Tile.Colours.ORANGE));
		assertEquals(meld.getType(), Meld.Types.SET);
		meld.addLastTile(new Tile(3, Tile.Colours.GREEN));
		assertEquals(meld.getType(), Meld.Types.SET);
		//remove all cards but one
		meld.removeLastTile();
		assertEquals(meld.getType(), Meld.Types.SET);
		meld.removeLastTile();
		assertEquals(meld.getType(), Meld.Types.UNDETERMINED);
		//create a run
		Tile t2 = new Tile(2, Tile.Colours.RED);
		Tile t3 = new Tile(4, Tile.Colours.RED);
		meld.addFirstTile(t2);
		assertEquals(meld.getType(), Meld.Types.RUN);
		meld.addLastTile(t3);
		assertEquals(meld.getType(), Meld.Types.RUN);
		assertThat(meld.getMeld(), contains(t2, t1, t3));
		//shouldn't behave as a set
		meld.addFirstTile(new Tile(2, Tile.Colours.GREEN));
		meld.addFirstTile(new Tile(3, Tile.Colours.GREEN));
		meld.addFirstTile(new Tile(4, Tile.Colours.GREEN));
		meld.addLastTile(new Tile(2, Tile.Colours.GREEN));
		meld.addLastTile(new Tile(3, Tile.Colours.GREEN));
		meld.addLastTile(new Tile(4, Tile.Colours.GREEN));
		assertThat(meld.getMeld(), contains(t2, t1, t3));
	}

	@Test
	public void testValue() {
		assertEquals(0, meld.getValue());
		meld.addLastTile(new Tile(4, Tile.Colours.RED));
		assertEquals(4, meld.getValue());
		meld.addLastTile(new Tile(5, Tile.Colours.RED));
		assertEquals(9, meld.getValue());
		meld.addFirstTile(new Tile(3, Tile.Colours.RED));
		assertEquals(12, meld.getValue());
		meld.addLastTile(new Tile(7, Tile.Colours.GREEN));
		assertEquals(12, meld.getValue());
		meld.addFirstTile(new Tile(1, Tile.Colours.ORANGE));
		assertEquals(12, meld.getValue());
		meld.removeLastTile();
		assertEquals(7, meld.getValue());
		meld.removeFirstTile();
		assertEquals(4, meld.getValue());
		meld.removeLastTile();
		assertEquals(0, meld.getValue());
		meld.removeLastTile();
		assertEquals(0, meld.getValue());
	}

	@Test
	public void testValid() {
		assertFalse(meld.isValidLength());
		//now add cards
		meld.addLastTile(new Tile(4, Tile.Colours.RED));
		assertFalse(meld.isValidLength());
		meld.addLastTile(new Tile(5, Tile.Colours.RED));
		assertFalse(meld.isValidLength());
		meld.addFirstTile(new Tile(2, Tile.Colours.RED));
		assertFalse(meld.isValidLength());
		meld.addLastTile(new Tile(2, Tile.Colours.RED));
		assertFalse(meld.isValidLength());
		meld.addFirstTile(new Tile(3, Tile.Colours.RED));
		assertTrue(meld.isValidLength());
		meld.addFirstTile(new Tile(2, Tile.Colours.RED));
		assertTrue(meld.isValidLength());
		meld.removeLastTile();
		assertTrue(meld.isValidLength());
		meld.removeFirstTile();
		assertFalse(meld.isValidLength());
		meld.removeLastTile();
		assertFalse(meld.isValidLength());
	}
}
