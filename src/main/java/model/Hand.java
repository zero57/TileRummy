package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import model.observable.ObservableTile;

import java.util.Comparator;

public class Hand {

	private ObservableList<ObservableTile> unsortedTiles;
	private ObservableList<ObservableTile> tiles;
	private IntegerProperty sizeProperty;

	public Hand() {
		unsortedTiles = FXCollections.observableArrayList();
		tiles = new SortedList<>(unsortedTiles, Comparator.naturalOrder());
		sizeProperty = new SimpleIntegerProperty(0);
		unsortedTiles.addListener((ListChangeListener<ObservableTile>) change -> sizeProperty.set(unsortedTiles.size()));
	}

	public Hand(Hand hand) {
		unsortedTiles = FXCollections.observableArrayList(hand.getTiles());
		tiles = new SortedList<>(unsortedTiles, Comparator.naturalOrder());
		sizeProperty = new SimpleIntegerProperty(0);
		unsortedTiles.addListener((ListChangeListener<ObservableTile>) change -> sizeProperty.set(unsortedTiles.size()));
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

	public IntegerProperty getSizeProperty() {
		return sizeProperty;
	}

	public void clear() {
		unsortedTiles.clear();
	}
}