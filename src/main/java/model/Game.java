package model;

public class Game {
	private Stock stock;
	private Hand player1Hand;
	private Hand player2Hand;
	private Hand player3Hand;
	private Hand player4Hand;

	public Game() {
		stock = new Stock();
		stock.shuffle();

		player1Hand = new Hand();
		player2Hand = new Hand();
		player3Hand = new Hand();
		player4Hand = new Hand();
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
}