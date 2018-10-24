package model;

import java.util.Arrays;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.contains;

public class TileTest {
	private ObservableList<Tile> tiles;
	private Tile t1;
	private Tile t2;
	private Tile t3;
	private Tile t4;
	private Tile t5;

	@BeforeEach
	public void setUp() {
		t1 = new Tile(3, Tile.Colours.RED);
		t2 = new Tile(4, Tile.Colours.RED);
		t3 = new Tile(5, Tile.Colours.ORANGE);
		t4 = new Tile(3, Tile.Colours.BLUE);
		t5 = new Tile(5, Tile.Colours.GREEN);
	}

	@Test
	public void testComparableSameColourLowerToHigher() {
		assertThat(t1.compareTo(t2), is(-1));
	}

	@Test
	public void testComparableSameColourHigherToLower() {
		assertThat(t2.compareTo(t1), is(1));
	}

	@Test
	public void testComparableSameRankDifferentColours() {
		assertThat(t1.compareTo(t4), is(-1));
		assertThat(t3.compareTo(t5), is(1));
	}

	@Test
	public void testComparableDifferentRankDifferentColours() {
		assertThat(t1.compareTo(t5), is(-1));
		assertThat(t5.compareTo(t3), is(-1));
		assertThat(t3.compareTo(t2), is(1));
	}

	@Test
	public void testSortWithTileComparable() {
		tiles = FXCollections.observableArrayList();
		tiles.add(t1);
		tiles.add(t2);
		tiles.add(t3);
		tiles.add(t4);
		tiles.add(t5);

		FXCollections.sort(tiles);

		assertThat(tiles, contains(Arrays.asList(equalTo(t1), 
												 equalTo(t2),
												 equalTo(t4),
												 equalTo(t5),
												 equalTo(t3))));
	}
}
