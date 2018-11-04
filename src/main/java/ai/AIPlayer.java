package ai;

import model.Game;
import model.Hand;

public class AIPlayer {
	private AIStrategy aiStrategy;
	private boolean playedFirstHand;
	private Game game;
	private Hand hand;


	AIStrategy getAiStrategy() {
		return aiStrategy;
	}

	boolean isPlayedFirstHand() {
		return playedFirstHand;
	}

	Game getGame() {
		return game;
	}

	Hand getHand() {
		return hand;
	}

	public AIPlayer(int stratNum, Game game, Hand hand) {
		this.game = game;
		this.hand = hand;
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
