package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import ca.ucalgary.seng300.p4.condensation.HelloApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import static org.junit.Assert.*;

public class CheckersHowToPlayControllerTest extends ApplicationTest{

    Button backButton;
    FXMLLoader fxmlLoader;
    Scene scene;
    CheckersHowToPlayController checkersHowToPlayController;
    HelloApplication application = new HelloApplication();

    @Override
    public void start(Stage stage) throws Exception {
        application.start(stage);
        fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("checkersDescription.fxml"));
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
        checkersHowToPlayController = fxmlLoader.getController();
    }
    @Before
    public void setUp() throws Exception {
        backButton = (Button) lookup("#howToPlayBackBtn").queryAs(Button.class);
    }

    @After
    public void tearDown() throws Exception {
        backButton = null;
    }

//    @Test
//    public void testOnHowToPlayBackBtnClick() throws Exception {
//        // Clicks back button
//        clickOn(backButton);
//
//        // Checks if scene changed to checkers home menu
//        Node rootNode = backButton.getScene().getRoot();
//        assertNotNull(rootNode);
//
//        FXMLLoader newLoader = new FXMLLoader(HelloApplication.class.getResource("checkersHomeMenu.fxml"));
//        Scene expectedScene = new Scene(newLoader.load(), 700, 500);
//        assertNotNull(expectedScene.getRoot());
//
//        assertEquals(rootNode.getId(), expectedScene.getRoot().getId());
//    }
}