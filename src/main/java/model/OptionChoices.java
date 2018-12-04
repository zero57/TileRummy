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
	private boolean showStartDialog;

	private String handRigFilePath = "";

	public OptionChoices setNumPlayers(int num) {
		numPlayers = num;
		return this;
	}

	public int getNumPlayers() {
		return numPlayers;
	}

	public OptionChoices setPlayer1(int index) {
		player1 = Arrays.stream(Type.values()).filter(t -> t.ordinal() == index).findFirst().orElse(null);
		return this;
	}

	public void setShowStartDialog(boolean choice) {
		showStartDialog = choice;
	}

	public boolean getShowStartDialog() {
		return showStartDialog;
	}

	public OptionChoices setPlayer2(int index) {
		player2 = Arrays.stream(Type.values()).filter(t -> t.ordinal() == index).findFirst().orElse(null);
		return this;
	}

	public OptionChoices setPlayer3(int index) {
		player3 = Arrays.stream(Type.values()).filter(t -> t.ordinal() == index).findFirst().orElse(null);
		return this;
	}

	public OptionChoices setPlayer4(int index) {
		player4 = Arrays.stream(Type.values()).filter(t -> t.ordinal() == index).findFirst().orElse(null);
		return this;
	}

	public OptionChoices setTimerChecked(boolean value) {
		timerChecked = value;
		return this;
	}

	public boolean getTimerChecked() {
		return timerChecked;
	}

	public OptionChoices setRigHandChecked(boolean value) {
		rigHandChecked = value;
		return this;
	}

	public boolean getRigHandChecked() {
		return rigHandChecked;
	}

	public OptionChoices setRigTileDrawChecked(boolean value) {
		rigTileDrawChecked = value;
		return this;
	}

	public boolean getRigTileDrawChecked() {
		return rigTileDrawChecked;
	}

	public OptionChoices setShowHandsChecked(boolean value) {
		showHandsChecked = value;
		return this;
	}

	public boolean getShowHandsChecked() {
		return showHandsChecked;
	}

	public Type getPlayer(int playerNum) {
		switch (playerNum) {
			case 1:
				return player1;
			case 2:
				return player2;
			case 3:
				return player3;
			case 4:
				return player4;
			default:
				throw new Error("Invalid player number: " + playerNum);
		}
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

	public OptionChoices setHandRigFilePath(String path) {
		handRigFilePath = path;
		return this;
	}

	public String getHandRigFilePath() {
		return handRigFilePath;
	}
}