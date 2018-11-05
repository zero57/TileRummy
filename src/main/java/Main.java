import com.jfoenix.controls.JFXDecorator;
import controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Stock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

public class Main extends Application {

	private static final Logger logger = LogManager.getLogger(Application.class.getName());

	@Override
	public void start(Stage stage) throws Exception {
		logger.info("Creating window and stage for Tile Rummy");
		final var width = 640;
		final var height = 480;

		MainController controller = new MainController();
		FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("view/MainView.fxml"));
		loader.setControllerFactory(c -> controller);
		VBox root = loader.load();

		var decorator = new JFXDecorator(stage, root);
		decorator.getStylesheets().add(Objects.requireNonNull(getClass().getClassLoader().getResource("css/window.css")).toExternalForm());
		var scene = new Scene(decorator, width, height);
		stage.setTitle("Tile Rummy");
		stage.setScene(scene);
		stage.show();
		logger.info("Stage shown");

		// Start new game
		controller.getGame().setStock(new Stock().shuffle());
		controller.getGame().dealInitialTiles();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
