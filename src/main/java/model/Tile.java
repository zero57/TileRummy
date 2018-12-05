package model;

import java.util.Comparator;

public class Tile implements Comparable<Tile> {
	public enum Colours {RED, BLUE, GREEN, ORANGE, UNDEFINED}

	private final int rank;
	private final Colours colour;
	private final boolean isJoker;

	public Tile(int rank, Colours colour) {
		this.rank = rank;
		this.colour = colour;
		isJoker = false;
	}

	public Tile(boolean isJoker) {
		this.rank = -1;
		this.colour = Colours.UNDEFINED;
		this.isJoker = isJoker;
	}

	public int getRank() {
		return rank;
	}

	public Tile(Tile another) {
		this.rank = another.rank;
		this.colour = another.colour;
		this.isJoker = false;
	}

	public Colours getColour() {
		return colour;
	}

	public boolean isJoker() {
		return isJoker;
	}

	@Override
	public String toString() {
		return getRank() + " " + getColour().name();
	}

	public int compareTo(Tile other) {
		return Comparator.comparing(Tile::getColour).thenComparing(Tile::getRank).compare(this, other);
	}
}