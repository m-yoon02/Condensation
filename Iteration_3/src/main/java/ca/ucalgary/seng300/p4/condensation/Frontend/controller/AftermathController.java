package ca.ucalgary.seng300.p4.condensation.Frontend.controller;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class AftermathController {
    @FXML
    private Label gameResultLabel; // Match with fx:id in FXML

    @FXML
    private Button rematchButton;

    @FXML
    public void initialize() {
        gameResultLabel.setText("Game Over!");
    }
    @FXML
    public void handleRematch() {
        System.out.println("Rematch button clicked!");
    }
}