package model;

import model.player.HumanPlayer;
import model.player.Player;
import model.player.ai.AIPlayer;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.observable.ObservableMeld;
import model.observable.ObservableTile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Optional;

public class Game {
	private static final Logger logger = LogManager.getLogger(Game.class.getName());

	private Stock stock;
	private ObservableList<ObservableMeld> table;
	private ArrayList<Player> players;
	private ArrayList<Hand> hands;

	private BooleanBinding isNPCTurn;
	private IntegerProperty playerTurn;
	private IntegerProperty winner;
	private AIPlayer ai1;
	private AIPlayer ai2;
	private AIPlayer ai3;
	private int numPlayers;

	// Note: you must call setStock since we do not do it automatically here
	// This is because we need to be able to set up the game for integration testing
	public Game() {
		// If no num of players are passed in, we default to 4 players
		this(new OptionChoices()
				.setNumPlayers(4)
				.setPlayer1(OptionChoices.Type.HUMAN.ordinal())
				.setPlayer2(OptionChoices.Type.AI1.ordinal())
				.setPlayer3(OptionChoices.Type.AI2.ordinal())
				.setPlayer4(OptionChoices.Type.AI3.ordinal()));
	}

	public Game(OptionChoices optionChoices) {
		table = FXCollections.observableArrayList();
		winner = new SimpleIntegerProperty(-1);
		numPlayers = optionChoices.getNumPlayers();
		players = new ArrayList<>();

		hands = new ArrayList<>();
		for (int i = 0; i < numPlayers; i++) {
			final int finalI = i;
			Hand hand = new Hand();
			hands.add(hand);
			hand.getSizeProperty().addListener((observableValue, oldVal, newVal) -> {
				if (newVal.intValue() == 0 && allMeldsValid()) {
					logger.info("Player " + (finalI + 1) + " is the winner!");
					winner.setValue(finalI + 1);
				}
			});
			if (i == 0) {
				if (optionChoices.getPlayer1() == OptionChoices.Type.HUMAN) {
					Player player = new HumanPlayer(this, hand);
					players.add(player);
				} else {
					Player player = new AIPlayer(optionChoices.getPlayer1().ordinal(), this, hand);
					players.add(player);
				}
			}
			if (i == 1) {
				if (optionChoices.getPlayer2() == OptionChoices.Type.HUMAN) {
					Player player = new HumanPlayer(this, hand);
					players.add(player);
				} else {
					Player player = new AIPlayer(optionChoices.getPlayer2().ordinal(), this, hand);
					players.add(player);
				}
			}
			if (i == 2) {
				if (optionChoices.getPlayer3() == OptionChoices.Type.HUMAN) {
					Player player = new HumanPlayer(this, hand);
					players.add(player);
				} else {
					Player player = new AIPlayer(optionChoices.getPlayer3().ordinal(), this, hand);
					players.add(player);
				}
			}
			if (i == 3) {
				if (optionChoices.getPlayer4() == OptionChoices.Type.HUMAN) {
					Player player = new HumanPlayer(this, hand);
					players.add(player);
				} else {
					Player player = new AIPlayer(optionChoices.getPlayer4().ordinal(), this, hand);
					players.add(player);
				}
			}
		}

		playerTurn = new SimpleIntegerProperty(0);
		isNPCTurn = Bindings.createBooleanBinding(() -> players.get(getPlayerTurn()) instanceof AIPlayer, playerTurn);
	}

	public void update() {
		players.get(playerTurn.get()).playTurn();
	}

	public void setStock(Stock stock) {
		this.stock = stock;
	}

	public void resetGame() {
		table.clear();
		winner.setValue(-1);
		playerTurn.setValue(0);
		for (Hand hand : hands) {
			hand.clear();
		}
	}

	public void dealInitialTiles() {
		for (Hand hand : hands) {
			for (int i = 0; i < 14; i++) {
				drawTile().ifPresent(hand::addTile);
			}
		}
	}

	public IntegerProperty getWinnerProperty() {
		return winner;
	}

	public int getNumPlayers() {
		return numPlayers;
	}

	public Optional<ObservableTile> drawTile() {
		return stock.draw();
	}

	public Hand getCurrentPlayerHand() {
		return hands.get(getPlayerTurn());
	}

	public Hand getPlayer1Hand() {
		return hands.get(0);
	}

