package controller;

import com.jfoenix.controls.JFXButton;
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

import javafx.stage.Modality;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import java.util.Optional;

public class MenuController {

	private static final Logger logger = LogManager.getLogger(MenuController.class.getName());

	@FXML
	private JFXButton playBtn;
	@FXML
	private JFXButton exitBtn;

	@FXML
	private Stage stage;

	@FXML
	private Scene gameScene;

	MainController controller;
	final int width = 640;
	final int height = 480;

	public MenuController(Stage stage) throws Exception {
		this.stage = stage;

		controller = new MainController();
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

		playBtn.setOnMouseClicked(b -> {
			stage.setScene(gameScene);
			logger.info("Game Scene loaded");
			controller.getGame().setStock(new Stock().shuffle());
			controller.getGame().dealInitialTiles();
			logger.info("Game started.");
		});
	}
}
