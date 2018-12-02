package model.player.ai;

import model.Game;
import model.Hand;
import model.Meld;
import model.Tile;
import model.observable.ObservableTile;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

public class attemptToAddAllTilesToTableTest {
	private AIPlayer ai;
	private Hand hand;
	@Test
	public void testAttemptToPlayAllTiles() {
		Game game = new Game();
		hand = new Hand();
		ai = new AIPlayer(2,game, hand);
		ArrayList<Meld> melds = new ArrayList<Meld>();

		Meld meld1 = new Meld();
		meld1.addLastTile(new ObservableTile(1, Tile.Colours.RED));
		meld1.addLastTile(new ObservableTile(1, Tile.Colours.GREEN));
		meld1.addLastTile(new ObservableTile(1, Tile.Colours.BLUE));
		meld1.addLastTile(new ObservableTile(1, Tile.Colours.ORANGE));

		Meld meld2 = new Meld();
		meld2.addLastTile(new ObservableTile(1, Tile.Colours.RED));
		meld2.addLastTile(new ObservableTile(2, Tile.Colours.RED));
		meld2.addLastTile(new ObservableTile(3, Tile.Colours.RED));
		meld2.addLastTile(new ObservableTile(4, Tile.Colours.RED));

		Meld meld3 = new Meld();
		meld3.addLastTile(new ObservableTile(6, Tile.Colours.BLUE));
		meld3.addLastTile(new ObservableTile(7, Tile.Colours.BLUE));
		meld3.addLastTile(new ObservableTile(8, Tile.Colours.BLUE));
		meld3.addLastTile(new ObservableTile(9, Tile.Colours.BLUE));

		Meld meld4 = new Meld();
		meld4.addLastTile(new ObservableTile(6, Tile.Colours.ORANGE));
		meld4.addLastTile(new ObservableTile(7, Tile.Colours.ORANGE));
		meld4.addLastTile(new ObservableTile(8, Tile.Colours.ORANGE));
		meld4.addLastTile(new ObservableTile(9, Tile.Colours.ORANGE));

		melds.add(meld1);
		melds.add(meld2);
		melds.add(meld3);
		melds.add(meld4);
		ai.getAiStrategy().playEverythingToTable(melds);
		game.getTable().forEach(meld -> meld.getMeld().forEach(ObservableTile::play));

		hand.addTile(new ObservableTile(3, Tile.Colours.RED));
		hand.addTile(new ObservableTile(5, Tile.Colours.RED));
		hand.addTile(new ObservableTile(2, Tile.Colours.BLUE));
		hand.addTile(new ObservableTile(3, Tile.Colours.BLUE));
		hand.addTile(new ObservableTile(9, Tile.Colours.GREEN));

		assertTrue(ai.getAiStrategy().attemptToPlayAllTiles());

		game.getTable().clear();
		ai.getAiStrategy().playEverythingToTable(melds);
		game.getTable().forEach(meld -> meld.getMeld().forEach(ObservableTile::play));
		hand.addTile(new ObservableTile(3, Tile.Colours.RED));
		hand.addTile(new ObservableTile(5, Tile.Colours.RED));
		//hand.addTile(new ObservableTile(2, Tile.Colours.BLUE));
		hand.addTile(new ObservableTile(3, Tile.Colours.BLUE));
		hand.addTile(new ObservableTile(9, Tile.Colours.GREEN));

		assertFalse(ai.getAiStrategy().attemptToPlayAllTiles());
	}

}
