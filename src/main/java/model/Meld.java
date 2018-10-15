package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Meld {
	private enum Types {UNDETERMINED, SET, RUN}

	private ObservableList<Tile> tiles;
	private Types type;
	private int value;

	Meld() {
		tiles = FXCollections.observableArrayList();
		type = Types.UNDETERMINED;
		value = 0;
	}

	public ObservableList<Tile> getMeld() {
		return tiles;
	}

	public int getValue() {
		return value;
	}

	public boolean isValid() {
		return tiles.size() >= 3;
	}

	private void simpleAddFirstTile(Tile tile) {
		value += tile.getRank();
		tiles.add(0, tile);
	}

	private void simpleAddLastTile(Tile tile) {
		value += tile.getRank();
		tiles.add(tile);
	}

	public boolean addFirstTile(Tile tile) {
		if (tiles.isEmpty()) {
			simpleAddFirstTile(tile);
			return true;
		}
		if (tiles.size() == 1) {
			if ((tile.getRank() == tiles.get(0).getRank() - 1) && (tile.getColour() == tiles.get(0).getColour())) {
				simpleAddFirstTile(tile);
				type = Types.RUN;
				return true;
			}
			if ((tile.getRank() == tiles.get(0).getRank()) && (tile.getColour() != tiles.get(0).getColour())) {
				simpleAddFirstTile(tile);
				type = Types.SET;
				return true;
			}
			return false;
		}
		//tiles.size()>=1
		if ((type == Types.RUN) && (tile.getRank() == tiles.get(0).getRank() - 1) && (tile.getColour() == tiles.get(0).getColour())) {
			simpleAddFirstTile(tile);
			return true;
		}
		if (type == Types.SET) {
			for (Tile t : tiles) {
				if (t.getColour() == tile.getColour()) {
					return false;
				}
			}
			if (tile.getRank() == tiles.get(0).getRank()) {
				simpleAddFirstTile(tile);
				return true;
			}
			return false;
		}
		return false;
	}

	public boolean addLastTile(Tile tile) {
		if (tiles.isEmpty()) {
			simpleAddLastTile(tile);
			return true;
		}
		if (tiles.size() == 1) {
			if ((tile.getRank() == tiles.get(tiles.size() - 1).getRank() + 1) && (tile.getColour() == tiles.get(0).getColour())) {
				simpleAddLastTile(tile);
				type = Types.RUN;
				return true;
			}
			if ((tile.getRank() == tiles.get(0).getRank()) && (tile.getColour() != tiles.get(0).getColour())) {
				simpleAddLastTile(tile);
				type = Types.SET;
				return true;
			}
			return false;
		}
		//tiles.size()>=1
		if ((type == Types.RUN) && (tile.getRank() == tiles.get(tiles.size() - 1).getRank() + 1) && (tile.getColour() == tiles.get(0).getColour())) {
			simpleAddLastTile(tile);
			return true;
		}
		if (type == Types.SET) {
			for (Tile t : tiles) {
				if (t.getColour() == tile.getColour()) {
					return false;
				}
			}
			if (tile.getRank() == tiles.get(0).getRank()) {
				simpleAddLastTile(tile);
				return true;
			}
			return false;
		}
		return false;
	}

	public void removeFirstTile() {
		if (!tiles.isEmpty()) {
			value -= tiles.remove(0).getRank();
		}
	}

	public void removeLastTile() {
		if (!tiles.isEmpty()) {
			value -= tiles.remove(tiles.size() - 1).getRank();
		}
	}
}