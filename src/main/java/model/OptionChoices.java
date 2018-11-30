package model;

import java.util.Arrays;

public class OptionChoices {
	public enum Type {
		HUMAN,
		AI1,
		AI2,
		AI3,
		AI4,
	}

	private int numPlayers;
	private Type player1;
	private Type player2;
	private Type player3;
	private Type player4;

	private boolean timerChecked;
	private boolean rigHandChecked;
	private boolean rigTileDrawChecked;
	private boolean showHandsChecked;

	public void setNumPlayers(int num) {
		numPlayers = num;
	}

	public int getNumPlayers() {
		return numPlayers;
	}

	public void setPlayer1(int index) {
		player1 = Arrays.stream(Type.values()).filter(t -> t.ordinal() == index).findFirst().orElse(null);
	}

	public void setPlayer2(int index) {
		player2 = Arrays.stream(Type.values()).filter(t -> t.ordinal() == index).findFirst().orElse(null);
	}

	public void setPlayer3(int index) {
		player3 = Arrays.stream(Type.values()).filter(t -> t.ordinal() == index).findFirst().orElse(null);
	}

	public void setPlayer4(int index) {
		player4 = Arrays.stream(Type.values()).filter(t -> t.ordinal() == index).findFirst().orElse(null);
	}

	public void setTimerChecked(boolean value) {
		timerChecked = value;
	}

	public boolean getTimerChecked() {
		return timerChecked;
	}

	public void setRigHandChecked(boolean value) {
		rigHandChecked = value;
	}

	public boolean getRigHandChecked() {
		return rigHandChecked;
	}

	public void setRigTileDrawChecked(boolean value) {
		rigTileDrawChecked = value;
	}

	public boolean getRigTileDrawChecked() {
		return rigTileDrawChecked;
	}

	public void setShowHandsChecked(boolean value) {
		showHandsChecked = value;
	}

	public boolean getShowHandsChecked() {
		return showHandsChecked;
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