package model;

public class Tile implements Comparable<Tile> {
	public enum Colours {RED, BLUE, GREEN, ORANGE}

	private final int rank;
	private final Colours colour;

	public Tile(int rank, Colours colour) {
		this.rank = rank;
		this.colour = colour;
	}

	public int getRank() { return rank; }

	public Colours getColour() { return colour; }

	@Override
	public int compareTo(Tile other) {
		if (this.getColour().ordinal() < other.getColour().ordinal()) {
			return -1;
		}
		if(this.getColour().ordinal() > other.getColour().ordinal()) {
			return 1;
		}
		if (this.getRank() < other.getRank()) {
			return -1;
		}
		if (this.getRank() > other.getRank()) {
			return 1;
		}
		return 0;
	}

	@Override
	public String toString() { return getRank() + " " + getColour().name(); }
}