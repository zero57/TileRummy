package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.observable.ObservableTile;

public class HandRigTable {
	private ObservableList<ObservableList<ObservableTile>> table;

	public HandRigTable() {
		table = FXCollections.observableArrayList();
		Stock stock = new Stock();
		for (int i = 0; i < 9; i++) {
			ObservableList<ObservableTile> row = FXCollections.observableArrayList();
			for (int j = 0; j < 16; j++) {
				stock.draw().ifPresent(row::add);
			}
			table.add(row);
		}
	}

	public ObservableList<ObservableList<ObservableTile>> getTable() {
		return table;
	}

	public void removeTileFromTable(int row, int col) {
		table.get(row).remove(col);
	}

	public void addTileToTable(ObservableTile tile, int row, int col) {
		if (col > table.get(row).size()) {
			table.get(row).add(tile);
			return;
		}
		table.get(row).add(col, tile);
	}
}
