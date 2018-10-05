import com.jfoenix.controls.JFXDecorator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

public class Main extends Application {

	private static final Logger logger = LogManager.getLogger(Application.class.getName());

	@Override
	public void start(Stage stage) throws Exception {
		logger.info("Creating window and stage for Tile Rummy");
		final int width = 640;
		final int height = 480;

		JFXDecorator decorator = new JFXDecorator(stage, FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("view/MainView.fxml"))));
		decorator.getStylesheets().add(Objects.requireNonNull(getClass().getClassLoader().getResource("css/window.css")).toExternalForm());
		Scene scene = new Scene(decorator, width, height);
		stage.setTitle("Tile Rummy");
		stage.setScene(scene);
		stage.setMinWidth(width);
		stage.setMinHeight(height);
		stage.show();
		logger.info("Stage shown");
	}

	public static void main(String[] args) {
		launch(args);
	}
}
