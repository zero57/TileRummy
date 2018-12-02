package model.player;

import model.Game;
import model.Hand;

public abstract class Player {
	protected Game game;
	protected Hand hand;

	public Player(Game game, Hand hand) {
		this.game = game;
		this.hand = hand;
	}

	public abstract void playTurn();

	public Hand getHand() {
		return hand;
	}
}
