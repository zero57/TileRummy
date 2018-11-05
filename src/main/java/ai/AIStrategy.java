package ai;

import model.Game;
import model.Hand;
import model.Meld;
import model.Tile;
import model.observable.ObservableTile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Comparator;

import java.util.ArrayList;
import java.util.List;

public abstract class AIStrategy {
	protected static final Logger logger = LogManager.getLogger(AIStrategy.class.getName());
	protected Game game;
	protected Hand hand;

	public AIStrategy(Game game, Hand hand) {
		this.game = game;
		this.hand = hand;
	}

	abstract boolean firstHandStrategy();

	abstract void regularStrategy();

	boolean attemptToPlayFirstHand() {
		int runTotal = 0;
		int setTotal = 0;
		ArrayList<Meld> runMelds = findRunsInHand(this.hand);
		ArrayList<Meld> setMelds = findSetsInHand(this.hand);

		if (!(runMelds.isEmpty())) {
			for (Meld meld : runMelds) {
				runTotal += meld.getValue();
			}
		} else if (!(setMelds.isEmpty())) {
			for (Meld meld : setMelds) {
				setTotal += meld.getValue();
			}
		} else {
			return false;
		}

		if (runTotal > setTotal) {
			int fakeSetTotal = 0;
			Hand fakeHand = new Hand(this.hand);
			for (Meld meld : runMelds) {
				for (ObservableTile t : meld.getMeld()) {
					fakeHand.removeTile(t);
				}
			}
			ArrayList<Meld> fakeSetMelds = findSetsInHand(fakeHand);
			if (!(fakeSetMelds.isEmpty())) {
				for (Meld meld : fakeSetMelds) {
					fakeSetTotal += meld.getValue();
				}
			}
			if (runTotal + fakeSetTotal >= 30) {
				for (Meld meld : fakeSetMelds) {
					runMelds.add(meld);
				}

				for (Meld meld : runMelds) {
					logger.debug(meld);
					for (ObservableTile t : meld.getMeld()) {
						this.hand.removeTile(t);
					}
				}

				playMeldsToTable(runMelds);

				return true;
			} else {
				return false;
			}
		} else {
			int fakeRunTotal = 0;
			Hand fakeHand = new Hand(this.hand);
			for (Meld meld : setMelds) {
				for (ObservableTile t : meld.getMeld()) {
					fakeHand.removeTile(t);
				}
			}
			ArrayList<Meld> fakeRunMelds = findRunsInHand(fakeHand);
			if (!(fakeRunMelds.isEmpty())) {
				for (Meld meld : fakeRunMelds) {
					fakeRunTotal += meld.getValue();
				}
			}
			if (setTotal + fakeRunTotal >= 30) {
				for (Meld meld : fakeRunMelds) {
					setMelds.add(meld);
				}

				for (Meld meld : setMelds) {
					logger.debug(meld);
					for (ObservableTile t : meld.getMeld()) {
						this.hand.removeTile(t);
					}
				}

				playMeldsToTable(setMelds);

				return true;
			} else {
				return false;
			}
		}
	}

	boolean attemptToPlayAllTiles() {
		List<ObservableTile> tiles = new ArrayList<ObservableTile>();
		game.getTable().forEach(observableMeld -> tiles.addAll(observableMeld.getMeld()));
		tiles.addAll(hand.getUnsortedTiles());
		List<List<List<ObservableTile>>> tilesByRank = new ArrayList<List<List<ObservableTile>>>();
		//dummy in position 0
		tilesByRank.add(null);
		for(int i = 1;i<=13;++i){
			tilesByRank.add(new ArrayList<>());
			for(int j = 0;j<4;++j){
				tilesByRank.get(i).add(new ArrayList<ObservableTile>());
			}
		}

		for (ObservableTile tile:tiles) {
			tilesByRank.get(tile.getRank()).get(tile.getColour().ordinal()).add(tile);
		}

		List<Meld> compiledSets = new ArrayList<Meld>();
		List<List<ObservableTile>> compiledRuns = new ArrayList<List<ObservableTile>>();
		//make 8 "channels"
		for(int i = 0;i<8;++i){
			compiledRuns.add(new ArrayList<>());
		}
		int [][] runState = {{0,0},{0,0},{0,0},{0,0}};
		if(maximalTilePlay(1,runState,tilesByRank,compiledSets,compiledRuns)){
			List<Meld> allMelds = compiledSets;
			for(int i = 0;i<8;++i){
				Hand fakeHand = new Hand();
				compiledRuns.get(i).forEach(t -> fakeHand.addTile(t));
				allMelds.addAll(findRunsInHand(fakeHand));
			}
			playEverythingToTable(allMelds);
			hand.getUnsortedTiles().clear();
			return true;
		}
		return false;
	}

