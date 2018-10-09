package factory;

import com.jfoenix.controls.JFXButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import model.Tile;

public class TileButtonFactory {

	public TileButtonFactory() {

	}

	public JFXButton newTileButton(int rank, Tile.Colours colour) {
		JFXButton btn = new JFXButton(Integer.toString(rank));
		switch (colour) {
			case RED:
				btn.getStyleClass().add("redTile");
				break;
			case BLUE:
				btn.getStyleClass().add("blueTile");
				break;
			case GREEN:
				btn.getStyleClass().add("greenTile");
				break;
			case ORANGE:
				btn.getStyleClass().add("orangeTile");
				break;
		}
		HBox.setHgrow(btn, Priority.ALWAYS);
		VBox.setVgrow(btn, Priority.ALWAYS);
		btn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		return btn;
	}

}