	public Hand getPlayer2Hand() {
		return hands.get(1);
	}

	public Hand getPlayer3Hand() {
		return hands.get(2);
	}

	public Hand getPlayer4Hand() {
		return hands.get(3);
	}

	public int getStockSize() {
		return stock.getSize();
	}

	public void endTurn(Hand hand) {
		if (!allMeldsValid()) {
			return;
		}

		if (noTilesAddedThisTurn()) {
			drawTile().ifPresent(hand::addTile);
		}
		playAllTiles();
		playerTurn.setValue((playerTurn.getValue() + 1) % numPlayers);
	}

	private boolean allMeldsValid() {
		for (Meld meld : table) {
			if (!meld.isValidLength()) {
				return false;
			}
		}
		return true;
	}

	private boolean noTilesAddedThisTurn() {
		for (Meld meld : table) {
			for (ObservableTile tile : meld.getMeld()) {
				//if a tile is unplayed, that means tiles have been added to the table this turn
				if (!tile.hasBeenPlayed()) {
					return false;
				}
			}
		}
		return true;
	}

	private void playAllTiles() {
		table.forEach(meld -> meld.getMeld().forEach(ObservableTile::play));
	}

	public BooleanBinding getNPCTurn() {
		return isNPCTurn;
	}

	public int getPlayerTurn() {
		return playerTurn.getValue();
	}

	public IntegerProperty getPlayerTurnProperty() {
		return playerTurn;
	}

	public ObservableList<ObservableMeld> getTable() {
		return table;
	}

	public boolean removeTileFromTable(ObservableTile tile, int row, int col) {
		for (ObservableMeld meld : table) {
			// Assume that there exists at least one tile in the Meld, as we do not allow empty melds to exist on the table
			ObservableTile firstTile = meld.getMeld().get(0);
			ObservableTile lastTile = meld.getMeld().get(meld.getSize() - 1);

			// In the same row
			if (row == meld.getRow()) {
				// Either at the beginning or end of list
				if (col == meld.getCol()) {
					if (firstTile.getRank() == tile.getRank() && firstTile.getColour().equals(tile.getColour())) {
						meld.removeFirstTile();
						// If we remove the beginning of the meld, the old starting column is out of date, so we update it
						meld.setCol(meld.getCol() + 1);
						// Remove empty melds from table
						if (meld.getSize() <= 0) {
							table.remove(meld);
						}
						return true;
					}
				} else if (col == meld.getCol() + meld.getSize() - 1) {
					if (lastTile.getRank() == tile.getRank() && lastTile.getColour().equals(tile.getColour())) {
						meld.removeLastTile();
						// Remove empty melds from table
						if (meld.getSize() <= 0) {
							table.remove(meld);
						}
						return true;
					}
				}
			}
		}

		return false;
	}

	private boolean meldExistsOnTheLeftAndRightOfCell(int row, int col) {
		boolean meldOnLeft = false;
		boolean meldOnRight = false;
		for (ObservableMeld meld : table) {
			if (meld.getRow() == row) {
				// A meld exists on the left of the cell
				if (col - 1 == meld.getCol() + meld.getSize() - 1) {
					meldOnLeft = true;
				}
				// A meld exists on the right of the cell
				if (col + 1 == meld.getCol()) {
					meldOnRight = true;
				}
			}
		}
		return meldOnLeft && meldOnRight;
	}

	public boolean addTileToTable(ObservableTile tile, int row, int col) {
		boolean success = false;

		if (meldExistsOnTheLeftAndRightOfCell(row, col)) {
			return false;
		}

		// Try to find existing meld to add tile to
		for (ObservableMeld meld : table) {
			// within bounds
			if (row == meld.getRow() && col >= meld.getCol() - 1 && col <= meld.getCol() + meld.getSize()) {
				// Only allow adding to tail ends
				if (col == meld.getCol() - 1) {
					success = meld.addFirstTile(tile);
					if (success) {
						// If we add to the beginning of the meld, the old starting column is out of date, so we update it
						meld.setCol(meld.getCol() - 1);
					}
				} else if (col == meld.getCol() + meld.getSize()) {
					success = meld.addLastTile(tile);
				}
				return success;
			}
		}

		// Otherwise, create new meld
		ObservableMeld meld = new ObservableMeld(row, col);
		if (meld.addFirstTile(tile)) {
			table.add(meld);
			return true;
		}
		return false;
	}
}
