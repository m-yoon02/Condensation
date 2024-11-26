package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ConnectFourQueueController {
    @FXML
    private Button leaveQueue;

    @FXML
    private Label title;

    @FXML
    private Label numberOfPeopleIndicator;

    @FXML
    private Label actualPeople;

    @FXML
    private TableView queue;

    @FXML
    private Parent origin;

    @FXML
    private Stage createWindow;
    @FXML
    private Scene createScene;

    protected void returnToProfile(Event event) throws IOException
    {
        origin = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Profile.fxml")));
        createWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();
        createScene = new Scene(origin);
        createWindow.setScene(createScene);
        createWindow.show();
    }
}
