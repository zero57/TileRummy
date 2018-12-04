package controller;

import factory.TileButtonFactory;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import model.Game;
import model.Hand;
import model.observable.ObservableTile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ui.UIHelper;

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
	protected boolean shouldShow;

	public HandController(Game game, int playerNumber, boolean shouldShow) {
		this.game = game;
		this.playerNumber = playerNumber;
		this.shouldShow = shouldShow;
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

	protected void updateHand(boolean hide) {
		fpHand.getChildren().clear();
		// Must set back to false since when we drag tiles, the root pane stops listening for mouse events
		// The call for root.setMouseTransparent(false) in the UIHelper's mouse released event does not fire
		// since we remove the node as soon as we release the mouse
		root.setMouseTransparent(false);

		for (ObservableTile t : hand.getTiles()) {
			Node btn;
			if (shouldShow) {
				btn = UIHelper.makeDraggable(tileButtonFactory.newTileButton(t, false), root);
			} else {
				btn = UIHelper.makeDraggable(tileButtonFactory.newTileButton(t, hide), root);
			}
			btn.disableProperty().bind(game.getPlayerTurnProperty().isNotEqualTo(playerNumber - 1).or(game.getWinnerProperty().isNotEqualTo(-1)));
			fpHand.getChildren().add(btn);
		}
	}
}
