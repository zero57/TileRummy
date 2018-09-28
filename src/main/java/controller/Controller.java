package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class Controller {
    @FXML
    private Button helloButton;

    @FXML
    private void onHelloButtonClick() {
        System.out.println("Inside function onHelloButtonClick");
        System.out.println("Text of button: " + helloButton.getText());
    }
}
