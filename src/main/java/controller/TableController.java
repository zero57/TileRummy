package controller;

import factory.TileButtonFactory;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import model.Game;
import model.Tile;
import model.observable.ObservableMeld;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ui.TileButton;
import ui.UIHelper;

import java.text.MessageFormat;
import java.util.Optional;

public class TableController {

	private static final Logger logger = LogManager.getLogger(TableController.class.getName());

	@FXML
	private GridPane gpTable;

	private final Game game;
	private final TileButtonFactory tileButtonFactory;
	private final ObservableList<ObservableMeld> table;

	public TableController(Game game) {
		this.game = game;
		this.tileButtonFactory = new TileButtonFactory();
		this.table = game.getTable();
	}

	@FXML
	public void initialize() {
		logger.info("Initializing Table");
		gpTable.getChildren().forEach(item -> {
			item.addEventFilter(MouseDragEvent.MOUSE_DRAG_ENTERED, e -> item.getStyleClass().setAll("grid-cell-on-drag-enter"));
			item.addEventFilter(MouseDragEvent.MOUSE_DRAG_EXITED, e -> item.getStyleClass().setAll("grid-cell-on-drag-exit"));
			item.addEventFilter(MouseDragEvent.MOUSE_DRAG_RELEASED, e -> {
				int row = GridPane.getRowIndex(item);
				int col = GridPane.getColumnIndex(item);
				logger.debug("On MouseDragRelease " + row + " " + col);
				TileButton btn = (TileButton) e.getGestureSource();
				Tile tile = btn.getTile();
				// Click and drag tile from table to table
				if (btn.getOriginatesFromTable()) {
					if (game.removeTileFromTable(tile, btn.getRow(), btn.getCol())) {
						if (!game.addTileToTable(tile, row, col)) {
							// add it back if you can't add it to the spot
							game.addTileToTable(tile, btn.getRow(), btn.getCol());
						}
					}
				} else {
					// Click and drag tile from hand to table
					if (game.addTileToTable(tile, row, col)) {
						game.getPlayer1Hand().removeTile(tile);
					}
				}
			});
		});
		table.addListener(onTableMeldListChange());
	}

	private ListChangeListener<ObservableMeld> onTableMeldListChange() {
		return (ListChangeListener.Change<? extends ObservableMeld> change) -> {
			while (change.next()) {
				if (change.wasAdded()) {
					change.getAddedSubList().forEach(meld -> {
						logger.debug(MessageFormat.format("Adding meld {0} to table at row:{1} col:{2} with length: {3}", meld, meld.getRow() + 1, meld.getCol() + 1, meld.getSize()));
						updateTable();
						meld.getMeld().addListener((ListChangeListener<Tile>) change1 -> {
							while (change1.next()) {
								if (change1.wasAdded()) {
									logger.debug(MessageFormat.format("Adding {0} to meld {1}", change1.getAddedSubList().toString(), meld.toString()));
								} else if (change1.wasRemoved()) {
									logger.debug(MessageFormat.format("Removing {0} in meld {1}", change1.getRemoved().toString(), meld.toString()));
								}
							}
							updateTable();
						});
					});
				}
			}
		};
	}

	private void updateTable() {
		Platform.runLater(() -> {
			clearTable();
			table.forEach(this::addMeldToTable);
		});
	}

	private void clearTable() {
		// Note, when calling this function, you should wrap it with Platform.runLater. It is not done here as having
		// too many runnables can cause issues such as flickering
		for (int row = 0; row < gpTable.getRowCount(); row++) {
			for (int col = 0; col < gpTable.getColumnCount(); col++) {
				getCellFromGridPane(row, col).ifPresent(hbox -> hbox.getChildren().clear());
			}
		}
	}

	private void addMeldToTable(ObservableMeld meld) {
		// Note, when calling this function, you should wrap it with Platform.runLater. It is not done here as having
		// too many runnables can cause issues such as flickering
		for (int i = 0; i < meld.getSize(); i++) {
			// Ignore the cell containing the tile
			var ignoreRootNode = getCellFromGridPane(meld.getRow(), meld.getCol() + i).orElseThrow(); // We throw, but this should never happen as there will always be an HBox in the GridCell
			var btn = UIHelper.makeDraggable(tileButtonFactory.newTileButton(meld.getMeld().get(i), false, meld.getRow(), meld.getCol() + i), ignoreRootNode);
			btn.disableProperty().bind(game.getNPCTurn());
			addOrReplaceNodeAtCell(btn, meld.getRow(), meld.getCol() + i);
		}
	}

	private void addOrReplaceNodeAtCell(Node node, int row, int col) {
		// Note, when calling this function, you should wrap it with Platform.runLater. It is not done here as having
		// too many runnables can cause issues such as flickering
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
