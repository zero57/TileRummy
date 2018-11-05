package ai;

import model.Meld;
import model.observable.ObservableTile;
import model.Game;
import model.observable.ObservableMeld;
import model.Hand;

import java.util.List;
import java.util.ArrayList;

import javafx.collections.FXCollections;

import java.util.Comparator;
import java.lang.Integer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

	void regularStrategy() {
		boolean success = attemptRegularStrategy();
		if (success) {
			logger.debug("AIStrategy1 PLAYED A HAND");
		}
	}

}