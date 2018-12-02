package controller;

import factory.TileButtonFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import model.Game;
import model.Hand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HandController {
	private static final Logger logger = LogManager.getLogger(HandController.class.getName());

	@FXML
	private VBox root;

	@FXML
	private Label lblPlayerNumber;

	@FXML
	private FlowPane fpHand;

	protected final Game game;
	protected final Hand hand;
	protected final int playerNumber;
	protected final TileButtonFactory tileButtonFactory;

	public HandController(Game game, int playerNumber) {
		this.game = game;
		this.playerNumber = playerNumber;
		this.tileButtonFactory = new TileButtonFactory();
		switch (playerNumber) {
			case 1:
				this.hand = game.getPlayer1Hand();
				break;
			case 2:
				this.hand = game.getPlayer2Hand();
				break;
			case 3:
				this.hand = game.getPlayer3Hand();
				break;
			case 4:
				this.hand = game.getPlayer4Hand();
				break;
			default:
				this.hand = null;
				logger.error("Invalid player number: " + playerNumber);
		}
	}

	public Pane getHandPane() {
		return fpHand;
	}
}
