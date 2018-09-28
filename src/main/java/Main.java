import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main extends Application {

    private static final Logger logger = LogManager.getLogger(Application.class.getName());

    @Override
    public void start(Stage stage) throws Exception {
        logger.info("Creating window and stage for Tile Rummy");
        final int width = 100;
        final int height = 100;

        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        stage.setTitle("Tile Rummy");
        stage.setScene(new Scene(root, width, height));
        stage.setMinWidth(width);
        stage.setMinHeight(height);
        stage.show();
        logger.info("Stage shown");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
