package model;

import model.observable.ObservableTile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

public class HandTest {
	private Hand hand;

	@BeforeEach
	public void setUp() {
		hand = new Hand();
	}

	@Test
	public void testHandAddAndRemove() {
		var t1 = new ObservableTile(7, Tile.Colours.GREEN);
		var t2 = new ObservableTile(12, Tile.Colours.ORANGE);
		var t3 = new ObservableTile(3, Tile.Colours.RED);
		var t4 = new ObservableTile(2, Tile.Colours.BLUE);
		var t5 = new ObservableTile(9, Tile.Colours.RED);
		var t6 = new ObservableTile(10, Tile.Colours.ORANGE);
		var t7 = new ObservableTile(5, Tile.Colours.GREEN);
		var t8 = new ObservableTile(11, Tile.Colours.GREEN);
		var t9 = new ObservableTile(6, Tile.Colours.RED);
		var t10 = new ObservableTile(1, Tile.Colours.BLUE);
		var t11 = new ObservableTile(4, Tile.Colours.ORANGE);
		var t12 = new ObservableTile(13, Tile.Colours.BLUE);
		var t13 = new ObservableTile(8, Tile.Colours.RED);
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
		assertThat(hand.getTiles(), contains(t3, t9, t13, t5, t10, t4, t12, t7, t1, t8, t11, t6, t2));
		hand.removeTile(t9);
		hand.removeTile(t4);
		hand.removeTile(t12);
		hand.removeTile(t2);
		assertThat(hand.getTiles(), contains(t3, t13, t5, t10, t7, t1, t8, t11, t6));
	}
}
