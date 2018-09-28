package model;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class Hand {

	private List<Tile> tiles;

	Hand() { tiles = new ArrayList<>(); }

	public void addTile(Tile tile) { tiles.add(tile); }
	public void removeTile(Tile tile) { tiles.remove(tile); }
	public List<Tile> getHand() { return Collections.unmodifiableList(tiles); }
}