package model;

import model.observable.ObservableTile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

public class Stock {
	private static final Logger logger = LogManager.getLogger(Stock.class.getName());
	private ArrayList<ObservableTile> stock;

	public Stock() {
		stock = new ArrayList<>();
		for (Tile.Colours colour : Tile.Colours.values()) {
			for (int rank = 1; rank <= 13; rank++) {
				stock.add(new ObservableTile(rank, colour));
				stock.add(new ObservableTile(rank, colour));
			}
		}
	}

	public Stock(ArrayList<ObservableTile> tiles) {
		stock = tiles;
	}

	public Optional<ObservableTile> draw() {
		if (stock.isEmpty()) {
			logger.debug("Stock is empty!");
			return Optional.empty();
		}
		return Optional.of(stock.remove(0));
	}

	public Stock shuffle() {
		Collections.shuffle(stock);
		return this;
	}

	public ArrayList<ObservableTile> getStock() {
		return stock;
	}

	public int getSize() {
		return stock.size();
	}
}