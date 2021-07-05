package model.observable;

import model.Tile;

public class ObservableTile extends Tile {
	private boolean beenPlayed;

	public ObservableTile(int rank, Colours colour) {
		super(rank, colour);
	}

	public ObservableTile(boolean isJoker) {
		super(isJoker);
	}

	public void play() {
		beenPlayed = true;
	}

	public boolean hasBeenPlayed() {
		return beenPlayed;
	}
}
