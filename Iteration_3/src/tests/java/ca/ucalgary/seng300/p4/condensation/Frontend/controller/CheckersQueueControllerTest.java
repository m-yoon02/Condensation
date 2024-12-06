package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import ca.ucalgary.seng300.p4.condensation.HelloApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import static org.junit.Assert.*;

public class CheckersQueueControllerTest extends ApplicationTest {

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

    @Test
    public void testFXMLLoad() throws Exception {
        // Ensure the FXML file and controller are loaded successfully
        assertNotNull(fxmlLoader);
        assertNotNull(checkersQueueController);
    }
}