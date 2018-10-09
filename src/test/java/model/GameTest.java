package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

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
}
