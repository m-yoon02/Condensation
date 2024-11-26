package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class OtherProfileController {

    @FXML
    private TextField playerUser;

    @FXML
    private Label titleOfScreen;

    @FXML
    private Label instructions;

    @FXML
    private Button returnToHome;

    @FXML
    private Parent origin;

    @FXML
    private Stage createWindow;
    @FXML
    private Scene createScene;

    @FXML
    private Label playerFound;

    @FXML
    protected void returnToProfile(Event event) throws IOException
    {
        origin = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Profile.fxml")));
        createWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();
        createScene = new Scene(origin);
        createWindow.setScene(createScene);
        createWindow.show();
    }
}
