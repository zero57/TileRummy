package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Hand {

	private ObservableList<Tile> tiles;

	Hand() { tiles = FXCollections.observableArrayList(); }

	public boolean addTile(Tile tile) { return tiles.add(tile); }

	public boolean removeTile(Tile tile) { return tiles.remove(tile); }

	public ObservableList<Tile> getTiles() { return tiles; }
}