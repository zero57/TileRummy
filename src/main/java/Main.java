import com.jfoenix.controls.JFXDecorator;
import controller.MainController;
import controller.MenuController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Stock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;

import java.util.Objects;

public class Main extends Application {

	private static final Logger logger = LogManager.getLogger(Application.class.getName());

	@Override
	public void start(Stage stage) throws Exception {
		logger.info("Creating window and stage for Tile Rummy");
		final var width = 640;
		final var height = 480;

		// MainController controller = new MainController();
		// FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("view/MainView.fxml"));
		// loader.setControllerFactory(c -> controller);
		// VBox root = loader.load();

		// var decorator = new JFXDecorator(stage, root);
		// decorator.getStylesheets().add(Objects.requireNonNull(getClass().getClassLoader().getResource("css/window.css")).toExternalForm());
		// var gameScene = new Scene(decorator, width, height);
		// stage.setTitle("Tile Rummy");
		// //stage.setScene(gameScene);
		// //stage.show();
		// logger.info("Stage shown");
		// // Start new game
		// //controller.getGame().setStock(new Stock().shuffle());
		// //controller.getGame().dealInitialTiles();

		//////////////////////////////////////////////////////////

		MenuController menuController = new MenuController(stage);
		FXMLLoader menuLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/MenuView.fxml"));
		menuLoader.setControllerFactory(c -> menuController);
		VBox menuRoot = menuLoader.load();		
		var menuDecorator = new JFXDecorator(stage, menuRoot);
		var menuScene = new Scene(menuDecorator, width, height);
		stage.setScene(menuScene);
		stage.show();
		logger.info("Stage shown");
		logger.info("Menu Scene loaded");
	}

	public static void main(String[] args) {
		launch(args);
	}
}
