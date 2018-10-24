package ai;

import java.util.Arrays;
import java.util.ArrayList;
import model.Meld;
import model.Hand;
import model.Tile;
import model.Game;

import ai.AIStrategy1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.contains;

public class AIStrategy1Test {

	private AIStrategy1 ai1;
	private Hand hand;
	private ArrayList<Meld> melds;

	@BeforeEach
	public void setUp() {
		hand = new Hand();
		hand.addTile(new Tile(3, Tile.Colours.RED));
		hand.addTile(new Tile(5, Tile.Colours.RED));
		hand.addTile(new Tile(4, Tile.Colours.RED));
		hand.addTile(new Tile(3, Tile.Colours.BLUE));
		hand.addTile(new Tile(3, Tile.Colours.GREEN));
		melds = new ArrayList<Meld>();
		ai1 = new AIStrategy1(new Game(), hand);
	}

	@Test
	public void testFindRunsInHand() {
		melds = ai1.findRunsInHand();
	}
}