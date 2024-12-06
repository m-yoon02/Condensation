package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import ca.ucalgary.seng300.p4.condensation.HelloApplication;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import org.testfx.framework.junit.ApplicationTest;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class HowToPlayConnect4ControllerTest  extends ApplicationTest {

    Button backButton;
    FXMLLoader fxmlLoader;
    Scene scene;

    HowToPlayConnect4Controller howToPlayConnect4Controller;
    HelloApplication application = new HelloApplication();

    @Override
    public void start(Stage stage) throws Exception {
        application.start(stage);

        fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("howToPlayConnect4.fxml"));
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
        howToPlayConnect4Controller = fxmlLoader.getController();
    }

    @Before
    public void setUp() throws Exception {
        backButton = (Button) lookup("#backButton").queryAs(Button.class);
    }

    @After
    public void tearDown() throws Exception {
        backButton = null;
    }

    @Test
    public void testOnHowToPlayBackBtnClick() throws Exception {

        clickOn(backButton);

        Node rootNode = backButton.getScene().getRoot();
        assertNotNull(rootNode);

        FXMLLoader newLoader = new FXMLLoader(HelloApplication.class.getResource("ConnectFourHomeMenu.fxml"));
        Scene expectedScene = new Scene(newLoader.load(), 700, 500);

        assertNotNull(expectedScene.getRoot());

    }
}