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

public class CheckersHomeMenuControllerTest extends ApplicationTest{

    FXMLLoader fxmlLoader;
    Scene scene;
    CheckersHomeMenuController checkersHomeMenuController;
    Button homeMenuBackBtn;
    HelloApplication application = new HelloApplication();

    @Override
    public void start(Stage stage) throws Exception {
        application.start(stage);

        fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("checkersHomeMenu.fxml"));
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
        checkersHomeMenuController = fxmlLoader.getController();
    }

    @Before
    public void setUp() throws Exception {
        homeMenuBackBtn = (Button) lookup("#homeMenuBackBtn").queryAs(Button.class);
    }

    @After
    public void tearDown() throws Exception {
        homeMenuBackBtn = null;
    }

    @Test
    public void testOnHomeMenuBackBtnClick() throws Exception {

        clickOn(homeMenuBackBtn);

        Node rootNode = homeMenuBackBtn.getScene().getRoot();
        assertNotNull(rootNode);

        FXMLLoader newLoader = new FXMLLoader(HelloApplication.class.getResource("ProfileGame.fxml"));
        Scene expectedScene = new Scene(newLoader.load(), 700, 500);
        assertNotNull(expectedScene.getRoot());

        assertEquals(rootNode.getId(), expectedScene.getRoot().getId());
    }
}