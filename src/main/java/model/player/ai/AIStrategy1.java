package model.player.ai;

import model.Game;
import model.Hand;

public class AIStrategy1 extends AIStrategy {

	public AIStrategy1(Game game, Hand hand) {
		super(game, hand);
	}

	@Override
	boolean firstHandStrategy() {
		boolean success = attemptToPlayFirstHand();
		if (success) {
			logger.debug("AIStrategy1 PLAYED ITS FIRST HAND");
		}
		return success;
	}

	@Override
	void regularStrategy() {
		if(attemptToPlayAllTiles()){
			logger.debug("AIStrategy1 PLAYED ALL ITS TILES");
			return;
		}
		if(playLiberal()){
			logger.debug("AIStrategy1 PLAYED A HAND");
			return;
		}
		logger.debug("AIStrategy1 DRAWS A TILE");
	}

}