package factory;

import ui.TileButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import model.Tile;

public class TileButtonFactory {

	public TileButtonFactory() {

	}

	public TileButton newTileButton(Tile tile, boolean hide) {
		return newTileButton(tile, hide, -1, -1);
	}

	public TileButton newTileButton(Tile tile, boolean hide, int row, int col) {
		TileButton btn = new TileButton(tile, hide, row, col);
		HBox.setHgrow(btn, Priority.ALWAYS);
		VBox.setVgrow(btn, Priority.ALWAYS);
		btn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		return btn;
	}

}
