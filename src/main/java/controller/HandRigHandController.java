package controller;

import factory.TileButtonFactory;
import javafx.collections.ListChangeListener;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import model.Hand;
import model.HandRigTable;
import model.observable.ObservableTile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ui.TileButton;
import ui.UIHelper;

public class HandRigHandController {
	private static final Logger logger = LogManager.getLogger(HandRigHandController.class.getName());

	@FXML
	private VBox root;

	@FXML
	private Label lblPlayerNumber;

	@FXML
	private FlowPane fpHand;

	private final Hand hand;
	private final int playerNumber;
	private final TileButtonFactory tileButtonFactory;
	private final HandRigTable handRigTable;

	public HandRigHandController(HandRigTable handRigTable, int playerNumber, Hand hand) {
		this.handRigTable = handRigTable;
		this.playerNumber = playerNumber;
		this.tileButtonFactory = new TileButtonFactory();
		this.hand = hand;
	}

	@FXML
	public void initialize() {
		logger.info("Initializing Hand for Player " + playerNumber);
		PseudoClass mouseDragEnter = PseudoClass.getPseudoClass("mouse-drag-enter");

		switch (playerNumber) {
			case 1:
				lblPlayerNumber.setText("Player 1");
				fpHand.setMinHeight(32);
				break;
			case 2:
				lblPlayerNumber.setText("Player 2");
				fpHand.setOrientation(Orientation.VERTICAL);
				fpHand.setMinWidth(32);
				break;
			case 3:
				lblPlayerNumber.setText("Player 3");
				fpHand.setMinHeight(32);
				break;
			case 4:
				lblPlayerNumber.setText("Player 4");
				fpHand.setOrientation(Orientation.VERTICAL);
				fpHand.setMinWidth(32);
				break;
			default:
				logger.error("Can't create a HandRigHandController for Player " + playerNumber);
				return;
		}
		hand.getTiles().addListener((ListChangeListener<ObservableTile>) change -> updateHand());
		fpHand.addEventFilter(MouseDragEvent.MOUSE_DRAG_ENTERED, e -> fpHand.pseudoClassStateChanged(mouseDragEnter, true));
		fpHand.addEventFilter(MouseDragEvent.MOUSE_DRAG_EXITED, e -> fpHand.pseudoClassStateChanged(mouseDragEnter, false));
		fpHand.addEventFilter(MouseDragEvent.MOUSE_DRAG_RELEASED, e -> {
			TileButton btn = (TileButton) e.getGestureSource();
			ObservableTile tile = btn.getTile();
			if (btn.getOriginatesFromTable()) {
				if (hand.getSizeProperty().getValue() < 14) {
					handRigTable.removeTileFromTable(btn.getRow(), btn.getCol());
					hand.addTile(tile);
				}
			}
		});
	}

	public Pane getHandPane() {
		return fpHand;
	}

	protected void updateHand() {
		fpHand.getChildren().clear();
		// Must set back to false since when we drag tiles, the root pane stops listening for mouse events
		// The call for root.setMouseTransparent(false) in the UIHelper's mouse released event does not fire
		// since we remove the node as soon as we release the mouse
		root.setMouseTransparent(false);

		for (ObservableTile t : hand.getTiles()) {
			Node btn;
			btn = UIHelper.makeDraggable(tileButtonFactory.newTileButton(t, false), root);
			fpHand.getChildren().add(btn);
		}
	}
}
