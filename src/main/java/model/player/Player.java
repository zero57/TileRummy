package model.player;

import model.Game;
import model.Hand;

public abstract class Player {
	protected Game game;
	protected Hand hand;
	protected int score;

	public Player(Game game, Hand hand) {
		this.game = game;
		this.hand = hand;
		score = 0;
	}

	public abstract void playTurn();

	public Hand getHand() {
		return hand;
	}

	public int getScore() {
		return score;
	}

	public void addScore(int points) {
		score += points;
	}
}
