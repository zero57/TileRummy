package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.jfoenix.controls.JFXDecorator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import model.Stock;
import java.util.Objects;
import java.io.IOException;
import model.OptionChoices;

public class MenuController {

	private static final Logger logger = LogManager.getLogger(MenuController.class.getName());

	@FXML
	private JFXButton playBtn;
	@FXML
	private JFXButton exitBtn;
	@FXML
	private JFXComboBox numPlayerBox;
	@FXML
	private JFXComboBox player1Box;
	@FXML
	private JFXComboBox player2Box;
	@FXML
	private JFXComboBox player3Box;
	@FXML
	private JFXComboBox player4Box;

	@FXML
	private Stage stage;

	@FXML
	private Scene gameScene;

	private OptionChoices options;
	private boolean isPlayable;
	MainController controller;
	final int width = 640;
	final int height = 480;

	public MenuController(Stage stage) throws Exception {
		this.stage = stage;
		options = new OptionChoices();

		controller = new MainController(options);
		FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("view/MainView.fxml"));
		loader.setControllerFactory(c -> controller);
		VBox root = loader.load();

		var decorator = new JFXDecorator(stage, root);
		decorator.getStylesheets().add(Objects.requireNonNull(getClass().getClassLoader().getResource("css/window.css")).toExternalForm());
		gameScene = new Scene(decorator, width, height);	
	}

	@FXML
	public void initialize() {
		logger.info("Initializing Menu view");

		player3Box.disableProperty().bind(numPlayerBox.valueProperty().isEqualTo(2));
		player4Box.disableProperty().bind(numPlayerBox.valueProperty().isEqualTo(2).or(numPlayerBox.valueProperty().isEqualTo(3)));

		playBtn.setOnMouseClicked(b -> {
			isPlayable = numPlayerBox.validate() && player1Box.validate() && player2Box.validate();

			if (player3Box.isDisabled()) {
				player3Box.getSelectionModel().clearSelection();
			} else {
				isPlayable = isPlayable && player3Box.validate();
			}

			if (player4Box.isDisabled()) {
				player4Box.getSelectionModel().clearSelection();
			} else {
				isPlayable = isPlayable && player4Box.validate();				
			}

			if (isPlayable) {
				options.setNumPlayers((Integer)numPlayerBox.getValue());
				options.setPlayer1((OptionChoices.Type)player1Box.getValue());
				options.setPlayer2((OptionChoices.Type)player2Box.getValue());
				if (!(player3Box.isDisabled())) {
					options.setPlayer3((OptionChoices.Type)player3Box.getValue());
				} else {
					options.setPlayer3(OptionChoices.Type.NULL);
				}

				if (!(player4Box.isDisabled())) {
					options.setPlayer4((OptionChoices.Type)player4Box.getValue());
				} else {
					options.setPlayer4(OptionChoices.Type.NULL);
				}

				logger.debug("OUTPUTTING OPTIONS STRUCTURE");
				logger.debug("NUM OF PLAYERS IS " + options.getNumPlayers());
				logger.debug("PLAYER 1 IS " + options.getPlayer1());
				logger.debug("PLAYER 2 IS " + options.getPlayer2());
				logger.debug("PLAYER 3 IS " + options.getPlayer3());
				logger.debug("PLAYER 4 IS " + options.getPlayer4());

				stage.setScene(gameScene);
				logger.info("Game Scene loaded");
				controller.getGame().setStock(new Stock().shuffle());
				controller.getGame().dealInitialTiles();
				logger.info("Game started.");
			}
		});

		exitBtn.setOnMouseClicked(b -> {
			System.exit(0);
		});
	}
}
