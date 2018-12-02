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
		boolean success = playLiberal();
		if (success) {
			logger.debug("AIStrategy1 PLAYED A HAND");
		}
	}

}