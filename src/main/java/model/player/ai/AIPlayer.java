package model.player.ai;

import model.Game;
import model.Hand;
import model.player.Player;

public class AIPlayer extends Player {
	private AIStrategy aiStrategy;
	private boolean playedFirstHand;

	AIStrategy getAiStrategy() {
		return aiStrategy;
	}

	public AIPlayer(int stratNum, Game game, Hand hand) {
		super(game, hand);
		switch (stratNum) {
			case 1:
				aiStrategy = new AIStrategy1(game, hand);
				break;
			case 2:
				aiStrategy = new AIStrategy2(game, hand);
				break;
			case 3:
				aiStrategy = new AIStrategy3(game, hand);
				break;
		}
	}

	@Override
	public void playTurn() {
		if (playedFirstHand) {
			performRegularStrategy();
		} else {
			playedFirstHand = performFirstHandStrategy();
		}
		game.endTurn(hand);
	}

	void performRegularStrategy() {
		aiStrategy.regularStrategy();
	}

	boolean performFirstHandStrategy() {
		return aiStrategy.firstHandStrategy();
	}
}
