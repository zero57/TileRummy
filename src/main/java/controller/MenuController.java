package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDecorator;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.OptionChoices;
import model.Stock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import parser.FileParser;

import java.io.File;
import java.util.Objects;

public class MenuController {

	private static final Logger logger = LogManager.getLogger(MenuController.class.getName());

	@FXML
	private JFXButton playBtn;

	@FXML
	private JFXButton btnFileHandRig;

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
	private JFXCheckBox timerCheck;

	@FXML
	private JFXCheckBox rigTileCheck;

	@FXML
	private JFXCheckBox rigHandCheck;

	@FXML
	private JFXCheckBox showHandCheck;

	@FXML
	private Stage stage;

	private OptionChoices options;
	private boolean isPlayable;
	final int width = 640;
	final int height = 480;

	public MenuController(Stage stage) {
		this.stage = stage;
		options = new OptionChoices();
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
				options.setNumPlayers((Integer) numPlayerBox.getValue());
				options.setPlayer1(player1Box.getSelectionModel().getSelectedIndex());
				options.setPlayer2(player2Box.getSelectionModel().getSelectedIndex());
				if (!(player3Box.isDisabled())) {
					options.setPlayer3(player3Box.getSelectionModel().getSelectedIndex());
				}

				if (!(player4Box.isDisabled())) {
					options.setPlayer4(player4Box.getSelectionModel().getSelectedIndex());
				}

				options.setTimerChecked(timerCheck.isSelected());
				options.setRigHandChecked(rigHandCheck.isSelected());
				options.setRigTileDrawChecked(rigTileCheck.isSelected());
				options.setShowHandsChecked(showHandCheck.isSelected());

				logger.debug("OUTPUTTING OPTIONS STRUCTURE");
				logger.debug("NUM OF PLAYERS IS " + options.getNumPlayers());
				logger.debug("PLAYER 1 IS " + options.getPlayer1());
				logger.debug("PLAYER 2 IS " + options.getPlayer2());
				logger.debug("PLAYER 3 IS " + options.getPlayer3());
				logger.debug("PLAYER 4 IS " + options.getPlayer4());

				logger.debug("TIMERBOX CHECKED IS " + options.getTimerChecked());
				logger.debug("RIGTILES CHECKED IS " + options.getRigTileDrawChecked());
				logger.debug("RIGHANDS CHECKED IS " + options.getRigHandChecked());
				logger.debug("SHOWHANDS CHECKED IS " + options.getShowHandsChecked());

				try {
					if (!options.getHandRigFilePath().isEmpty()) {
						FileParser fileParser = new FileParser();
						if (!fileParser.isValidFile(options.getHandRigFilePath(), options.getNumPlayers())) {
							logger.error("Invalid file input. Try again");
							return;
						}
					}
					showMainStage();
				} catch (Exception e) {
					logger.error(e);
				}
			}
		});

		exitBtn.setOnMouseClicked(b -> {
			System.exit(0);
		});

		btnFileHandRig.setOnMouseClicked(e -> {
			FileChooser fileChooser = new FileChooser();
			File file = fileChooser.showOpenDialog(stage);
			if (file != null) {
				options.setHandRigFilePath(file.getAbsolutePath());
			}
		});
	}

	public void showMainStage() throws Exception {
		Platform.setImplicitExit(false);

		Stage mainStage = new Stage();
		MainController mainController = new MainController(options);
		FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("view/MainView.fxml"));
		loader.setControllerFactory(c -> mainController);
		VBox root = loader.load();

		var decorator = new JFXDecorator(mainStage, root);
		decorator.getStylesheets().add(Objects.requireNonNull(getClass().getClassLoader().getResource("css/window.css")).toExternalForm());
		var scene = new Scene(decorator, width, height);
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
