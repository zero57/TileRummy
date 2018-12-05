package model;

import model.observable.ObservableTile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

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
		assertThat(stock.getStock(), hasSize(106));
	}

	@Test
	public void testStockShuffle() {
		var unshuffledTiles = new ArrayList<>(stock.getStock());
		// Check that unshuffled cards is the same as the base deck
		assertThat(stock.getStock(), contains(unshuffledTiles.toArray()));
		stock.shuffle();
		// Check that the unshuffled cards/base deck is NOT the same after being shuffled.
		assertThat(stock.getStock(), containsInAnyOrder(unshuffledTiles.toArray()));
	}

	@Test
	public void testStockDraw() {
		ObservableTile tile = stock.draw().orElseThrow();
		assertThat(stock.getStock(), hasSize(105));
		assertThat(stock.getStock(), not(hasItem(tile)));
	}

	@Test
	public void testStockDoubleDraw() {
		ObservableTile tile1 = stock.draw().orElseThrow();
		ObservableTile tile2 = stock.draw().orElseThrow();
		assertThat(tile1, is(not(tile2)));
		assertThat(stock.getSize(), is(104));
	}

	@Test
	public void testEmptyStock() {
		assertThat(stock.getSize(), is(106));
		for (int i = stock.getSize(); i > 0; i--) {
			stock.draw();
		}
		assertThat(stock.getSize(), is(0));
		// Should not throw or cause an error
		stock.draw();
		assertThat(stock.getSize(), is(0));
	}
}