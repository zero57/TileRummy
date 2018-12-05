package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDecorator;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Hand;
import model.HandRigTable;
import model.OptionChoices;
import model.Stock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import parser.FileWriter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class HandRigController {

	private static final Logger logger = LogManager.getLogger(HandRigController.class.getName());

	@FXML
	private BorderPane bpPlayScreen;

	@FXML
	private HBox hboxMenuBar;

	@FXML
	private HBox hboxCenter;

	@FXML
	private JFXButton btnRigHands;

	@FXML
	private StackPane root;

	private HandRigTable handRigTable;
	private Hand player1Hand;
	private Hand player2Hand;
	private Hand player3Hand;
	private Hand player4Hand;

	private OptionChoices options;

	private HandRigHandController player1HandController;
	private HandRigHandController player2HandController;
	private HandRigHandController player3HandController;
	private HandRigHandController player4HandController;

	private Stage stage;

	public HandRigController(Stage stage, OptionChoices options) {
		this.stage = stage;
		this.options = options;
		this.handRigTable = new HandRigTable();
		this.player1Hand = new Hand();
		this.player2Hand = new Hand();
		this.player3Hand = new Hand();
		this.player4Hand = new Hand();
	}

	@FXML
	public void initialize() {
		logger.info("Initializing Hand Rig View");

		GridPane tableView;
		VBox player1HandView;
		VBox player2HandView;
		VBox player3HandView = null;
		VBox player4HandView = null;

		FXMLLoader loader;

		try {
			loader = new FXMLLoader(getClass().getClassLoader().getResource("view/HandRigTableView.fxml"));
			loader.setControllerFactory(c -> new HandRigTableController(handRigTable, player1Hand, player2Hand, player3Hand, player4Hand));
			tableView = loader.load();

			loader = new FXMLLoader(getClass().getClassLoader().getResource("view/HandView.fxml"));
			player1HandController = new HandRigHandController(handRigTable, 1, player1Hand);
			loader.setControllerFactory(c -> player1HandController);
			player1HandView = loader.load();

			loader = new FXMLLoader(getClass().getClassLoader().getResource("view/HandView.fxml"));
			player2HandController = new HandRigHandController(handRigTable, 2, player2Hand);
			loader.setControllerFactory(c -> player2HandController);
			player2HandView = loader.load();

			if (options.getNumPlayers() >= 3) {
				loader = new FXMLLoader(getClass().getClassLoader().getResource("view/HandView.fxml"));
				player3HandController = new HandRigHandController(handRigTable, 3, player3Hand);
				loader.setControllerFactory(c -> player3HandController);
				player3HandView = loader.load();
			}

			if (options.getNumPlayers() >= 4) {
				loader = new FXMLLoader(getClass().getClassLoader().getResource("view/HandView.fxml"));
				player4HandController = new HandRigHandController(handRigTable, 4, player4Hand);
				loader.setControllerFactory(c -> player4HandController);
				player4HandView = loader.load();
			}

		} catch (IOException e) {
			logger.error("Failed to load fxml files", e);
			return;
		}

		HBox.setHgrow(tableView, Priority.ALWAYS);
		bpPlayScreen.setBottom(player1HandView);
		bpPlayScreen.setLeft(player2HandView);
		if (options.getNumPlayers() >= 3) {
			bpPlayScreen.setTop(player3HandView);
		}
		if (options.getNumPlayers() >= 4) {
			bpPlayScreen.setRight(player4HandView);
		}
		hboxCenter.getChildren().add(0, tableView);

		btnRigHands.setOnMouseClicked(mouseEvent -> {
			if (player1Hand.getSizeProperty().getValue() != 14 ||
					player2Hand.getSizeProperty().getValue() != 14) {
				return;
			}
			if (options.getNumPlayers() >= 3) {
				if (player3Hand.getSizeProperty().getValue() != 14) {
					return;
				}
			}
			if (options.getNumPlayers() >= 4) {
				if (player4Hand.getSizeProperty().getValue() != 14) {
					return;
				}
			}
			try {
				ArrayList<Hand> hands = new ArrayList<>();
				if (player1Hand.getTiles().size() == 14) {
					hands.add(player1Hand);
				}
				if (player2Hand.getTiles().size() == 14) {
					hands.add(player2Hand);
				}
				if (options.getNumPlayers() >= 3) {
					if (player3Hand.getTiles().size() == 14) {
						hands.add(player3Hand);
					}
				}
				if (options.getNumPlayers() >= 4) {
					if (player4Hand.getTiles().size() == 14) {
						hands.add(player4Hand);
					}
				}
				File tmpFile = FileWriter.getRiggedHandFile(hands);
				options.setHandRigFilePath(tmpFile.getAbsolutePath());
				showMainStage();
			} catch (Exception e) {
				logger.error(e);
			}
		});

	}

	public void showMainStage() throws Exception {
		Platform.setImplicitExit(false);

		Stage mainStage = new Stage();
		MainController mainController = new MainController(options);
		FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("view/MainView.fxml"));
		loader.setControllerFactory(c -> mainController);
		StackPane root = loader.load();

		var decorator = new JFXDecorator(mainStage, root);
		decorator.getStylesheets().add(Objects.requireNonNull(getClass().getClassLoader().getResource("css/window.css")).toExternalForm());
		var scene = new Scene(decorator, 640, 480);
		mainStage.setScene(scene);
		mainStage.setOnHidden(event -> Platform.exit());
		stage.hide();
		mainStage.show();

		logger.info("Game Scene loaded");
		// If we choose to rig the hands using file, then the game internally sets the stock for us
		if (options.getHandRigFilePath().isEmpty()) {
			mainController.getGame().setStock(new Stock().shuffle());
		}
		mainController.getGame().dealInitialTiles();
		logger.info("Game started.");
	}
}
