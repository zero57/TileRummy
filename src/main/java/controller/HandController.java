package controller;

import factory.TileButtonFactory;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import model.Game;
import model.Hand;
import model.Tile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.MessageFormat;

public class HandController {
	private static final Logger logger = LogManager.getLogger(HandController.class.getName());

	@FXML
	private Label lblPlayerNumber;

	@FXML
	private FlowPane fpHand;

	private final Game game;
	private final Hand hand;
	private final int playerNumber;
	private TileButtonFactory tileButtonFactory;

	public HandController(Game game, int playerNumber) {
		this.game = game;
		this.playerNumber = playerNumber;
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
				return;
		}
		tileButtonFactory = new TileButtonFactory();
	}

	@FXML
	public void initialize() {
		logger.info("Initializing Hand for Player " + playerNumber);
		switch (playerNumber) {
			case 1:
				lblPlayerNumber.setText("Player 1");
				break;
			case 2:
				lblPlayerNumber.setText("Player 2");
				fpHand.setOrientation(Orientation.VERTICAL);
				break;
			case 3:
				lblPlayerNumber.setText("Player 3");
				break;
			case 4:
				lblPlayerNumber.setText("Player 4");
				fpHand.setOrientation(Orientation.VERTICAL);
				break;
		}
		hand.getTiles().addListener(onTileListChange());
	}

	public ListChangeListener<Tile> onTileListChange() {
		return (ListChangeListener.Change<? extends Tile> change) -> {
			while (change.next()) {
				if (change.wasAdded()) {
					logger.debug(MessageFormat.format("Adding {0} to Player {1}s hand", change.getAddedSubList().toString(), playerNumber));
					for (Tile t : change.getAddedSubList()) {
						var btn = tileButtonFactory.newTileButton(t.getRank(), t.getColour());
						fpHand.getChildren().add(btn);
					}
				} else if (change.wasRemoved()) {
					logger.debug(MessageFormat.format("Removing {0} in Player {1}s hand", change.getRemoved().toString(), playerNumber));
				}
			}
		};
	}

}
