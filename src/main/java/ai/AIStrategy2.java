package ai;

import model.Meld;
import model.observable.ObservableTile;
import model.Game;
import model.Hand;

import java.util.ArrayList;

import javafx.collections.FXCollections;

import java.util.Comparator;
import java.lang.Integer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AIStrategy2 extends AIStrategy {

	public AIStrategy2(Game game, Hand hand) {
		super(game, hand);
	}

	@Override
	boolean firstHandStrategy() {
		if (game.getTable().size() == 0) {
			logger.debug("NO MELDS ON TABLE. AIStrategy2 WON'T PLAY FIRST HAND");
			return false;
		}
		boolean success = attemptToPlayFirstHand();
		if (success) {
			logger.debug("AIStrategy2 PLAYED ITS FIRST HAND");
		}
		return success;
	}

	@Override
	void regularStrategy() {
		logger.debug("TODO: AIStrategy2 regularStrategy()");
	}
}