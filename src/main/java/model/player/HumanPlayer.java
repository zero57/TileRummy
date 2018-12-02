package model.player;

import model.Game;
import model.Hand;

public class HumanPlayer extends Player {

	public HumanPlayer(Game game, Hand hand) {
		super(game, hand);
	}

	@Override
	public void playTurn() {
		// Do nothing. Since human player will interact using the UI
	}
}
