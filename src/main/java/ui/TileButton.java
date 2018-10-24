package ui;

import com.jfoenix.controls.JFXButton;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import model.Tile;

public class TileButton extends JFXButton {

	private final Tile tile;
	private IntegerProperty row;
	private IntegerProperty col;
	private BooleanProperty originatesFromTable;

	public TileButton(Tile tile, boolean hide, int row, int col) {
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
		this.row = new SimpleIntegerProperty(row);
		this.col = new SimpleIntegerProperty(col);
		this.originatesFromTable = new SimpleBooleanProperty();
		this.originatesFromTable.bind(this.row.greaterThanOrEqualTo(0).and(this.col.greaterThanOrEqualTo(0)));
	}

	public Tile getTile() {
		return tile;
	}

	public int getRow() {
		return row.get();
	}

	public int getCol() {
		return col.get();
	}

	public void setRow(int row) {
		this.row.set(row);
	}

	public void setCol(int col) {
		this.col.set(col);
	}

	public IntegerProperty getRowProperty() {
		return row;
	}

	public IntegerProperty getColProperty() {
		return col;
	}

	public boolean getOriginatesFromTable() {
		return originatesFromTable.get();
	}

	public BooleanProperty getOriginatesFromTableProperty() {
		return originatesFromTable;
	}
}
