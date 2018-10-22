package model.observable;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import model.Meld;

public class ObservableMeld extends Meld {
	private IntegerProperty row;
	private IntegerProperty col;

	public ObservableMeld(int row, int col) {
		super();
		this.row = new SimpleIntegerProperty(row);
		this.col = new SimpleIntegerProperty(col);
	}

	public void setRow(int row) {
		this.row.set(row);
	}

	public void setCol(int col) {
		this.col.set(col);
	}

	public int getRow() {
		return row.get();
	}

	public int getCol() {
		return col.get();
	}

	public IntegerProperty getRowProperty() {
		return row;
	}

	public IntegerProperty getColProperty() {
		return col;
	}

}
