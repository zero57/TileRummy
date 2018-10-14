package controller;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import model.Game;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

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

	private Game game;

	public MainController() {
		game = new Game();
	}

	@FXML
	public void initialize() {
		logger.info("Initializing Main view");

		GridPane tableView;
		VBox player1HandView;
		VBox player2HandView;
		VBox player3HandView;
		VBox player4HandView;

		FXMLLoader loader;

		try {
			loader = new FXMLLoader(getClass().getClassLoader().getResource("view/TableView.fxml"));
			loader.setControllerFactory(c -> new TableController(game));
			tableView = loader.load();

			loader = new FXMLLoader(getClass().getClassLoader().getResource("view/HandView.fxml"));
			loader.setControllerFactory(c -> new HandController(game, 1));
			player1HandView = loader.load();

			loader = new FXMLLoader(getClass().getClassLoader().getResource("view/HandView.fxml"));
			loader.setControllerFactory(c -> new NPCHandController(game, 2));
			player2HandView = loader.load();

			loader = new FXMLLoader(getClass().getClassLoader().getResource("view/HandView.fxml"));
			loader.setControllerFactory(c -> new NPCHandController(game, 3));
			player3HandView = loader.load();

			loader = new FXMLLoader(getClass().getClassLoader().getResource("view/HandView.fxml"));
			loader.setControllerFactory(c -> new NPCHandController(game, 4));
			player4HandView = loader.load();
		} catch (IOException e) {
			logger.error("Failed to load fxml files", e);
			return;
		}

		HBox.setHgrow(tableView, Priority.ALWAYS);
		Platform.runLater(() -> {
			bpPlayScreen.setBottom(player1HandView);
			bpPlayScreen.setLeft(player2HandView);
			bpPlayScreen.setTop(player3HandView);
			bpPlayScreen.setRight(player4HandView);
			hboxCenter.getChildren().add(0, tableView);
		});

		game.dealInitialTiles();
	}
}
