package model;

import javafx.collections.ObservableList;
import model.observable.ObservableMeld;

public class Memento {
	private ObservableList<ObservableMeld> table;
	private Hand hand;

	public Memento(ObservableList<ObservableMeld> table, Hand hand) {
		this.table = table;
		this.hand = hand;
	}

	public ObservableList<ObservableMeld> getTableState() {
	 	return table;
	}

	public Hand getHandState() {
		return hand;
	}
}