package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.observable.ObservableTile;

public class Meld {
	public enum Types {UNDETERMINED, SET, RUN}

	private ObservableList<ObservableTile> tiles;
	private Types type;
	private int lowerBound;
	private int upperBound;
	private Tile.Colours colour;

	public Meld() {
		tiles = FXCollections.observableArrayList();
		type = Types.UNDETERMINED;
		lowerBound = -1;
		upperBound = -1;
		colour = Tile.Colours.UNDEFINED;
	}

	public ObservableList<ObservableTile> getMeld() {
		return tiles;
	}

	public Types getType() {
		return type;
	}

	public int getValue() {
		if(type==Types.SET){
			return lowerBound*getSize();
		}
		if(type==Types.RUN){
			int total = 0;
			for (int i = lowerBound;i<=upperBound;++i){
				total+=i;
			}
			return total;
		}
		return 0;
	}

	public int getLowerBound() {
		return lowerBound;
	}

	public int getUpperBound() {
		return upperBound;
	}

	public boolean hasJoker() {
		for (ObservableTile t: tiles) {
			if(t.isJoker()){
				return true;
			}
		}
		return false;
	}

	public int getSize() {
		return tiles.size();
	}

	public boolean isValidLength() {
		return getSize() >= 3;
	}

	public boolean isValidFirstTile(ObservableTile tile) {
		if (tiles.isEmpty()) {
			return true;
		}
		if (tiles.size() == 1) {
			if (tile.isJoker()){
				return !hasJoker();
			}
			if (hasJoker()){
				return true;
			}
			if ((tile.getRank() == tiles.get(0).getRank() - 1) && (tile.getColour() == tiles.get(0).getColour())) {
				return true;
			}
			if ((tile.getRank() == tiles.get(0).getRank()) && (tile.getColour() != tiles.get(0).getColour())) {
				return true;
			}
			return false;
		}
		if(tiles.size()==2){
			if (tile.isJoker()){
				return !(hasJoker() || (tiles.get(0).getRank()==1 && tiles.get(0).getRank()==2));
			}
			//tile is not joker
			if (hasJoker()){
				if(tiles.get(0).isJoker()){
					return (tile.getRank()==tiles.get(1).getRank() && tile.getColour()!=tiles.get(1).getColour())
							|| (tile.getRank()==tiles.get(1).getRank() - 2 && tile.getColour()==tiles.get(1).getColour());
				}
				return (tile.getRank()==tiles.get(0).getRank() && tile.getColour()!=tiles.get(0).getColour())
						|| (tile.getRank()==tiles.get(0).getRank() - 1 && tile.getColour()==tiles.get(0).getColour());
			}
			//no jokers
			//set
			if(tiles.get(0).getRank()==tiles.get(1).getRank()){
				return tile.getRank()==tiles.get(0).getRank() && tile.getColour()!=tiles.get(0).getColour() && tile.getColour()!=tiles.get(1).getColour();
			}
			//run
			if(tiles.get(0).getColour()==tiles.get(1).getColour()){
				return tile.getRank()==tiles.get(0).getRank()-1 && tile.getColour()==tiles.get(0).getColour();
			}
		}
		//size>=3, type and upper and lower bounds are determined
		if (tile.isJoker()){
			return !(hasJoker() || (type==Types.RUN && lowerBound == 1) || (type==Types.SET && getSize()==4));
		}
		if (type == Types.RUN) {
			return (tile.getRank() == lowerBound - 1) && (tile.getColour() == colour);
		}
		if (type == Types.SET) {
			if (getSize()==4){
				return false;
			}
			for (ObservableTile t : tiles) {
				if (t.getColour() == tile.getColour()) {
					return false;
				}
			}
			return tile.getRank() == lowerBound;
		}
		return false;
	}

	//sets type, bounds, and colour of a (valid) meld with 3 tiles
	//sets bounds for meld with more than 3 tiles
	//clears these fields for melds of lower size
	private void updateTypeBoundsAndColour(){
		if(getSize()<3){
			type = Types.UNDETERMINED;
			lowerBound = -1;
			upperBound = -1;
			colour = Tile.Colours.UNDEFINED;
		}else if(getSize()>3 && type==Types.RUN){
			if(tiles.get(0).isJoker()){
				lowerBound = tiles.get(1).getRank()-1;
				}else{
				lowerBound = tiles.get(0).getRank();
			}
			if(tiles.get(getSize()-1).isJoker()){
				upperBound = tiles.get(getSize()-2).getRank()+1;
			}else{
				upperBound = tiles.get(getSize()-1).getRank();
			}
		} else if(getSize()==3){
			if(hasJoker()){
				if(tiles.get(0).isJoker()){
					if(tiles.get(1).getRank()==tiles.get(2).getRank()){
						type = Types.SET;
						upperBound = lowerBound = tiles.get(1).getRank();
					}else{
						type = Types.RUN;
						lowerBound = tiles.get(1).getRank() - 1;
						upperBound = tiles.get(2).getRank();
						colour = tiles.get(1).getColour();
					}
				}else if(tiles.get(1).isJoker()){
					if(tiles.get(0).getRank()==tiles.get(2).getRank()){
						type = Types.SET;
						upperBound = lowerBound = tiles.get(0).getRank();
					}else{
						type = Types.RUN;
						lowerBound = tiles.get(0).getRank();
						upperBound = tiles.get(2).getRank();
						colour = tiles.get(0).getColour();
					}
				}else if(tiles.get(2).isJoker()){
					if(tiles.get(0).getRank()==tiles.get(1).getRank()){
						type = Types.SET;
						upperBound = lowerBound = tiles.get(0).getRank();
					}else{
						type = Types.RUN;
						lowerBound = tiles.get(0).getRank();
						upperBound = tiles.get(1).getRank() + 1;
						colour = tiles.get(0).getColour();
					}
				}
			} else {
				if(tiles.get(0).getRank()==tiles.get(1).getRank()){
					type = Types.SET;
					upperBound = lowerBound = tiles.get(0).getRank();
				}else{
					type = Types.RUN;
					lowerBound = tiles.get(0).getRank();
					upperBound = tiles.get(2).getRank();
					colour = tiles.get(0).getColour();
				}
			}
		}
	}

