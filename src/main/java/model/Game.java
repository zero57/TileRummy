package model;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Game {
	private Stock stock;
	private Hand player1Hand;
	private Hand player2Hand;
	private Hand player3Hand;
	private Hand player4Hand;

	private BooleanBinding isNPCTurn;
	private IntegerProperty playerTurn;

	public Game() {
		stock = new Stock();
		stock.shuffle();

		player1Hand = new Hand();
		player2Hand = new Hand();
		player3Hand = new Hand();
		player4Hand = new Hand();

		playerTurn = new SimpleIntegerProperty(0);
		isNPCTurn = playerTurn.greaterThanOrEqualTo(1);
	}

	public void dealInitialTiles() {
		for (int i = 0; i < 14; i++) {
			player1Hand.addTile(drawTile());
			player2Hand.addTile(drawTile());
			player3Hand.addTile(drawTile());
			player4Hand.addTile(drawTile());
		}
	}

	public Tile drawTile() {
		return stock.draw();
	}

	public Hand getCurrentPlayerhand() {
		switch (getPlayerTurn()) {
			case 0: return player1Hand;
			case 1: return player2Hand;
			case 2: return player3Hand;
			case 3: return player4Hand;	
		}
		return null;
	}

	public Hand getPlayer1Hand() {
		return player1Hand;
	}

	public Hand getPlayer2Hand() {
		return player2Hand;
	}

	public Hand getPlayer3Hand() {
		return player3Hand;
	}

	public Hand getPlayer4Hand() {
		return player4Hand;
	}

	public int getStockSize() {
		return stock.getSize();
	}

	public void endTurn() {
		playerTurn.set((playerTurn.getValue()+1) % 4);
	}

	public void drawTurn(Hand hand) {
		hand.addTile(drawTile());
		endTurn();
	}

	public BooleanBinding getNPCTurn() {
		return isNPCTurn;
	}

	private int getPlayerTurn() { return playerTurn.get(); }
	private IntegerProperty getPlayerTurnProperty() { return playerTurn; }
}
