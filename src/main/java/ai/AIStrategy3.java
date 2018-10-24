package ai;
import model.Game;
import model.Hand;

public class AIStrategy3 implements AIStrategy {

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
		System.out.println("baka oniisama!! -- AIStrategy3: TURN IS 3");
	}

	@Override
	public void firstHandStrategy() {}

	@Override
	public void regularStrategy() {}
}