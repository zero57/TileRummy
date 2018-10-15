package controller;

import factory.TileButtonFactory;
import javafx.fxml.FXML;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.layout.GridPane;
import model.Game;
import model.Tile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ui.TileButton;

public class TableController {

	private static final Logger logger = LogManager.getLogger(TableController.class.getName());

	@FXML
	private GridPane gpTable;

	private final Game game;
	private final TileButtonFactory tileButtonFactory;

	public TableController(Game game) {
		this.game = game;
		this.tileButtonFactory = new TileButtonFactory();
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
				game.getPlayer1Hand().removeTile(tile);
				// todo: add logic to adding tile to Game table
				// this code below is for temporary testing purposes
				// gpTable.add(tileButtonFactory.newTileButton(tile), col, row);
			});

		});
	}

}
