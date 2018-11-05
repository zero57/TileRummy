package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import model.observable.ObservableTile;

import java.util.Comparator;

public class Hand {

	private ObservableList<ObservableTile> unsortedTiles;
	private ObservableList<ObservableTile> tiles;

	public Hand() {
		unsortedTiles = FXCollections.observableArrayList();
		tiles = new SortedList<>(unsortedTiles, Comparator.naturalOrder());
	}

	public Hand(Hand hand) {
		unsortedTiles = FXCollections.observableArrayList(hand.getTiles());
		tiles = new SortedList<>(unsortedTiles, Comparator.naturalOrder());
	}

	public boolean addTile(ObservableTile tile) {
		return unsortedTiles.add(tile);
	}

	public boolean removeTile(ObservableTile tile) {
		return unsortedTiles.remove(tile);
	}

	public ObservableList<ObservableTile> getTiles() {
		return tiles;
	}

	public ObservableList<ObservableTile> getUnsortedTiles() {
		return unsortedTiles;
	}

	public void clear() {
		unsortedTiles.clear();
	}
}