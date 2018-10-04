package model;

import java.util.List;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.hasItem;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

public class StockTest {

	@Test
	public void testStockCreation() {
		Stock stock = new Stock();
		assertThat(stock.getStock(), hasSize(104));
	}

	@Test
    public void testStockShuffle() {
    	Stock stock = new Stock();
        List<Tile> unshuffledTiles = new ArrayList<>(stock.getStock());
        // Check that unshuffled cards is the same as the base deck
        assertThat(stock.getStock(), contains(unshuffledTiles.toArray()));
        stock.shuffle();
        // Check that the unshuffled cards/base deck is NOT the same after being shuffled.
        assertThat(stock.getStock(), containsInAnyOrder(unshuffledTiles.toArray()));
    }

    @Test 
    public void testStockDraw() {
    	Stock stock = new Stock();
    	Tile tile = stock.draw();
    	assertThat(stock.getStock(), hasSize(103));
    	assertThat(stock.getStock(), not(hasItem(tile)));
    }

    @Test
    public void testStockDoubleDraw() {
    	Stock stock = new Stock();
    	Tile tile1 = stock.draw();
    	Tile tile2 = stock.draw();
    	assertThat(tile1, is(not(tile2)));
    }
}