package ui;

import com.jfoenix.controls.JFXButton;
import model.Tile;

public class TileButton extends JFXButton {

	private final Tile tile;

	public TileButton(Tile tile) {
		this(tile, false);
	}

	public TileButton(Tile tile, boolean hide) {
		super(Integer.toString(tile.getRank()));
		this.tile = tile;
		switch (tile.getColour()) {
			case RED:
				this.getStyleClass().add("redTile");
				break;
			case BLUE:
				this.getStyleClass().add("blueTile");
				break;
			case GREEN:
				this.getStyleClass().add("greenTile");
				break;
			case ORANGE:
				this.getStyleClass().add("orangeTile");
				break;
		}
		if (hide) {
			this.getStyleClass().add("blankTile");
		}
	}

	public Tile getTile() {
		return tile;
	}
}