	public boolean isValidLastTile(ObservableTile tile) {
		if (tiles.isEmpty()) {
			return true;
		}
		if (tiles.size() == 1) {
			if (tile.isJoker()){
				return !hasJoker();
			}
			if (hasJoker()){
				return true;
			}
			if ((tile.getRank() == tiles.get(0).getRank() + 1) && (tile.getColour() == tiles.get(0).getColour())) {
				return true;
			}
			if ((tile.getRank() == tiles.get(0).getRank()) && (tile.getColour() != tiles.get(0).getColour())) {
				return true;
			}
			return false;
		}
		if(tiles.size()==2){
			if (tile.isJoker()){
				return !(hasJoker() || (tiles.get(0).getRank()==12 && tiles.get(1).getRank()==13));
			}
			//tile is not joker
			if (hasJoker()){
				if(tiles.get(0).isJoker()){
					return (tile.getRank()==tiles.get(1).getRank() && tile.getColour()!=tiles.get(1).getColour())
							|| (tile.getRank()==tiles.get(1).getRank() + 1 && tile.getColour()==tiles.get(1).getColour());
				}
				return (tile.getRank()==tiles.get(0).getRank() && tile.getColour()!=tiles.get(0).getColour())
						|| (tile.getRank()==tiles.get(0).getRank() + 2 && tile.getColour()==tiles.get(0).getColour());
			}
			//no jokers
			//set
			if(tiles.get(0).getRank()==tiles.get(1).getRank()){
				return tile.getRank()==tiles.get(0).getRank() && tile.getColour()!=tiles.get(0).getColour() && tile.getColour()!=tiles.get(1).getColour();
			}
			//run
			if(tiles.get(0).getColour()==tiles.get(1).getColour()){
				return tile.getRank()==tiles.get(1).getRank()+1 && tile.getColour()==tiles.get(0).getColour();
			}
		}
		//size>=3, type and upper and lower bounds are determined
		if (tile.isJoker()){
			return !(hasJoker() || (type==Types.RUN && upperBound == 13) || (type==Types.SET && getSize()==4));
		}
		if (type == Types.RUN) {
			return (tile.getRank() == upperBound + 1) && (tile.getColour() == colour);
		}
		if (type == Types.SET) {
			if (getSize()==4){
				return false;
			}
			for (ObservableTile t : tiles) {
				if (t.getColour() == tile.getColour()) {
					return false;
				}
			}
			return tile.getRank() == lowerBound;
		}
		return false;
	}

	public boolean addFirstTile(ObservableTile tile) {
		if (isValidFirstTile(tile)) {
			tiles.add(0, tile);
			updateTypeBoundsAndColour();
			return true;
		}
		return false;
	}

	public boolean addLastTile(ObservableTile tile) {
		if (isValidLastTile(tile)) {
			tiles.add(tile);
			updateTypeBoundsAndColour();
			return true;
		}
		return false;
	}

	public boolean removeFirstTile() {
		if (!tiles.isEmpty() && !hasJoker()) {
			tiles.remove(0);
			updateTypeBoundsAndColour();
			return true;
		}
		return false;
	}

	public boolean removeLastTile() {
		if (!tiles.isEmpty() && !hasJoker()) {
			tiles.remove(getSize()-1);
			updateTypeBoundsAndColour();
			return true;
		}
		return false;
	}

	public boolean replaceJoker(ObservableTile tile) {
		if(hasJoker()&&getSize()>=3){
			if(tile.isJoker()){
				return true;
			}
			int jokerPosition = getJokerPosition();
			if(type==Types.SET){
				if (tile.getRank() == lowerBound){
					for (Tile t: tiles) {
						if(tile.getColour()==t.getColour()){
							return false;
						}
					}
					tiles.set(jokerPosition,tile);
					return true;
				}
				return false;
			}
			//type == RUN
			if(tile.getColour() == colour){
				if(tile.getRank()==lowerBound+jokerPosition){
					tiles.set(jokerPosition,tile);
					return true;
				}
			}
			return false;
		}
		return false;
	}

	public int getJokerPosition(){
		int jokerPosition = -1;
		for (int i = 0; i<tiles.size();++i){
			if(tiles.get(i).isJoker()){
				jokerPosition = i;
				break;
			}
		}
		return jokerPosition;
	}

	@Override
	public String toString() {
		return tiles.toString();
	}
}