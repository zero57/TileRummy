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
		logger.debug("TODO: AIStrategy3 regularStrategy()");
	}
}