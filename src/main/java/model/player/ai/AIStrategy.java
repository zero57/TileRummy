package model.player.ai;

import model.Game;
import model.Hand;
import model.Meld;
import model.observable.ObservableTile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		List<Meld> highestValueMelds = highestValueMelds(hand);
		int totalValue = 0;
		for (Meld m : highestValueMelds) {
			totalValue += m.getValue();
		}
		if (totalValue >= 30) {
			playMeldsToTable(highestValueMelds, true);
			highestValueMelds.forEach(meld -> meld.getMeld().forEach(tile -> hand.removeTile(tile)));
			return true;
		}
		return false;
	}

	//does some setup work then calls allTilePlay to find a way to play all tiles to table, if such a way exists.
	//in that case it performs that play
	boolean attemptToPlayAllTiles() {
		if (hand.hasJoker()){
			return false;
		}
		List<ObservableTile> tiles = new ArrayList<ObservableTile>();
		game.getTable().forEach(observableMeld -> tiles.addAll(observableMeld.getMeld()));
		tiles.addAll(hand.getTiles());

		List<List<List<ObservableTile>>> tilesByRank = tilesByRank(tiles);

		Hand setTiles = new Hand();
		Hand runTiles = new Hand();

		int[][] runState = {{0, 0}, {0, 0}, {0, 0}, {0, 0}};
		if (allTilePlay(1, runState, tilesByRank, setTiles, runTiles)) {
			List<Meld> allMelds = new ArrayList<Meld>();
			allMelds.addAll(findSetsInHand(setTiles));
			allMelds.addAll(findRunsInHand(runTiles));

			playMeldsToTable(allMelds, false);
			hand.clear();
			return true;
		}
		return false;
	}

	private boolean allTilePlay(int rank, int[][] runState, List<List<List<ObservableTile>>> tilesByRank, Hand setTiles, Hand runTiles) {
		int[] redAdditions;
		int[] blueAdditions;
		int[] greenAdditions;
		int[] orangeAdditions;
		if (rank <= 11) {
			redAdditions = findAdditions(runState[0]);
			blueAdditions = findAdditions(runState[1]);
			greenAdditions = findAdditions(runState[2]);
			orangeAdditions = findAdditions(runState[3]);
		} else {
			redAdditions = findAdditionsRank12or13(runState[0]);
			blueAdditions = findAdditionsRank12or13(runState[1]);
			greenAdditions = findAdditionsRank12or13(runState[2]);
			orangeAdditions = findAdditionsRank12or13(runState[3]);
		}
		List<List<ObservableTile>> currentTiles = tilesByRank.get(rank);

		for (int r : redAdditions) {
			int redTilesRemaining = currentTiles.get(0).size() - r;
			if (redTilesRemaining < 0) {
				continue;
			}
			for (int b : blueAdditions) {
				int blueTilesRemaining = currentTiles.get(1).size() - b;
				if (blueTilesRemaining < 0) {
					continue;
				}
				for (int g : greenAdditions) {
					int greenTilesRemaining = currentTiles.get(2).size() - g;
					if (greenTilesRemaining < 0) {
						continue;
					}
					for (int o : orangeAdditions) {
						int orangeTilesRemaining = currentTiles.get(3).size() - o;
						if (orangeTilesRemaining < 0) {
							continue;
						}
						if (!canFormSetsUsingAllTiles(redTilesRemaining, blueTilesRemaining, greenTilesRemaining, orangeTilesRemaining)) {
							continue;
						}
						int[] additions = {r, b, g, o};

						if (rank == 13 || allTilePlay(rank + 1, addStateAndAddition(runState, additions), tilesByRank, setTiles, runTiles)) {
							for (int i = 0; i < 4; ++i) {
								for (int j = 0; j < additions[i]; ++j) {
									runTiles.getUnsortedTiles().add(currentTiles.get(i).remove(0));
								}
							}
							currentTiles.forEach(tiles -> setTiles.getUnsortedTiles().addAll(tiles));
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	//allTilePlay helper
	private int[] findAdditions(int[] suitRunState) {
		if (suitRunState[1] == 0 || suitRunState[0] >= 3 || (suitRunState[0] == 0 && suitRunState[1] >= 3)) {
			return new int[]{0, 1, 2};
		}
		if (suitRunState[0] >= 1 && suitRunState[1] <= 2) {
			return new int[]{2};
		}
		return new int[]{1, 2};
	}

	//allTilePlay helper
	private int[] findAdditionsRank12or13(int[] suitRunState) {
		if (suitRunState[0] == 0) {
			if (suitRunState[1] == 0) {
				return new int[]{0};
			}
			if (suitRunState[1] <= 2) {
				return new int[]{1};
			}
			return new int[]{0, 1};
		}
		return findAdditions(suitRunState);
	}

	//allTilePlay helper
	private int[][] addStateAndAddition(int[][] runState, int[] additions) {
		int[][] result = new int[4][];
		for (int i = 0; i < 4; ++i) {
			result[i] = runState[i].clone();
			if (additions[i] == 2) {
				if (result[i][0] <= 2) {
					++result[i][0];
				}
				;
				if (result[i][1] <= 2) {
					++result[i][1];
				}
			} else if (additions[i] == 1) {
				if (result[i][0] == 1 || result[i][0] == 2) {
					result[i][1] = result[i][0] + 1;
					result[i][0] = 0;
				} else {
					if (result[i][1] <= 2) {
						++result[i][1];
					}
					result[i][0] = 0;
				}
			} else {
				result[i][0] = 0;
				result[i][1] = 0;
			}
		}
		return result;
	}

	private boolean canFormSetsUsingAllTiles(int numRedTiles, int numblueTiles, int numgreenTiles, int numOrangeTiles) {
		int numTiles = numRedTiles + numblueTiles + numgreenTiles + numOrangeTiles;
		if (numTiles == 1 || numTiles == 2 || numTiles == 5) {
			return false;
		}
		if ((numTiles == 3 || numTiles == 4) && (numRedTiles == 2 || numblueTiles == 2 || numgreenTiles == 2 || numOrangeTiles == 2)) {
			return false;
		}
		return true;
	}


	//play conservative && playLiberal
	boolean playConservative() {
		List<Meld> highestValueMelds = highestValueMelds(hand);
		List<ObservableTile> tilesNotInMelds = new ArrayList<ObservableTile>(hand.getTiles());
		highestValueMelds.forEach(meld -> meld.getMeld().forEach(tilesNotInMelds::remove));
		return useBoardMelds(tilesNotInMelds);
	}

	boolean playLiberal() {
		List<Meld> highestValueMelds = highestValueMelds(hand);
		playMeldsToTable(highestValueMelds, true);
		highestValueMelds.forEach(meld -> meld.getMeld().forEach(hand::removeTile));
		return useBoardMelds(new ArrayList<ObservableTile>(hand.getTiles())) || !highestValueMelds.isEmpty();
	}

	//TODO: helper method useBoardMelds for playConservative and playLiberal
	//playableTiles is a list of tiles from the players hand that may be used for board reuse
	//note playableTiles is not the same as the players hands list of tiles, so removing
	//from playableTiles doesn't change the hand. Tiles must be removed from hand manually
	private boolean useBoardMelds(List<ObservableTile> playableTiles) {
		List<Meld> tableMelds = new ArrayList<Meld>();
		for (Meld meld : game.getTable()) {
			Meld copy = new Meld();
			meld.getMeld().forEach(copy::addLastTile);
			tableMelds.add(copy);
		}

		List<ObservableTile> tilesToRemove = new ArrayList<ObservableTile>();

		for (Meld meld : tableMelds) {
			boolean tilesRemovedThisIteration = true;
			while (tilesRemovedThisIteration) {
				tilesRemovedThisIteration = false;
				for (ObservableTile tile : playableTiles) {
					if (meld.addLastTile(tile) || meld.addFirstTile(tile)) {
						tilesToRemove.add(tile);
						tilesRemovedThisIteration = true;
					}
				}
				playableTiles.removeAll(tilesToRemove);
			}
		}

		List<Meld> meldsToAdd = new ArrayList<Meld>();

		for (Meld meld : tableMelds) {
			if (meld.getType() == Meld.Types.RUN && meld.getSize() >= 5 && !meld.hasJoker()) {
				for (ObservableTile tile : playableTiles) {
					int tileRank = tile.getRank();
					if (tile.getColour() == meld.getMeld().get(0).getColour()
							&& tileRank >= meld.getMeld().get(0).getRank() + 2
							&& tileRank <= meld.getMeld().get(meld.getSize() - 1).getRank() - 2) {
						Meld meldToAdd = new Meld();
						int numTilesOfLowerRank = tileRank - meld.getMeld().get(0).getRank();
						for (int i = 0; i < numTilesOfLowerRank; ++i) {
							meldToAdd.addLastTile(meld.getMeld().get(0));
							meld.removeFirstTile();
						}
						meldToAdd.addLastTile(tile);
						meldsToAdd.add(meldToAdd);
						tilesToRemove.add(tile);

						System.out.println(meldToAdd);

						break;
					}
				}
				playableTiles.removeAll(tilesToRemove);
			}
		}

		List<List<List<ObservableTile>>> tilesByRank = tilesByRank(playableTiles);

		for (Meld meld : tableMelds) {
			if (meld.getType() == Meld.Types.SET && meld.getSize() == 4 && !meld.hasJoker()) {
				int rank = meld.getMeld().get(0).getRank();
				for (ObservableTile setTile : meld.getMeld()) {
					int colourNum = setTile.getColour().ordinal();
					List<ObservableTile> rankMinus2 = new ArrayList<ObservableTile>();
					List<ObservableTile> rankMinus1 = new ArrayList<ObservableTile>();
					List<ObservableTile> rankPlus1 = new ArrayList<ObservableTile>();
					List<ObservableTile> rankPlus2 = new ArrayList<ObservableTile>();
					if (rank >= 2) {
						rankMinus1 = tilesByRank.get(rank - 1).get(colourNum);
						if (rank >= 3) {
							rankMinus2 = tilesByRank.get(rank - 2).get(colourNum);
						}
					}
					boolean success = true;
					if (rank <= 12) {
						rankPlus1 = tilesByRank.get(rank + 1).get(colourNum);
						if (rank <= 11) {
							rankPlus2 = tilesByRank.get(rank + 2).get(colourNum);
						}
					}
					if ((!rankMinus2.isEmpty()) && (!rankMinus1.isEmpty())) {
						Meld meldToAdd = new Meld();
						meldToAdd.addLastTile(rankMinus2.get(0));
						tilesToRemove.add(rankMinus2.remove(0));
						meldToAdd.addLastTile(rankMinus1.get(0));
						tilesToRemove.add(rankMinus1.remove(0));
						meldToAdd.addLastTile(setTile);
						meldsToAdd.add(meldToAdd);

					} else if ((!rankMinus1.isEmpty()) && (!rankPlus1.isEmpty())) {
						Meld meldToAdd = new Meld();
						meldToAdd.addLastTile(rankMinus1.get(0));
						tilesToRemove.add(rankMinus1.remove(0));
						meldToAdd.addLastTile(setTile);
						meldToAdd.addLastTile(rankPlus1.get(0));
						tilesToRemove.add(rankPlus1.remove(0));
						meldsToAdd.add(meldToAdd);

					} else if ((!rankPlus1.isEmpty()) && (!rankPlus2.isEmpty())) {
						Meld meldToAdd = new Meld();
						meldToAdd.addLastTile(setTile);
						meldToAdd.addLastTile(rankPlus1.get(0));
						tilesToRemove.add(rankPlus1.remove(0));
						meldToAdd.addLastTile(rankMinus1.get(0));
						tilesToRemove.add(rankMinus1.remove(0));
						meldsToAdd.add(meldToAdd);

					} else {
						success = false;
					}
					if (success) {
						List<ObservableTile> holdingList = new ArrayList<ObservableTile>();
						meld.getMeld().forEach(tile -> {
							if (tile != setTile) {
								holdingList.add(tile);
							}
						});
						meld.getMeld().clear();
						holdingList.forEach(meld::addLastTile);
						break;
					}
				}
			}
		}

		tableMelds.addAll(meldsToAdd);

		if (!tilesToRemove.isEmpty()) {
			playMeldsToTable(tableMelds, false);
			tilesToRemove.forEach(hand::removeTile);
		}

		return !tilesToRemove.isEmpty();
	}

	//finds a combination of melds in a hand with maximal total value
	List<Meld> highestValueMelds(Hand hand) {
		List<List<List<ObservableTile>>> tilesByRank = tilesByRank(hand.getTiles());

		List<Map<Integer, MaxValAndAdditions>> optimalPathLog = new ArrayList<Map<Integer, MaxValAndAdditions>>();
		//dummy
		optimalPathLog.add(null);
		for (int i = 1; i <= 13; ++i) {
			optimalPathLog.add(new HashMap<Integer, MaxValAndAdditions>());
		}

		int[][] runState = {{0, 0}, {0, 0}, {0, 0}, {0, 0}};
		highestValueMeldsTemplate(1, runState, tilesByRank, optimalPathLog);

		return produceMeldsFromRunTileNumbers(tilesByRank, runTileNumbers(optimalPathLog));
	}

	//TODO: implement highestValueMeldsTemplate
	private void highestValueMeldsTemplate(int rank, int[][] runState, List<List<List<ObservableTile>>> tilesByRank, List<Map<Integer, MaxValAndAdditions>> optimalPathLog) {
		int[][] redAdditions;
		int[][] blueAdditions;
		int[][] greenAdditions;
		int[][] orangeAdditions;
		if (rank <= 11) {
			redAdditions = findAdditionsFreely(runState[0]);
			blueAdditions = findAdditionsFreely(runState[1]);
			greenAdditions = findAdditionsFreely(runState[2]);
			orangeAdditions = findAdditionsFreely(runState[3]);
		} else {
			redAdditions = findAdditionsRank12or13freely(runState[0]);
			blueAdditions = findAdditionsRank12or13freely(runState[1]);
			greenAdditions = findAdditionsRank12or13freely(runState[2]);
			orangeAdditions = findAdditionsRank12or13freely(runState[3]);
		}

		List<List<ObservableTile>> currentTiles = tilesByRank.get(rank);

		int maxValue = 0;
		int[][] bestAdditions = null;
		int compressedBestNextRunState = 0;

		for (int[] r : redAdditions) {
			int redTilesRemaining = currentTiles.get(0).size() - (r[0] + r[1]);
			if (redTilesRemaining < 0) {
				continue;
			}
			for (int b[] : blueAdditions) {
				int blueTilesRemaining = currentTiles.get(1).size() - (b[0] + b[1]);
				if (blueTilesRemaining < 0) {
					continue;
				}
				for (int g[] : greenAdditions) {
					int greenTilesRemaining = currentTiles.get(2).size() - (g[0] + g[1]);
					if (greenTilesRemaining < 0) {
						continue;
					}
					for (int o[] : orangeAdditions) {
						int orangeTilesRemaining = currentTiles.get(3).size() - (o[0] + o[1]);
						if (orangeTilesRemaining < 0) {
							continue;
						}

						int[][] additions = {r, b, g, o};
						int[][] nextRunState = addStateAndAddition(runState, additions);
						int compressedNextRunState = compressRunState(nextRunState);

						int nextMaxValue = 0;
						if (rank < 13) {
							MaxValAndAdditions maxValAndAdditions = optimalPathLog.get(rank + 1).get(compressedNextRunState);
							if (maxValAndAdditions == null) {
								highestValueMeldsTemplate(rank + 1, nextRunState, tilesByRank, optimalPathLog);
								maxValAndAdditions = optimalPathLog.get(rank + 1).get(compressedNextRunState);
							}
							nextMaxValue = maxValAndAdditions.maxValue;
						}

						int value = numSetTiles(redTilesRemaining, blueTilesRemaining, greenTilesRemaining, orangeTilesRemaining) * rank + valueFromNewRuns(rank, runState, additions) + nextMaxValue;
						if (value >= maxValue) {
							maxValue = value;
							bestAdditions = additions;
							compressedBestNextRunState = compressedNextRunState;
						}
					}
				}
			}
		}
		optimalPathLog.get(rank).put(compressRunState(runState), new MaxValAndAdditions(maxValue, bestAdditions, compressedBestNextRunState));
	}

	private Integer compressRunState(int[][] runState) {
		int result = 0;
		for (int i = 0; i < 4; ++i) {
			for (int j = 0; j < 2; ++j) {
				result += runState[i][j] * Math.pow(10, 2 * i + j);
			}
		}
		return result;
	}

	private class MaxValAndAdditions {
		int maxValue;
		int[][] additions;
		int compressedNextRunState;

		MaxValAndAdditions(int maxV, int[][] adds, int nextR) {
			maxValue = maxV;
			additions = adds;
			compressedNextRunState = nextR;
		}
	}

	/*
	      0       1       2        3
	0   [0,1]   [0,1]   [0,1]    [0,1]
	1     -     [0,1]   [0,1]    both
	2     -       -     [0,1]    [1,0]
	3     -       -       -      [0,1]
	 */
	private int[][] findAdditionsFreely(int[] suitRunState) {
		if (suitRunState[1] >= 3) {
			if (suitRunState[0] == 2) {
				return new int[][]{{0, 0}, {1, 0}, {1, 1}};
			}
			if (suitRunState[0] == 1) {
				return new int[][]{{0, 0}, {1, 0}, {0, 1}, {1, 1}};
			}
		}
		return new int[][]{{0, 0}, {0, 1}, {1, 1}};
	}

	/*
	       0          1              2              3
	0   [[0,0]]  [[0,0][0,1]]  [[0,0][0,1]]    [[0,0][0,1]]
	1     -         prev            prev           prev
	2     -          -              prev           prev
	3     -          -               -             prev
	 */
	private int[][] findAdditionsRank12or13freely(int[] suitRunState) {
		if (suitRunState[0] == 0) {
			if (suitRunState[1] == 0) {
				return new int[][]{{0, 0}};
			}
			return new int[][]{{0, 0}, {0, 1}};
		}
		return findAdditionsFreely(suitRunState);
	}

	private int[][] addStateAndAddition(int[][] runState, int[][] additions) {
		int[][] result = new int[4][];
		for (int i = 0; i < 4; ++i) {
			result[i] = runState[i].clone();
			if (Arrays.equals(additions[i], new int[]{1, 0})) {
				result[i][1] = result[i][0] + (result[i][0] <= 2 ? 1 : 0);
				result[i][0] = 0;
			} else {
				for (int j = 0; j < 2; ++j) {
					if (additions[i][j] == 0) {
						result[i][j] = 0;
					} else {
						result[i][j] = result[i][j] + (result[i][j] <= 2 ? 1 : 0);
					}
				}
			}
		}
		return result;
	}

	private int numSetTiles(int numRedTiles, int numblueTiles, int numgreenTiles, int numOrangeTiles) {
		int numTiles = numRedTiles + numblueTiles + numgreenTiles + numOrangeTiles;
		if (numTiles <= 2) {
			return 0;
		}
		int[] tileNumFrequency = new int[3];
		int[] numTileArray = {numRedTiles, numblueTiles, numgreenTiles, numOrangeTiles};
		for (int i : numTileArray) {
			++tileNumFrequency[i];
		}
		if (numTiles == 3) {
			if (tileNumFrequency[2] == 0) {
				return 3;
			}
			return 0;
		}
		if (numTiles == 4) {
			if (tileNumFrequency[2] == 0) {
				return 4;
			}
			if (tileNumFrequency[2] == 1) {
				return 3;
			}
			return 0;
		}
		if (numTiles == 5) {
			if (tileNumFrequency[2] == 1) {
				return 4;
			}
			return 3;
		}
		return numTiles;
	}

	private int valueFromNewRuns(int rank, int[][] runState, int[][] additions) {
		int total = 0;
		for (int i = 0; i < 4; ++i) {
			for (int j = 0; j < 2; ++j) {
				if (additions[i][j] == 1) {
					if (runState[i][j] == 3) {
						total += rank;
					} else if (runState[i][j] == 2) {
						total += rank * 3 - 3;
					}
				}
			}
		}
		return total;
	}

	private List<Meld> produceMeldsFromRunTileNumbers(List<List<List<ObservableTile>>> tilesByRank, int[][] runTileNumbers) {
		List<Meld> allMelds = new ArrayList<Meld>();

		Hand setTiles = new Hand();
		Hand runTiles = new Hand();
		for (int i = 1; i <= 13; ++i) {
			for (int j = 0; j < 4; ++j) {
				for (int n = 0; n < runTileNumbers[i][j]; ++n) {
					runTiles.addTile(tilesByRank.get(i).get(j).remove(0));
				}
				tilesByRank.get(i).get(j).forEach(setTiles::addTile);
			}
		}
		allMelds.addAll(findSetsInHand(setTiles));
		allMelds.addAll(findRunsInHand(runTiles));
		return allMelds;
	}

	private int[][] runTileNumbers(List<Map<Integer, MaxValAndAdditions>> optimalPathLog) {
		int[][] runTileNumbers = new int[14][4];
		int compressedNextRunState = 0;
		for (int i = 1; i <= 13; ++i) {
			MaxValAndAdditions m = optimalPathLog.get(i).get(compressedNextRunState);
			for (int j = 0; j < 4; ++j) {
				runTileNumbers[i][j] = m.additions[j][0] + m.additions[j][1];
			}
			compressedNextRunState = m.compressedNextRunState;
		}
		return runTileNumbers;
	}

	//Run and Set finders
	List<Meld> findRunsInHand(Hand hand) {
		List<Meld> melds = new ArrayList<Meld>();
		List<List<List<ObservableTile>>> tilesByColour = new ArrayList<List<List<ObservableTile>>>();
		for (int i = 0; i < 4; ++i) {
			tilesByColour.add(new ArrayList<>());
			//dummy in position 0
			tilesByColour.get(i).add(new ArrayList<>());
			for (int j = 1; j <= 13; ++j) {
				tilesByColour.get(i).add(new ArrayList<ObservableTile>());
			}
		}
		for (ObservableTile tile : hand.getTiles()) {
			tilesByColour.get(tile.getColour().ordinal()).get(tile.getRank()).add(tile);
		}
		for (int i = 0; i < 4; ++i) {
			List<List<ObservableTile>> tilesOfColour = tilesByColour.get(i);
			melds.addAll(formRunsOfASingleColour(tilesOfColour));
		}
		return melds;
	}

	private List<Meld> formRunsOfASingleColour(List<List<ObservableTile>> tiles) {
		List<Meld> result = new ArrayList<Meld>();
		Meld meld1 = new Meld();
		Meld meld2 = new Meld();
		for (int i = 1; i <= 13; ++i) {
			int numTilesOfRank = tiles.get(i).size();
			if (numTilesOfRank == 0) {
				if (meld1.isValidLength()) {
					result.add(meld1);
				}
				if (meld2.isValidLength()) {
					result.add(meld2);
				}
				meld1 = new Meld();
				meld2 = new Meld();
			} else if (numTilesOfRank == 1) {
				if (meld1.getSize() == meld2.getSize()) {
					meld1.addLastTile(tiles.get(i).get(0));
					if (meld2.isValidLength()) {
						result.add(meld2);
					}
					meld2 = new Meld();
				} else if (meld1.getSize() == 0) {
					meld2.addLastTile(tiles.get(i).get(0));
				} else if (meld2.getSize() == 0) {
					meld1.addLastTile(tiles.get(i).get(0));
				} else if (meld1.getSize() == 2) {
					meld1.addLastTile(tiles.get(i).get(0));
					if (meld2.isValidLength()) {
						result.add(meld2);
					}
					meld2 = new Meld();
				} else if (meld2.getSize() == 2) {
					meld2.addLastTile(tiles.get(i).get(0));
					if (meld1.isValidLength()) {
						result.add(meld1);
					}
					meld1 = new Meld();
				}
				//one meld has a single tile, the other has at least 3
				else if (meld1.getSize() == 1) {
					if (i < 13 && tiles.get(i + 1).size() > 0) {
						meld1.addLastTile(tiles.get(i).get(0));
						result.add(meld2);
						meld2 = new Meld();
					} else {
						meld2.addLastTile(tiles.get(i).get(0));
						meld1 = new Meld();
					}
				} else if (meld2.getSize() == 1) {
					if (i < 13 && tiles.get(i + 1).size() > 0) {
						meld2.addLastTile(tiles.get(i).get(0));
						result.add(meld1);
						meld1 = new Meld();
					} else {
						meld1.addLastTile(tiles.get(i).get(0));
						meld2 = new Meld();
					}
				}
			} else {
				meld1.addLastTile(tiles.get(i).get(0));
				meld2.addLastTile(tiles.get(i).get(1));
			}
			if (i == 13) {
				if (meld1.isValidLength()) {
					result.add(meld1);
				}
				if (meld2.isValidLength()) {
					result.add(meld2);
				}
			}
		}
		return result;
	}

	List<Meld> findSetsInHand(Hand hand) {
		List<Meld> melds = new ArrayList<Meld>();
		List<List<List<ObservableTile>>> tilesByRank = tilesByRank(hand.getTiles());
		for (int i = 1; i <= 13; ++i) {
			List<List<ObservableTile>> tilesOfRank = tilesByRank.get(i);
			melds.addAll(formSetsOfASingleRank(tilesOfRank));
		}
		return melds;
	}

	private List<Meld> formSetsOfASingleRank(List<List<ObservableTile>> tiles) {
		List<Meld> result = new ArrayList<Meld>();
		int numRedTiles = tiles.get(0).size();
		int numblueTiles = tiles.get(1).size();
		int numgreenTiles = tiles.get(2).size();
		int numOrangeTiles = tiles.get(3).size();
		int numTiles = numRedTiles + numblueTiles + numgreenTiles + numOrangeTiles;
		if (numTiles <= 2) {
			return result;
		}
		if (numTiles <= 5) {
			Meld meld = new Meld();
			for (List<ObservableTile> l : tiles) {
				if (!l.isEmpty()) {
					meld.addLastTile(l.remove(0));
				}
			}
			if (meld.isValidLength()) {
				result.add(meld);
			}
			return result;
		}
		//6 or more tiles
		result.add(new Meld());
		result.add(new Meld());
		for (List<ObservableTile> l : tiles) {
			if (!l.isEmpty()) {
				result.get(0).addLastTile(l.remove(0));
				if (!l.isEmpty()) {
					result.get(1).addLastTile(l.remove(0));
				}
			}
		}
		//if there were 6 tiles and 2 of them were isolates (didn't have a duplicate tile), result.get(1) will only have 2 tiles
		if (!result.get(1).isValidLength()) {
			List<ObservableTile> firstMeldTiles = result.get(0).getMeld();
			for (int i = 0; i < 4; ++i) {
				if (result.get(1).addLastTile(firstMeldTiles.get(i))) {
					firstMeldTiles.remove(i);
					result.set(0, new Meld());
					firstMeldTiles.forEach(t -> result.get(0).addLastTile(t));
					break;
				}
			}
		}
		return result;
	}

	private List<List<List<ObservableTile>>> tilesByRank(List<ObservableTile> tiles) {
		List<List<List<ObservableTile>>> tilesByRank = new ArrayList<List<List<ObservableTile>>>();
		//dummy in position 0
		tilesByRank.add(null);
		for (int i = 1; i <= 13; ++i) {
			tilesByRank.add(new ArrayList<>());
			for (int j = 0; j < 4; ++j) {
				tilesByRank.get(i).add(new ArrayList<ObservableTile>());
			}
		}

		for (ObservableTile tile : tiles) {
			if(!tile.isJoker()){
				tilesByRank.get(tile.getRank()).get(tile.getColour().ordinal()).add(tile);
			}
		}
		return tilesByRank;
	}

	void playMeldsToTable(List<Meld> melds, boolean keepExistingMelds) {
		int i = 0;
		int j = 0;
		if (keepExistingMelds) {
			game.getTable().forEach(observableMeld -> melds.add((Meld) observableMeld));
		}

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