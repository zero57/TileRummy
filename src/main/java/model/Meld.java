package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Meld {
	public enum Types {UNDETERMINED, SET, RUN}

	private ObservableList<Tile> tiles;
	private Types type;
	private int value;

	public Meld() {
		tiles = FXCollections.observableArrayList();
		type = Types.UNDETERMINED;
		value = 0;
	}

	public ObservableList<Tile> getMeld() {
		return tiles;
	}

	public Types getType() {
		return type;
	}

	public int getValue() {
		return value;
	}

	public int getLength() {
		return tiles.size();
	}

	public boolean isValidLength() {
		return getLength() >= 3;
	}

	public boolean isValidFirstTile(Tile tile) {
		if (tiles.isEmpty()) {
			return true;
		}
		if (tiles.size() == 1) {
			if ((tile.getRank() == tiles.get(0).getRank() - 1) && (tile.getColour() == tiles.get(0).getColour())) {
				type = Types.RUN;
				return true;
			}
			if ((tile.getRank() == tiles.get(0).getRank()) && (tile.getColour() != tiles.get(0).getColour())) {
				type = Types.SET;
				return true;
			}
			return false;
		}
		//tiles.size()>1
		if ((type == Types.RUN) && (tile.getRank() == tiles.get(0).getRank() - 1) && (tile.getColour() == tiles.get(0).getColour())) {
			return true;
		}
		if (type == Types.SET) {
			for (Tile t : tiles) {
				if (t.getColour() == tile.getColour()) {
					return false;
				}
			}
			if (tile.getRank() == tiles.get(0).getRank()) {
				return true;
			}
			return false;
		}
		return false;
	}

	public boolean addFirstTile(Tile tile) {
		boolean isValid = isValidFirstTile(tile);
		if (isValid) {
			value += tile.getRank();
			tiles.add(0, tile);
		}
		return isValid;
	}

	public boolean isValidLastTile(Tile tile) {
		if (tiles.isEmpty()) {
			return true;
		}
		if (tiles.size() == 1) {
			if ((tile.getRank() == tiles.get(tiles.size() - 1).getRank() + 1) && (tile.getColour() == tiles.get(0).getColour())) {
				type = Types.RUN;
				return true;
			}
			if ((tile.getRank() == tiles.get(0).getRank()) && (tile.getColour() != tiles.get(0).getColour())) {
				type = Types.SET;
				return true;
			}
			return false;
		}
		//tiles.size()>1
		if ((type == Types.RUN) && (tile.getRank() == tiles.get(tiles.size() - 1).getRank() + 1) && (tile.getColour() == tiles.get(0).getColour())) {
			return true;
		}
		if (type == Types.SET) {
			for (Tile t : tiles) {
				if (t.getColour() == tile.getColour()) {
					return false;
				}
			}
			if (tile.getRank() == tiles.get(0).getRank()) {
				return true;
			}
			return false;
		}
		return false;
	}

	public boolean addLastTile(Tile tile) {
		boolean isValid = isValidLastTile(tile);
		if (isValid) {
			value += tile.getRank();
			tiles.add(tile);
		}
		return isValid;
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

	public int getSize() {
		return tiles.size();
	}

	@Override
	public String toString() {
		return tiles.toString();
	}
}