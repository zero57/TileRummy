package model;

public class OptionChoices {
	public enum Type {

	    HUMAN("Human"), AI1("AI1"), AI2("AI2"), AI3("AI3"), AI4("AI4"), NULL("NULL");

	    private String name;

	    private Type(String theType) {
	        this.name = theType;
		}
	}	

	private int numPlayers;
	private Type player1;
	private Type player2;
	private Type player3;
	private Type player4;

	public void setNumPlayers(int num) {
		numPlayers = num;
	}

	public int getNumPlayers() {
		return numPlayers;
	}

	public void setPlayer1(Type type) {
		player1 = type;
	}
	public void setPlayer2(Type type) {
		player2 = type;
	}
	public void setPlayer3(Type type) {
		player3 = type;
	}
	public void setPlayer4(Type type) {
		player4 = type;
	}

	public Type getPlayer1() {
		return player1;
	}

	public Type getPlayer2() {
		return player2;
	}

	public Type getPlayer3() {
		return player3;
	}

	public Type getPlayer4() {
		return player4;
	}
}