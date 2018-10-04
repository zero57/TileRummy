package model;

import java.util.List;
import java.util.Stack;
import java.util.Collections;

public class Stock {
	private Stack<Tile> stock;

	Stock() {
		stock = new Stack<>();
		for (Tile.Colours colour : Tile.Colours.values()) {
			for (int rank=0; rank<13; rank++) {
				stock.add(new Tile(rank, colour));
				stock.add(new Tile(rank, colour));
			}
		}
	}

	public Tile draw() { return stock.pop(); }
	public void shuffle() { Collections.shuffle(stock); }
	public List<Tile> getStock() { return Collections.unmodifiableList(stock); }
}