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
		if(attemptToPlayAllTiles()){
			logger.debug("AIStrategy3 PLAYED ALL ITS TILES");
			return;
		}
		int lowLimit = hand.getTiles().size()-3;
		if(game.getPlayer1Hand().getTiles().size()<=lowLimit
		   || game.getPlayer2Hand().getTiles().size()<=lowLimit
		   || game.getPlayer3Hand().getTiles().size()<=lowLimit){
			if(playLiberal()){
				logger.debug("AIStrategy3 PLAYED A HAND");
				return;
			}
		}else{
			if(playConservative()){
				logger.debug("AIStrategy3 PLAYED A HAND");
				return;
			}
		}
		logger.debug("AIStrategy3 DRAWS A TILE");
	}
}