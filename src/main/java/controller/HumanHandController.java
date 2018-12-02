package controller;

import factory.TileButtonFactory;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import model.Game;
import model.Hand;
import model.observable.ObservableTile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ui.TileButton;
import ui.UIHelper;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import javafx.animation.Animation;

import java.text.MessageFormat;

public class HumanHandController extends HandController {
	private static final Logger logger = LogManager.getLogger(HumanHandController.class.getName());

	@FXML
	private VBox root;

	@FXML
	private Label lblPlayerNumber;

	@FXML
	private FlowPane fpHand;

	private Timeline timer;
	private boolean shouldTime;

	public HumanHandController(Game game, int playerNumber, boolean shouldTime) {
		super(game, playerNumber);
		this.shouldTime = shouldTime;
	}

	@FXML
	public void initialize() {
		logger.info("Initializing Hand for Player " + playerNumber);
		PseudoClass currentTurn = PseudoClass.getPseudoClass("current-turn");
		PseudoClass mouseDragEnter = PseudoClass.getPseudoClass("mouse-drag-enter");

		if (shouldTime) {
			timer = new Timeline(new KeyFrame(
			Duration.millis(120000),
			ae -> System.out.println("IVE BEEN TRIGGERED"))); // TODO: end turn and return to valid board state if turn is not valid
		}

		switch (playerNumber) {
			case 1:
				game.getPlayerTurnProperty().addListener((observableValue, oldVal, newVal) -> {
					if (newVal.equals(0)) {
						lblPlayerNumber.pseudoClassStateChanged(currentTurn, true);
						if (shouldTime) timer.play();
					} else {
						lblPlayerNumber.pseudoClassStateChanged(currentTurn, false);
						if (shouldTime) timer.stop();
					}
				});
				lblPlayerNumber.setText("Player 1");
				lblPlayerNumber.pseudoClassStateChanged(currentTurn, true);
				break;
			case 2:
				game.getPlayerTurnProperty().addListener((observableValue, oldVal, newVal) -> {
					if (newVal.equals(1)) {
						lblPlayerNumber.pseudoClassStateChanged(currentTurn, true);
						if (shouldTime) timer.play();
					} else {
						lblPlayerNumber.pseudoClassStateChanged(currentTurn, false);
						if (shouldTime) timer.stop();
					}
				});
				lblPlayerNumber.setText("Player 2");
				fpHand.setOrientation(Orientation.VERTICAL);
				break;
			case 3:
				game.getPlayerTurnProperty().addListener((observableValue, oldVal, newVal) -> {
					if (newVal.equals(2)) {
						lblPlayerNumber.pseudoClassStateChanged(currentTurn, true);
						if (shouldTime) timer.play();
					} else {
						lblPlayerNumber.pseudoClassStateChanged(currentTurn, false);
						if (shouldTime) timer.stop();
					}
				});
				lblPlayerNumber.setText("Player 3");
				break;
			case 4:
				game.getPlayerTurnProperty().addListener((observableValue, oldVal, newVal) -> {
					if (newVal.equals(3)) {
						lblPlayerNumber.pseudoClassStateChanged(currentTurn, true);
						if (shouldTime) timer.play();
					} else {
						lblPlayerNumber.pseudoClassStateChanged(currentTurn, false);
						if (shouldTime) timer.stop();
					}
				});
				lblPlayerNumber.setText("Player 4");
				fpHand.setOrientation(Orientation.VERTICAL);
				break;
			default:
				logger.error("Can't create a HandController for Player " + playerNumber);
				return;
		}
		if (playerNumber == 1 && shouldTime) timer.play();
		hand.getTiles().addListener(onTileListChange());
		fpHand.addEventFilter(MouseDragEvent.MOUSE_DRAG_ENTERED, e -> {
			boolean isMyTurn = game.getPlayerTurn() == (playerNumber - 1);
			if (isMyTurn) {
				fpHand.pseudoClassStateChanged(mouseDragEnter, true);
			}
		});
		fpHand.addEventFilter(MouseDragEvent.MOUSE_DRAG_EXITED, e -> {
			boolean isMyTurn = game.getPlayerTurn() == (playerNumber - 1);
			if (isMyTurn) {
				fpHand.pseudoClassStateChanged(mouseDragEnter, false);
			}
		});
		fpHand.addEventFilter(MouseDragEvent.MOUSE_DRAG_RELEASED, e -> {
			TileButton btn = (TileButton) e.getGestureSource();
			ObservableTile tile = btn.getTile();
			boolean isMyTurn = game.getPlayerTurn() == (playerNumber - 1);
			if (btn.getOriginatesFromTable() && !tile.hasBeenPlayed() && isMyTurn && game.removeTileFromTable(tile, btn.getRow(), btn.getCol())) {
				hand.addTile(tile);
			}
		});
		game.getPlayerTurnProperty().addListener((observableValue, oldVal, newVal) -> {
			boolean isMyTurn = game.getPlayerTurn() == (playerNumber - 1);
			updateHand(!isMyTurn);
		});
	}

	private void updateHand(boolean hide) {
		fpHand.getChildren().clear();
		// Must set back to false since when we drag tiles, the root pane stops listening for mouse events
		// The call for root.setMouseTransparent(false) in the UIHelper's mouse released event does not fire
		// since we remove the node as soon as we release the mouse
		root.setMouseTransparent(false);

		for (ObservableTile t : hand.getTiles()) {
			var btn = UIHelper.makeDraggable(tileButtonFactory.newTileButton(t, hide), root);
			btn.disableProperty().bind(game.getPlayerTurnProperty().isNotEqualTo(playerNumber - 1));
			fpHand.getChildren().add(btn);
		}
	}

	private ListChangeListener<ObservableTile> onTileListChange() {
		return (ListChangeListener.Change<? extends ObservableTile> change) -> {
			boolean isMyTurn = game.getPlayerTurn() == (playerNumber - 1);
			updateHand(!isMyTurn);
			while (change.next()) {
				if (change.wasAdded()) {
					logger.debug(MessageFormat.format("Adding {0} to Player {1}s hand", change.getAddedSubList().toString(), playerNumber));
				} else if (change.wasRemoved()) {
					logger.debug(MessageFormat.format("Removing {0} in Player {1}s hand", change.getRemoved().toString(), playerNumber));
				}
			}
		};
	}

	public Pane getHandPane() {
		return fpHand;
	}
}
