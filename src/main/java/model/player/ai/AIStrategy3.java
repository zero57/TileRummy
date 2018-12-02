package model.player.ai;

import model.Game;
import model.Hand;

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