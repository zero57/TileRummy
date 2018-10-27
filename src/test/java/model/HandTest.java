package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

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
		Tile t1 = new Tile(7, Tile.Colours.GREEN);
		Tile t2 = new Tile(12, Tile.Colours.ORANGE);
		Tile t3 = new Tile(3, Tile.Colours.RED);
		Tile t4 = new Tile(2, Tile.Colours.BLUE);
		Tile t5 = new Tile(9, Tile.Colours.RED);
		Tile t6 = new Tile(10, Tile.Colours.ORANGE);
		Tile t7 = new Tile(5, Tile.Colours.GREEN);
		Tile t8 = new Tile(11, Tile.Colours.GREEN);
		Tile t9 = new Tile(6, Tile.Colours.RED);
		Tile t10 = new Tile(1, Tile.Colours.BLUE);
		Tile t11 = new Tile(4, Tile.Colours.ORANGE);
		Tile t12 = new Tile(13, Tile.Colours.BLUE);
		Tile t13 = new Tile(8, Tile.Colours.RED);
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
