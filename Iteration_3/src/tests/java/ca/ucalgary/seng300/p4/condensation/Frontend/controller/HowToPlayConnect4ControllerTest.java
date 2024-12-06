package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import ca.ucalgary.seng300.p4.condensation.HelloApplication;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.junit.Test;

import static org.junit.Assert.*;

public class HowToPlayConnect4ControllerTest  extends Application {

    FXMLLoader fxmlLoader;
    Scene scene;
    ActionEvent event = new ActionEvent();
    HowToPlayConnect4Controller howToPlayConnect4Controller;

    @Override
    public void start(Stage stage) throws Exception {
        fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("howToPlayConnect4.fxml"));
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
        howToPlayConnect4Controller = fxmlLoader.getController();
    }

    @Test
    public void testOnHowToPlayBackBtnClick() throws Exception {
        howToPlayConnect4Controller.goBack(event);


    }
}