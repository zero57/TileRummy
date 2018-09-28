package model;

public class Tile {
	public enum Colours{ RED, BLUE, GREEN, ORANGE; }

	private final int rank;
	private final Colours colour;

	Tile(int rank, Colours colour) {
		this.rank = rank;
		this.colour = colour;
	}

	public int getRank() { return rank; }
	public Colours getColour() { return colour; }
}