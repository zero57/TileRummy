package model;

import java.util.Comparator;

public class Tile implements Comparable<Tile> {
	public enum Colours {RED, BLUE, GREEN, ORANGE}

	private final int rank;
	private final Colours colour;

	public Tile(int rank, Colours colour) {
		this.rank = rank;
		this.colour = colour;
	}

	public int getRank() {
		return rank;
	}

	public Colours getColour() {
		return colour;
	}

	@Override
	public String toString() {
		return getRank() + " " + getColour().name();
	}

	public int compareTo(Tile other) {
		return Comparator.comparing(Tile::getColour).thenComparing(Tile::getRank).compare(this, other);
	}
}