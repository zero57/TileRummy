package model;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class Meld {

	private List <Tile> tiles;

	Meld() { tiles = new ArrayList<>(); }

	public void addFirstTile(Tile tile) { tiles.add(0,tile); }
	public void addLastTile(Tile tile) { tiles.add(tile); }
	public void removeTile(Tile tile) { tiles.remove(tile); }
	public List<Tile> getMeld() { return Collections.unmodifiableList(tiles); }
}