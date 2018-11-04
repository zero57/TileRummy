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
public class AIStrategy3 implements AIStrategy {

	private static final Logger logger = LogManager.getLogger(AIStrategy3.class.getName());
	private Hand hand;
	private final Game game;
	private boolean playedFirstHand;

	public AIStrategy3(Game game, Hand hand) {
		this.game = game;
		this.hand = hand;
		playedFirstHand = false;
	}

	@Override
	public void doSomething() {
		if (playedFirstHand) {
			regularStrategy();
		} else {
			firstHandStrategy();
		}
	}

	@Override
	public void firstHandStrategy() {
		int runTotal = 0;
		int setTotal = 0;
		ArrayList<Meld> runMelds = findRunsInHand(this.hand);
		ArrayList<Meld> setMelds = findSetsInHand(this.hand);

		if (!(runMelds.isEmpty())) {
			for (Meld meld : runMelds) {
				runTotal += meld.getValue();
			}
		} else if (!(setMelds.isEmpty())) {
			for (Meld meld : setMelds) {
				setTotal += meld.getValue();
			}
		} else {
			game.endTurn(this.hand);
			return;
		}

		if (runTotal > setTotal) {
			int fakeSetTotal = 0;
			Hand fakeHand = new Hand(this.hand);
			for (Meld meld : runMelds) {
				for (ObservableTile t : meld.getMeld()) {
					fakeHand.removeTile(t);
				}
			}
			ArrayList<Meld> fakeSetMelds = findSetsInHand(fakeHand);
			if (!(fakeSetMelds.isEmpty())) {
				for (Meld meld : fakeSetMelds) {
					fakeSetTotal += meld.getValue();
				}
			}
			if (runTotal + fakeSetTotal >= 30) {
				logger.debug("AIStrategy3 HAS A PLAYABLE FIRST HAND");

				for (Meld meld : runMelds) {
					logger.debug(meld);
				}
				for (Meld meld : fakeSetMelds) {
					logger.debug(meld);
				}

				// TODO: play the tiles to the board. The correct melds are stored in 
				// runMelds and fakeSetMelds. Must correctly remove from this.hand and 
				// show on GUI somehow.

				playedFirstHand = true;
				game.endTurn(this.hand);
				return;
			} else {
				game.endTurn(this.hand);
				return;
			}
		} else {
			int fakeRunTotal = 0;
			Hand fakeHand = new Hand(this.hand);
			for (Meld meld : setMelds) {
				for (ObservableTile t : meld.getMeld()) {
					fakeHand.removeTile(t);
				}
			}
			ArrayList<Meld> fakeRunMelds = findRunsInHand(fakeHand);
			if (!(fakeRunMelds.isEmpty())) {
				for (Meld meld : fakeRunMelds) {
					fakeRunTotal += meld.getValue();
				}
			}
			if (setTotal + fakeRunTotal >= 30) {
				logger.debug("AIStrategy3 HAS A PLAYABLE FIRST HAND");

				for (Meld meld : setMelds) {
					logger.debug(meld);
				}
				for (Meld meld : fakeRunMelds) {
					logger.debug(meld);
				}

				// TODO: play the tiles to the board. The correct melds are stored in 
				// setMelds and fakeRunMelds. Must correctly remove from this.hand and 
				// show on GUI somehow.

				playedFirstHand = true;
				game.endTurn(this.hand);
				return;
			} else {
				game.endTurn(this.hand);
				return;
			}
		}
	}

	@Override
	public void regularStrategy() {
		logger.debug("TODO: AIStrategy3 regularStrategy()");
		game.endTurn(this.hand);
		return;
	}

	public ArrayList<Meld> findRunsInHand(Hand hand) {
		ArrayList melds = new ArrayList<Meld>();
		Meld meld;

		FXCollections.sort(hand.getTiles());
		for (int i = 0; i < hand.getTiles().size() - 2; i++) {
			if ((hand.getTiles().get(i+1).getRank() == hand.getTiles().get(i).getRank()+1) && 
			(hand.getTiles().get(i+2).getRank() == hand.getTiles().get(i+1).getRank()+1)) {

				meld = new Meld();
				meld.addLastTile(hand.getTiles().get(i));
				meld.addLastTile(hand.getTiles().get(i+1));
				meld.addLastTile(hand.getTiles().get(i+2));
				i+=2;
				while ((i < hand.getTiles().size() - 1) && (hand.getTiles().get(i+1).getRank() == hand.getTiles().get(i).getRank()+1)) {
					meld.addLastTile(hand.getTiles().get(i+1));
					i++;
				}

				melds.add(meld);
			}
		}

		return melds;	
	}

	public ArrayList<Meld> findSetsInHand(Hand hand) {
		ArrayList melds = new ArrayList<Meld>();
		Meld meld;

		hand.getTiles().sort((ObservableTile t1, ObservableTile t2)->Integer.compare(t1.getRank(), t2.getRank()));

		for (int i = 0; i < hand.getTiles().size() - 2; i++) {
			if ((hand.getTiles().get(i+1).getRank() == hand.getTiles().get(i).getRank()) && 
			(hand.getTiles().get(i+2).getRank() == hand.getTiles().get(i+1).getRank())) {
				if ((hand.getTiles().get(i+1).getColour() != hand.getTiles().get(i).getColour()) &&
					(hand.getTiles().get(i+1).getColour() != hand.getTiles().get(i+2).getColour()) &&
					(hand.getTiles().get(i).getColour() != hand.getTiles().get(i+2).getColour())) {
						meld = new Meld();
						meld.addLastTile(hand.getTiles().get(i));
						meld.addLastTile(hand.getTiles().get(i+1));
						meld.addLastTile(hand.getTiles().get(i+2));

						if ((i < hand.getTiles().size() - 1)) {
							if (meld.isValidLastTile(hand.getTiles().get(i+1))) {
								meld.addLastTile(hand.getTiles().get(i+1));
								i++;
							}
						}

						melds.add(meld);
				}
			}
		}
		return melds;
	}
}