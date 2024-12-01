package ca.ucalgary.seng300.p4.condensation.Frontend.controller;


import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class WelcomeScreenController {

    @FXML
    private Button loginBtn;

    @FXML
    private Button signupBtn;

    @FXML
    private FXMLLoader loader;

    @FXML
    private Pane root;

    @FXML
    private Stage window;

    @FXML
    private Scene scene;


    @FXML
    public void moveToLogin(MouseEvent event) throws IOException {

        loader = new FXMLLoader(getClass().getResource("/ca/ucalgary/seng300/p4/condensation/Login.fxml"));
        root = loader.load();

        window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        window.setScene(scene);
        window.show();

    }

    @FXML
    public void moveToSignup(MouseEvent event) throws IOException {

        loader = new FXMLLoader(getClass().getResource("/ca/ucalgary/seng300/p4/condensation/SignUp.fxml"));
        root = loader.load();

        window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        window.setScene(scene);
        window.show();

    }




}
