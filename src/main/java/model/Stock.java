package model;

import model.observable.ObservableTile;

import java.util.List;
import java.util.Stack;
import java.util.Collections;

public class Stock {
	private Stack<ObservableTile> stock;

	Stock() {
		stock = new Stack<>();
		for (Tile.Colours colour : Tile.Colours.values()) {
			for (int rank=1; rank<=13; rank++) {
				stock.add(new ObservableTile(rank, colour));
				stock.add(new ObservableTile(rank, colour));
			}
		}
	}

	public ObservableTile draw() { return stock.pop(); }
	public void shuffle() { Collections.shuffle(stock); }
	public List<ObservableTile> getStock() { return Collections.unmodifiableList(stock); }
	public int getSize() { return stock.size(); }
}