package controller;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import model.Game;
import model.OptionChoices;
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

	private Game game;
	private OptionChoices options;

	private HandController player1HandController;
	private NPCHandController player2HandController;
	private NPCHandController player3HandController;
	private NPCHandController player4HandController;

	public MainController(OptionChoices options) {
		this.options = options;
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
			player1HandController = new HandController(game, 1);
			loader.setControllerFactory(c -> player1HandController);
			player1HandView = loader.load();

			loader = new FXMLLoader(getClass().getClassLoader().getResource("view/HandView.fxml"));
			player2HandController = new NPCHandController(game, 2);
			loader.setControllerFactory(c -> player2HandController);
			player2HandView = loader.load();

			loader = new FXMLLoader(getClass().getClassLoader().getResource("view/HandView.fxml"));
			player3HandController = new NPCHandController(game, 3);
			loader.setControllerFactory(c -> player3HandController);
			player3HandView = loader.load();

			loader = new FXMLLoader(getClass().getClassLoader().getResource("view/HandView.fxml"));
			player4HandController = new NPCHandController(game, 4);
			loader.setControllerFactory(c -> player4HandController);
			player4HandView = loader.load();
		} catch (IOException e) {
			logger.error("Failed to load fxml files", e);
			return;
		}

		HBox.setHgrow(tableView, Priority.ALWAYS);
		bpPlayScreen.setBottom(player1HandView);
		bpPlayScreen.setLeft(player2HandView);
		bpPlayScreen.setTop(player3HandView);
		bpPlayScreen.setRight(player4HandView);
		hboxCenter.getChildren().add(0, tableView);

		btnEndTurn.setOnMouseClicked(b -> {
			Task<Void> sleepTask = new Task<>() {
				@Override
				protected Void call() throws Exception {
					Platform.runLater(() -> game.endTurn(game.getCurrentPlayerhand()));
					Thread.sleep(1000);
					Platform.runLater(() -> game.getAI1().playTurn());
					Thread.sleep(1000);
					Platform.runLater(() -> game.getAI2().playTurn());
					Thread.sleep(1000);
					Platform.runLater(() -> game.getAI3().playTurn());
					Thread.sleep(1000);
					return null;
				}
			};
			new Thread(sleepTask).start();
		});
		btnEndTurn.disableProperty().bind(game.getNPCTurn());
	}

	public Game getGame() {
		return game;
	}

	public Pane getPlayer1HandPane() {
		return player1HandController.getHandPane();
	}

	public Pane getPlayer2HandPane() {
		return player2HandController.getHandPane();
	}

	public Pane getPlayer3HandPane() {
		return player3HandController.getHandPane();
	}

	public Pane getPlayer4HandPane() {
		return player4HandController.getHandPane();
	}
}
