package ai;
import model.Meld;
import model.Tile;
import model.Game;
import model.Hand;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import java.util.Comparator;
import java.lang.Integer;

public class AIStrategy1 implements AIStrategy {

	private Hand hand;
	private final Game game;
	private boolean playedFirstHand;

	public AIStrategy1(Game game, Hand hand) {
		this.game = game;
		this.hand = hand;
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
		//ArrayList<Meld> melds = findRunsInHand();
		ArrayList<Meld> melds = findSetsInHand();

		if (melds.isEmpty()) {
			game.drawTurn(hand);
		} else {
			for (Meld meld : melds) {
				System.out.println("SET DETECTED");
				for (Tile t : meld.getMeld()) {
					System.out.println(t);
				}
			}
		}
	}

	public ArrayList<Meld> findRunsInHand() {
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

	public ArrayList<Meld> findSetsInHand() {
		ArrayList melds = new ArrayList<Meld>();
		Meld meld;

		hand.getTiles().sort((Tile t1, Tile t2)->Integer.compare(t1.getRank(), t2.getRank()));

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

	@Override
	public void regularStrategy() {
		//System.out.println("playedFirstHand is " + playedFirstHand);
	}
}