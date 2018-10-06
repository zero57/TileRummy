package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;

public class StockTest {
	private Stock stock;

	@BeforeEach
	public void setUp() {
		stock = new Stock();
	}

	@Test
	public void testStockCreation() {
		assertThat(stock.getStock(), hasSize(104));
	}

	@Test
	public void testStockShuffle() {
		List<Tile> unshuffledTiles = new ArrayList<>(stock.getStock());
		// Check that unshuffled cards is the same as the base deck
		assertThat(stock.getStock(), contains(unshuffledTiles.toArray()));
		stock.shuffle();
		// Check that the unshuffled cards/base deck is NOT the same after being shuffled.
		assertThat(stock.getStock(), containsInAnyOrder(unshuffledTiles.toArray()));
	}

	@Test
	public void testStockDraw() {
		Tile tile = stock.draw();
		assertThat(stock.getStock(), hasSize(103));
		assertThat(stock.getStock(), not(hasItem(tile)));
	}

	@Test
	public void testStockDoubleDraw() {
		Tile tile1 = stock.draw();
		Tile tile2 = stock.draw();
		assertThat(tile1, is(not(tile2)));
	}
}