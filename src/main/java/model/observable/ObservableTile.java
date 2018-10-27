package model.observable;

import model.Tile;

public class ObservableTile extends Tile {
	private boolean beenPlayed;

	public ObservableTile(Tile tile) {
		super(tile.getRank(), tile.getColour());
		beenPlayed = false;
	}

	public void play() {
		beenPlayed = true;
	}

	public boolean hasBeenPlayed() {
		return beenPlayed;
	}
}
