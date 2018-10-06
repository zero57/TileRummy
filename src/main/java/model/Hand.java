package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Hand {

	private ObservableList<Tile> tiles;

	Hand() { tiles = FXCollections.observableArrayList(); }

	public void addTile(Tile tile) { tiles.add(tile); }

	public void removeTile(Tile tile) { tiles.remove(tile); }

	public ObservableList<Tile> getTiles() { return tiles; }
}