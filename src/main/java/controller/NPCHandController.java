package controller;

import factory.TileButtonFactory;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import model.Game;
import model.Hand;
import model.Tile;
import model.observable.ObservableTile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ui.TileButton;

import java.text.MessageFormat;

public class NPCHandController {
	private static final Logger logger = LogManager.getLogger(NPCHandController.class.getName());

	@FXML
	private VBox root;

	@FXML
	private Label lblPlayerNumber;

	@FXML
	private FlowPane fpHand;

	private final Game game;
	private final Hand hand;
	private final int playerNumber;
	private final TileButtonFactory tileButtonFactory;

	public NPCHandController(Game game, int playerNumber) {
		this.game = game;
		this.playerNumber = playerNumber;
		this.tileButtonFactory = new TileButtonFactory();
		switch (playerNumber) {
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

	@FXML
	public void initialize() {
		logger.info("Initializing Hand for Player " + playerNumber);
		PseudoClass currentTurn = PseudoClass.getPseudoClass("current-turn");
		switch (playerNumber) {
			case 2:
				game.getPlayerTurnProperty().addListener((observableValue, oldVal, newVal) -> {
					if (newVal.equals(1)) {
						Platform.runLater(() -> lblPlayerNumber.pseudoClassStateChanged(currentTurn, true));
					} else {
						Platform.runLater(() -> lblPlayerNumber.pseudoClassStateChanged(currentTurn, false));
					}
				});
				Platform.runLater(() -> {
					lblPlayerNumber.setText("Player 2");
					fpHand.setOrientation(Orientation.VERTICAL);
				});
				break;
			case 3:
				game.getPlayerTurnProperty().addListener((observableValue, oldVal, newVal) -> {
					if (newVal.equals(2)) {
						Platform.runLater(() -> lblPlayerNumber.pseudoClassStateChanged(currentTurn, true));
					} else {
						Platform.runLater(() -> lblPlayerNumber.pseudoClassStateChanged(currentTurn, false));
					}
				});
				Platform.runLater(() -> lblPlayerNumber.setText("Player 3"));
				break;
			case 4:
				game.getPlayerTurnProperty().addListener((observableValue, oldVal, newVal) -> {
					if (newVal.equals(3)) {
						Platform.runLater(() -> lblPlayerNumber.pseudoClassStateChanged(currentTurn, true));
					} else {
						Platform.runLater(() -> lblPlayerNumber.pseudoClassStateChanged(currentTurn, false));
					}
				});
				Platform.runLater(() -> {
					lblPlayerNumber.setText("Player 4");
					fpHand.setOrientation(Orientation.VERTICAL);
				});
				break;
			default:
				logger.error("Can't create a NPCHandController for Player " + playerNumber);
				return;
		}
		hand.getTiles().addListener(onTileListChange());
	}

	private ListChangeListener<ObservableTile> onTileListChange() {
		return (ListChangeListener.Change<? extends ObservableTile> change) -> {
			while (change.next()) {
				if (change.wasAdded()) {
					logger.debug(MessageFormat.format("Adding {0} to Player {1}s hand", change.getAddedSubList().toString(), playerNumber));
					for (ObservableTile t : change.getAddedSubList()) {
						var btn = tileButtonFactory.newTileButton(t, true);
						Platform.runLater(() -> fpHand.getChildren().add(change.getFrom(), btn));
					}
				} else if (change.wasRemoved()) {
					logger.debug(MessageFormat.format("Removing {0} in Player {1}s hand", change.getRemoved().toString(), playerNumber));
					change.getRemoved().forEach(tile -> {
						fpHand.getChildren().stream()
								.filter(b -> ((TileButton) b).getTile().equals(tile))
								.findAny()
								.ifPresent(btn -> Platform.runLater(() -> fpHand.getChildren().remove(btn)));
					});
				}
			}
		};
	}

}