	private boolean maximalTilePlay(int rank, int[][] runState, List<List<List<ObservableTile>>> tilesByRank, List<Meld> compiledSets, List<List<ObservableTile>> compiledRuns){
		int[][] redAdditions;int[][] greenAdditions;int[][] blueAdditions;int[][] orangeAdditions;
		if(rank<=11){
			redAdditions = findAdditions(runState[0]);
			greenAdditions = findAdditions(runState[1]);
			blueAdditions = findAdditions(runState[2]);
			orangeAdditions = findAdditions(runState[3]);
		}else{
			redAdditions = findAdditionsRank12or13(runState[0]);
			greenAdditions = findAdditionsRank12or13(runState[1]);
			blueAdditions = findAdditionsRank12or13(runState[2]);
			orangeAdditions = findAdditionsRank12or13(runState[3]);
		}
		List<List<ObservableTile>> currentTiles = tilesByRank.get(rank);

		for (int[] r:redAdditions) {
			int redTilesRemaining = currentTiles.get(0).size()-(r[0]+r[1]);
			if(redTilesRemaining>=0){
				for (int[] g:greenAdditions) {
					int greenTilesRemaining = currentTiles.get(1).size()-(g[0]+g[1]);
					if(greenTilesRemaining>=0){
						int maxColour2 = Math.max(redTilesRemaining,greenTilesRemaining);
						for (int[] b:blueAdditions) {
							int blueTilesRemaining = currentTiles.get(2).size()-(b[0]+b[1]);
							if(blueTilesRemaining>=0){
								int maxColour3 = Math.max(maxColour2,blueTilesRemaining);
								for (int[] o:orangeAdditions) {
									int orangeTilesRemaining = currentTiles.get(3).size()-(o[0]+o[1]);
									if(orangeTilesRemaining>=0){
										int tilesRemaining = redTilesRemaining + greenTilesRemaining + blueTilesRemaining + orangeTilesRemaining;
										if(tilesRemaining==1 || tilesRemaining==2 || tilesRemaining==5){
											continue;
										}
										int maxColour4 = Math.max(maxColour3,orangeTilesRemaining);
										if(maxColour4==2&&(tilesRemaining==3 || tilesRemaining==4)){
											continue;
										}
										int[][] additions = {r,g,b,o};
										if(rank == 13 || maximalTilePlay(rank+1,addStateAndAddition(runState,additions),tilesByRank,compiledSets,compiledRuns)){
											for (int i =0;i<4;++i){
												for (int j=0;j<2;++j){
													if (additions[i][j]==1){
														compiledRuns.get(2*i+j).add(0,currentTiles.get(i).remove(0));
													}
												}
											}
											compiledSets.addAll(formSetsMaximalTileHelper(currentTiles,tilesRemaining));
											return true;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return false;
	}

	private List<Meld> formSetsMaximalTileHelper(List<List<ObservableTile>> currentTiles, int totalTiles){
		List<Meld> result = new ArrayList<Meld>();
		if(totalTiles == 0){
			return result;
		}
		if(totalTiles==3 || totalTiles==4){
			result.add(new Meld());
			for (List<ObservableTile> l: currentTiles) {
				if(!l.isEmpty()){
					result.get(0).addLastTile(l.remove(0));
				}
			}
			return result;
		}
		result.add(new Meld());
		result.add(new Meld());
		for (List<ObservableTile> l: currentTiles) {
			if(!l.isEmpty()){
				result.get(0).addLastTile(l.remove(0));
				if(!l.isEmpty()){
					result.get(1).addLastTile(l.remove(0));
				}
			}
		}
		//if there were 6 tiles and 2 of them were isolates (didn't have a duplicate tile), result.get(1) will only have 2 tiles
		if (!result.get(1).isValidLength()){
			List<ObservableTile> firstMeldTiles = result.get(0).getMeld();
			for(int i=0;i<4;++i){
				if(result.get(1).addLastTile(firstMeldTiles.get(i))){
					firstMeldTiles.remove(i);
					result.set(0,new Meld());
					firstMeldTiles.forEach(t -> result.get(0).addLastTile(t));
				}
			}
		}
		return result;
	}

	private int[][] findAdditions(int[] suitRunState){
		if((suitRunState[0]==0 && suitRunState[1]==0)||(suitRunState[0]>=3 && suitRunState[1]>=3)){
			int[][] result = {{0,0},{0,1},{1,1}};
			return result;
		}
		if(suitRunState[0]==1 || suitRunState[0]==2){
			if(suitRunState[1]==1 || suitRunState[1]==2){
				int[][] result = {{1,1}};
				return result;
			}
			else{
				int[][] result = {{1,0},{1,1}};
				return result;
			}
		}else {
			if(suitRunState[1]==1 || suitRunState[1]==2){
				int[][] result = {{0,1},{1,1}};
				return result;
			}
			else{
				int[][] result = {{0,0},{0,1},{1,0},{1,1}};
				return result;
			}
		}
	}

	private int[][] findAdditionsRank12or13(int[] suitRunState){
		if(suitRunState[0]>=3){
			if(suitRunState[1]>=3){
				int[][] result = {{0,0},{0,1},{1,1}};
				return result;
			}else if(suitRunState[1]==0){
				int[][] result = {{0,0},{1,0}};
				return result;
			}else {
				int[][] result = {{0,1},{1,1}};
				return result;
			}
		}else if(suitRunState[0]==0){
			if(suitRunState[1]>=3){
				int[][] result = {{0,0},{0,1}};
				return result;
			}else if(suitRunState[1]==0){
				int[][] result = {{0,0}};
				return result;
			}else{
				int[][] result = {{0,1}};
				return result;
			}
		}else{
			if(suitRunState[1]>=3){
				int[][] result = {{1,0},{1,1}};
				return result;
			}else if(suitRunState[1]==0){
				int[][] result = {{1,0}};
				return result;
			}else{
				int[][] result = {{1,1}};
				return result;
			}
		}
	}

	private int[][] addStateAndAddition(int[][] runState, int[][] additions){
		int[][] result = new int[4][2];
		for (int i =0;i<4;++i){
			for (int j=0;j<2;++j){
				if(additions[i][j]==0){
					result[i][j]=0;
				}else{
					result[i][j]=runState[i][j]+1;
				}
			}
		}
		return result;
	}

	boolean playConservative() {
		return false;
	}

	boolean playLiberal() {
		int runTotal = 0;
		int setTotal = 0;
		ArrayList<Meld> runMelds = findRunsInHand(this.hand);
		ArrayList<Meld> setMelds = findSetsInHand(this.hand);

		if (!(runMelds.isEmpty())) {
			for (Meld meld : runMelds) {
				runTotal += meld.getValue();
			}
		} else if (!(setMelds.isEmpty())) {
			for (Meld meld : setMelds) {
				setTotal += meld.getValue();
			}
		} else {
			return false;
		}

		if (runTotal > setTotal) {
			int fakeSetTotal = 0;
			Hand fakeHand = new Hand(this.hand);
			for (Meld meld : runMelds) {
				for (ObservableTile t : meld.getMeld()) {
					fakeHand.removeTile(t);
				}
			}
			ArrayList<Meld> fakeSetMelds = findSetsInHand(fakeHand);
			if (!(fakeSetMelds.isEmpty())) {
				for (Meld meld : fakeSetMelds) {
					fakeSetTotal += meld.getValue();
				}
			}

			for (Meld meld : runMelds) {
				logger.debug(meld);
				for (ObservableTile t : meld.getMeld()) {
					this.hand.removeTile(t);
				}
			}

			playMeldsToTable(runMelds);

			return true;
		} else {
			int fakeRunTotal = 0;
			Hand fakeHand = new Hand(this.hand);
			for (Meld meld : setMelds) {
				for (ObservableTile t : meld.getMeld()) {
					fakeHand.removeTile(t);
				}
			}
			ArrayList<Meld> fakeRunMelds = findRunsInHand(fakeHand);
			if (!(fakeRunMelds.isEmpty())) {
				for (Meld meld : fakeRunMelds) {
					fakeRunTotal += meld.getValue();
				}
			}

			for (Meld meld : setMelds) {
				logger.debug(meld);
				for (ObservableTile t : meld.getMeld()) {
					this.hand.removeTile(t);
				}
			}

			playMeldsToTable(setMelds);

			return true;
		}
	}

	ArrayList<Meld> findRunsInHand(Hand hand) {
		ArrayList melds = new ArrayList<Meld>();
		Meld meld;

		for (int i = 0; i < hand.getTiles().size() - 2; i++) {
			if ((hand.getTiles().get(i + 1).getRank() == hand.getTiles().get(i).getRank() + 1) &&
					(hand.getTiles().get(i + 2).getRank() == hand.getTiles().get(i + 1).getRank() + 1)) {

				meld = new Meld();
				meld.addLastTile(hand.getTiles().get(i));
				meld.addLastTile(hand.getTiles().get(i + 1));
				meld.addLastTile(hand.getTiles().get(i + 2));
				i += 2;
				while ((i < hand.getTiles().size() - 1) && (hand.getTiles().get(i + 1).getRank() == hand.getTiles().get(i).getRank() + 1)) {
					meld.addLastTile(hand.getTiles().get(i + 1));
					i++;
				}

				melds.add(meld);
			}
		}

		return melds;
	}

	ArrayList<Meld> findSetsInHand(Hand hand) {
		ArrayList melds = new ArrayList<Meld>();
		Meld meld;

		hand.getUnsortedTiles().sort(Comparator.comparing(ObservableTile::getRank).thenComparing(ObservableTile::getColour));
		for (int i = 0; i < hand.getUnsortedTiles().size() - 2; i++) {
			if ((hand.getUnsortedTiles().get(i + 1).getRank() == hand.getUnsortedTiles().get(i).getRank()) &&
					(hand.getUnsortedTiles().get(i + 2).getRank() == hand.getUnsortedTiles().get(i + 1).getRank())) {
				if ((hand.getUnsortedTiles().get(i + 1).getColour() != hand.getUnsortedTiles().get(i).getColour()) &&
						(hand.getUnsortedTiles().get(i + 1).getColour() != hand.getUnsortedTiles().get(i + 2).getColour()) &&
						(hand.getUnsortedTiles().get(i).getColour() != hand.getUnsortedTiles().get(i + 2).getColour())) {
					meld = new Meld();
					meld.addLastTile(hand.getUnsortedTiles().get(i));
					meld.addLastTile(hand.getUnsortedTiles().get(i + 1));
					meld.addLastTile(hand.getUnsortedTiles().get(i + 2));

					if ((i < hand.getUnsortedTiles().size() - 1)) {
						if (meld.isValidLastTile(hand.getUnsortedTiles().get(i + 1))) {
							meld.addLastTile(hand.getUnsortedTiles().get(i + 1));
							i++;
						}
					}

					melds.add(meld);
				}
			}
		}
		return melds;
	}

	void playMeldsToTable(List<Meld> melds) {
		int i = 0;
		int j = 0;
		game.getTable().forEach(observableMeld -> melds.add((Meld) observableMeld));

		game.getTable().clear();
		for (Meld meld : melds) {
			if (i + meld.getSize() - 1 > 15) {
				j++;
				i = 0;
			}
			for (ObservableTile tile : meld.getMeld()) {
				game.addTileToTable(tile, j, i);
				i++;
			}
			i++;
		}
	}

	//same as above, but assumes melds has the exact list of melds that will appear on the table
	void playEverythingToTable(List<Meld> melds) {

		int i = 0;
		int j = 0;

		game.getTable().clear();
		for (Meld meld : melds) {
			if (i + meld.getSize() - 1 > 15) {
				j++;
				i = 0;
			}
			for (ObservableTile tile : meld.getMeld()) {
				game.addTileToTable(tile, j, i);
				i++;
			}
			i++;
		}
	}
}