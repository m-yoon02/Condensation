package ca.ucalgary.seng300.p4.condensation.Frontend.controller;


import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WelcomeScreenController {


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

    @FXML
    void progMenu(ActionEvent event) throws URISyntaxException, IOException {

        Desktop.getDesktop().browse(new URI("https://csgit.ucalgary.ca/ssafe/seng300-f24-project/-/blob/main/Iteration_3/Deliverables/README.md?ref_type=heads"));

    }

    @FXML
    void teamMenu(ActionEvent event) throws URISyntaxException, IOException {

        Desktop.getDesktop().browse(new URI("https://csgit.ucalgary.ca/ssafe/seng300-f24-project/-/blob/main/team.md?ref_type=heads"));

    }

    @FXML
    void aboutMenu(ActionEvent event) throws URISyntaxException, IOException {

        Desktop.getDesktop().browse(new URI("https://csgit.ucalgary.ca/ssafe/seng300-f24-project/-/blob/main/README.md?ref_type=heads"));

    }




}
