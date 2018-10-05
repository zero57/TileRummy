package controller;

import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HandController {
	private static final Logger logger = LogManager.getLogger(HandController.class.getName());

	@FXML
	private FlowPane fpHand;

	@FXML
	public void initialize() {
		logger.info("Initializing Hand");
	}

}
