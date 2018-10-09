package model;

import java.util.List;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class MeldTest {
	private Meld meld;

	@BeforeEach
	public void setUp() {
		meld = new Meld();
	}
	
	@Test
	public void testMeldAddFirst() {
		Tile t1 = new Tile(5,Tile.Colours.RED);
		Tile t2 = new Tile(4,Tile.Colours.RED);
		Tile t3 = new Tile(3,Tile.Colours.RED);
		meld.addFirstTile(t1);
		meld.addFirstTile(t2);
		meld.addFirstTile(t3);
		assertThat(meld.getMeld(),contains(t3,t2,t1));//last tile added should be first tile in meld
	}
	
	@Test
	public void testMeldAddLast() {
		Tile t1 = new Tile(10,Tile.Colours.RED);
		Tile t2 = new Tile(10,Tile.Colours.GREEN);
		Tile t3 = new Tile(10,Tile.Colours.ORANGE);
		meld.addLastTile(t1);
		meld.addLastTile(t2);
		meld.addLastTile(t3);
		assertThat(meld.getMeld(),contains(t1,t2,t3));
	}
	
	@Test
	public void testMeldAddArbitrary() {
		Tile t1 = new Tile(1,Tile.Colours.BLUE);
		Tile t2 = new Tile(2,Tile.Colours.BLUE);
		Tile t3 = new Tile(3,Tile.Colours.BLUE);
		Tile t4 = new Tile(4,Tile.Colours.BLUE);
		Tile t5 = new Tile(5,Tile.Colours.BLUE);
		meld.addFirstTile(t1);
		meld.addFirstTile(t2);
		meld.addLastTile(t3);
		meld.addLastTile(t4);
		meld.addFirstTile(t5);
		assertThat(meld.getMeld(),contains(t5,t2,t1,t3,t4));
	}
	
	@Test
	public void testMeldRemove() {
		Tile t1 = new Tile(1,Tile.Colours.RED);
		Tile t2 = new Tile(2,Tile.Colours.GREEN);
		Tile t3 = new Tile(3,Tile.Colours.ORANGE);
		Tile t4 = new Tile(4,Tile.Colours.BLUE);
		Tile t5 = new Tile(5,Tile.Colours.BLUE);
		meld.addLastTile(t1);
		meld.addLastTile(t2);
		meld.addLastTile(t3);
		meld.addLastTile(t4);
		meld.addLastTile(t5);
		
		meld.removeTile(t4);
		assertThat(meld.getMeld(),contains(t1,t2,t3,t5));
		meld.removeTile(t1);
		assertThat(meld.getMeld(),contains(t2,t3,t5));
		meld.removeTile(t3);
		assertThat(meld.getMeld(),contains(t2,t5));
		meld.removeTile(t5);
		assertThat(meld.getMeld(),contains(t2));
		meld.removeTile(t2);
		assertThat(meld.getMeld(),is(empty()));
	}
	
}
