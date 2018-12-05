package controller;

import factory.TileButtonFactory;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import model.Game;
import model.Hand;
import model.HandRigTable;
import model.observable.ObservableMeld;
import model.observable.ObservableTile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ui.TileButton;
import ui.UIHelper;

import java.text.MessageFormat;
import java.util.Optional;

public class HandRigTableController {

	private static final Logger logger = LogManager.getLogger(HandRigTableController.class.getName());

	@FXML
	private GridPane gpTable;

	private final TileButtonFactory tileButtonFactory;
	private final ObservableList<ObservableList<ObservableTile>> table;
	private final HandRigTable handRigTable;
	private Hand player1Hand;
	private Hand player2Hand;
	private Hand player3Hand;
	private Hand player4Hand;

	public HandRigTableController(HandRigTable handRigTable, Hand player1Hand, Hand player2Hand, Hand player3Hand, Hand player4Hand) {
		this.handRigTable = handRigTable;
		this.tileButtonFactory = new TileButtonFactory();
		this.table = handRigTable.getTable();
		this.player1Hand = player1Hand;
		this.player2Hand = player2Hand;
		this.player3Hand = player3Hand;
		this.player4Hand = player4Hand;
	}

	@FXML
	public void initialize() {
		logger.info("Initializing Table");
		PseudoClass mouseDragEnter = PseudoClass.getPseudoClass("mouse-drag-enter");
		gpTable.getChildren().forEach(item -> {
			item.addEventFilter(MouseDragEvent.MOUSE_DRAG_ENTERED, e -> item.pseudoClassStateChanged(mouseDragEnter, true));
			item.addEventFilter(MouseDragEvent.MOUSE_DRAG_EXITED, e -> item.pseudoClassStateChanged(mouseDragEnter, false));
			item.addEventFilter(MouseDragEvent.MOUSE_DRAG_RELEASED, e -> {
				int row = GridPane.getRowIndex(item);
				int col = GridPane.getColumnIndex(item);
				logger.debug("On MouseDragRelease " + row + " " + col);
				TileButton btn = (TileButton) e.getGestureSource();
				ObservableTile tile = btn.getTile();
				if (!btn.getOriginatesFromTable()) {
					// Click and drag tile from hand to table
					if (getCellFromGridPane(row, col).orElseThrow().getChildren().size() == 0) {
						handRigTable.addTileToTable(tile, row, col);
						// todo: this is bad, and relies heavily on the fact that we actually pass references of ObservableTile around
						player1Hand.removeTile(tile);
						player2Hand.removeTile(tile);
						player3Hand.removeTile(tile);
						player4Hand.removeTile(tile);
					}
				}
			});
		});
		table.forEach(row -> {
			row.addListener((ListChangeListener<ObservableTile>) change -> updateTable());
		});
		updateTable();
	}

	private void updateTable() {
		clearTable();
		for (int row = 0; row < table.size(); row++) {
			for (int col = 0; col < table.get(row).size(); col++) {
				var tile = table.get(row).get(col);
				var ignoreRootNode = getCellFromGridPane(row, col).orElseThrow(); // We throw, but this should never happen as there will always be an HBox in the GridCell
				var btn = (TileButton) UIHelper.makeDraggable(tileButtonFactory.newTileButton(tile, false, row, col), ignoreRootNode);
				addOrReplaceNodeAtCell(btn, row, col);
			}
		}
	}

	private void clearTable() {
		for (int row = 0; row < gpTable.getRowCount(); row++) {
			for (int col = 0; col < gpTable.getColumnCount(); col++) {
				getCellFromGridPane(row, col).ifPresent(hbox -> {
					hbox.getChildren().clear();
					// Must set back to false since when we drag tiles, the root pane stops listening for mouse events
					// The call for root.setMouseTransparent(false) in the UIHelper's mouse released event does not fire
					// since we remove the node as soon as we release the mouse
					hbox.setMouseTransparent(false);
				});
			}
		}
	}

	private void addOrReplaceNodeAtCell(Node node, int row, int col) {
		getCellFromGridPane(row, col).ifPresent(cell -> {
			if (cell.getChildren().size() > 0) {
				cell.getChildren().setAll(node);
			} else {
				cell.getChildren().add(node);
			}
		});
	}

	private Optional<HBox> getCellFromGridPane(int row, int col) {
		for (Node node : gpTable.getChildren()) {
			if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col) {
				return Optional.of((HBox) node);
			}
		}
		return Optional.empty();
	}
}
