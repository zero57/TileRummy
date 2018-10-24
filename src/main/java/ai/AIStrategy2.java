package ai;
import model.Game;
import model.Hand;

public class AIStrategy2 implements AIStrategy {

	private Hand hand;
	private final Game game;
	private boolean playedFirstHand;

	public AIStrategy2(Game game, Hand hand) {
		this.game = game;
		this.hand = hand;
		playedFirstHand = false;
	}

	@Override
	public void doSomething() {
		System.out.println("henlo uwu -- AIStrategy2: TURN IS 2");
	}

	@Override
	public void firstHandStrategy() {}

	@Override
	public void regularStrategy() {}
}