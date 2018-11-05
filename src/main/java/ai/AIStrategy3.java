package ai;

import model.Game;
import model.Hand;
import org.apache.logging.log4j.Logger;

public class AIStrategy3 extends AIStrategy {

	public AIStrategy3(Game game, Hand hand) {
		super(game, hand);
	}

	@Override
	boolean firstHandStrategy() {
		boolean success = attemptToPlayFirstHand();
		if (success) {
			logger.debug("AIStrategy3 PLAYED ITS FIRST HAND");
		}
		return success;
	}

	@Override
	void regularStrategy() {
		boolean success = attemptRegularStrategy();
		if (success) {
			logger.debug("AIStrategy3 PLAYED A HAND");
		}
	}
}