package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import ca.ucalgary.seng300.p4.condensation.HelloApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import static org.junit.Assert.*;

public class CheckersQueueControllerTest extends ApplicationTest {

    Parent origin;
    Stage createWindow;
    Scene createScene;
    Stage stage;

    FXMLLoader fxmlLoader;
    Scene scene;

    CheckersQueueController checkersQueueController;

    @Override
    public void start(Stage stage) throws Exception {
        fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("checkersqueue.fxml"));
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
        checkersQueueController = fxmlLoader.getController();
    }

    @Before
    public void setUp() {
        origin = (Parent) lookup("#origin").queryAs(Parent.class);
//        createWindow = (Stage) lookup("#createWindow").queryAs(Stage.class);
//        createScene = (Scene) lookup("#createScene").queryAs(Scene.class);
//        stage = (Stage) lookup("#stage").queryAs(Stage.class);
    }

    @After
    public void tearDown() {
        origin = null;
        createWindow = null;
        createScene = null;
        stage = null;
    }

    @Test
    public void initialize() {
    }

    @Test
    public void returnToProfile() {
    }

    @Test
    public void sendToBackendCheckers() {
    }
}