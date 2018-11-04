package model;

import model.observable.ObservableTile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

public class Stock {
	private static final Logger logger = LogManager.getLogger(Stock.class.getName());
	private Stack<ObservableTile> stock;

	Stock() {
		stock = new Stack<>();
		for (Tile.Colours colour : Tile.Colours.values()) {
			for (int rank = 1; rank <= 13; rank++) {
				stock.add(new ObservableTile(rank, colour));
				stock.add(new ObservableTile(rank, colour));
			}
		}
	}

	public Optional<ObservableTile> draw() {
		if (stock.empty()) {
			logger.debug("Stock is empty!");
			return Optional.empty();
		}
		return Optional.of(stock.pop());
	}

	public void shuffle() {
		Collections.shuffle(stock);
	}

	public List<ObservableTile> getStock() {
		return Collections.unmodifiableList(stock);
	}

	public int getSize() {
		return stock.size();
	}
}