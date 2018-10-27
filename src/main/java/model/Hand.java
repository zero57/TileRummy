package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;

import java.util.Comparator;

public class Hand {

	private ObservableList<Tile> backingTiles;
	private ObservableList<Tile> tiles;

	Hand() {
		backingTiles = FXCollections.observableArrayList();
		tiles = new SortedList<>(backingTiles, Comparator.naturalOrder());
	}

	public boolean addTile(Tile tile) {
		return backingTiles.add(tile);
	}

	public boolean removeTile(Tile tile) {
		return backingTiles.remove(tile);
	}

	public ObservableList<Tile> getTiles() {
		return tiles;
	}
}