package controller;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Objects;

public class MainController {

	private static final Logger logger = LogManager.getLogger(MainController.class.getName());

	@FXML
	private BorderPane bpPlayScreen;

	@FXML
	private HBox hboxMenuBar;

	@FXML
	private HBox hboxCenter;

	@FXML
	private JFXButton btnEndTurn;

	@FXML
	private JFXButton btnDrawTile;

	@FXML
	public void initialize() {
		logger.info("Initializing Main view");
		Parent tableView = null;
		FlowPane player1HandView = null;
		FlowPane player2HandView = null;
		FlowPane player3HandView = null;
		FlowPane player4HandView = null;

		try {
			tableView = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("view/TableView.fxml")));
			player1HandView = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("view/HandView.fxml")));
			player2HandView = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("view/HandView.fxml")));
			player3HandView = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("view/HandView.fxml")));
			player4HandView = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("view/HandView.fxml")));
		} catch (IOException e) {
			logger.error("Failed to load fxml files", e);
		}

		player1HandView.setPrefHeight(48);
		player3HandView.setPrefHeight(48);
		player2HandView.setPrefWidth(48);
		player4HandView.setPrefWidth(48);
		player2HandView.setOrientation(Orientation.VERTICAL);
		player4HandView.setOrientation(Orientation.VERTICAL);
		bpPlayScreen.setBottom(player1HandView);
		bpPlayScreen.setLeft(player2HandView);
		bpPlayScreen.setTop(player3HandView);
		bpPlayScreen.setRight(player4HandView);
		hboxCenter.getChildren().add(0, tableView);
	}
}
