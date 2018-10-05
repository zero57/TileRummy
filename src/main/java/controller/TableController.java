package controller;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TableController {

	private static final Logger logger = LogManager.getLogger(TableController.class.getName());

	@FXML
	private GridPane gpTable;

	@FXML
	public void initialize() {
		logger.info("Initializing Table");
	}

}
