package controller;

import factory.TileButtonFactory;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import model.Game;
import model.Hand;
import model.Tile;
import model.observable.ObservableTile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ui.TileButton;
import ui.UIHelper;

import java.text.MessageFormat;

public class HandController {
	private static final Logger logger = LogManager.getLogger(HandController.class.getName());

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

	public HandController(Game game, int playerNumber) {
		this.game = game;
		this.playerNumber = playerNumber;
		this.tileButtonFactory = new TileButtonFactory();
		switch (playerNumber) {
			case 1:
				this.hand = game.getPlayer1Hand();
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
		PseudoClass mouseDragEnter = PseudoClass.getPseudoClass("mouse-drag-enter");
		Platform.runLater(() -> lblPlayerNumber.pseudoClassStateChanged(currentTurn, true));
		switch (playerNumber) {
			case 1:
				game.getPlayerTurnProperty().addListener((observableValue, oldVal, newVal) -> {
					if (newVal.equals(0)) {
						Platform.runLater(() -> lblPlayerNumber.pseudoClassStateChanged(currentTurn, true));
					} else {
						Platform.runLater(() -> lblPlayerNumber.pseudoClassStateChanged(currentTurn, false));
					}
				});
				Platform.runLater(() -> lblPlayerNumber.setText("Player 1"));
				break;
			default:
				logger.error("Can't create a HandController for Player " + playerNumber);
				return;
		}
		hand.getTiles().addListener(onTileListChange());
		fpHand.addEventFilter(MouseDragEvent.MOUSE_DRAG_ENTERED, e -> Platform.runLater(() -> fpHand.pseudoClassStateChanged(mouseDragEnter, true)));
		fpHand.addEventFilter(MouseDragEvent.MOUSE_DRAG_EXITED, e -> Platform.runLater(() -> fpHand.pseudoClassStateChanged(mouseDragEnter, false)));
		fpHand.addEventFilter(MouseDragEvent.MOUSE_DRAG_RELEASED, e -> {
			TileButton btn = (TileButton) e.getGestureSource();
			Tile tile = btn.getTile();
			if (btn.getOriginatesFromTable() && !((ObservableTile) tile).hasBeenPlayed() && game.removeTileFromTable(tile, btn.getRow(), btn.getCol())) {
				hand.addTile(tile);
			}
		});
	}

	private ListChangeListener<Tile> onTileListChange() {
		return (ListChangeListener.Change<? extends Tile> change) -> {
			while (change.next()) {
				if (change.wasAdded()) {
					logger.debug(MessageFormat.format("Adding {0} to Player {1}s hand", change.getAddedSubList().toString(), playerNumber));
					for (Tile t : change.getAddedSubList()) {
						var btn = UIHelper.makeDraggable(tileButtonFactory.newTileButton(t, false), root);
						btn.disableProperty().bind(game.getNPCTurn());
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
