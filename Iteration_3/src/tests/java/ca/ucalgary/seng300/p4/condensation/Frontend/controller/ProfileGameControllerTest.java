package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import ca.ucalgary.seng300.p4.condensation.Frontend.controller.ProfileGameController;
import ca.ucalgary.seng300.p4.condensation.HelloApplication;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.testfx.framework.junit.ApplicationTest;

import java.io.IOException;

import static org.junit.Assert.*;

public class ProfileGameControllerTest extends ApplicationTest {

    Button ticTacToePlayButton;
    Button connect4PlayButton;
    Button checkersPlayButton;
    Button homeButton;
    Label ticTacToeArea;
    Label connect4Area;
    Label checkersArea;

    ProfileGameController gameController;
    FXMLLoader fxmlLoader;
    Scene scene;
    Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("ProfileGame.fxml"));
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
        gameController = fxmlLoader.getController();

    }

    @Before
    public void setUp() throws Exception {
        ticTacToeArea = (Label) lookup("#ticTacToeArea").queryAs(Label.class);
        connect4Area = (Label) lookup("#connect4Area").queryAs(Label.class);
        checkersArea = (Label) lookup("#checkersArea").queryAs(Label.class);
        ticTacToePlayButton = (Button) lookup("#ticTacToePlayButton").queryAs(Button.class);
        connect4PlayButton = (Button) lookup("#connect4PlayButton").queryAs(Button.class);
        checkersPlayButton = (Button) lookup("#checkersPlayButton").queryAs(Button.class);
        homeButton = (Button) lookup("#homeButton").queryAs(Button.class);
    }

    @After
    public void tearDown() {
        ticTacToeArea = null;
        connect4Area = null;
        checkersArea = null;
        ticTacToePlayButton = null;
        connect4PlayButton = null;
        checkersPlayButton = null;
        homeButton = null;
    }


    @Test
    public void testFXMLLoad() {
        assertNotNull("FXML file should load successfully", fxmlLoader);
    }

    @Test
    public void testUIComponentsExist() {
        assertNotNull("Label for Tic Tac Toe should exist", ticTacToeArea);
        assertNotNull("Label for Connect 4 should exist", connect4Area);
        assertNotNull("Label for Checkers should exist", checkersArea);
        assertNotNull("Tic Tac Toe Play button should exist", ticTacToePlayButton);
        assertNotNull("Connect 4 Play button should exist", connect4PlayButton);
        assertNotNull("Checkers Play button should exist", checkersPlayButton);
        assertNotNull("Home button should exist", homeButton);
    }

    @Test
    public void testTicTacToeButtonClick() throws Exception {
        assertNotNull(ticTacToePlayButton);

        clickOn(ticTacToePlayButton);

        // Verify stage changes to Tic Tac Toe menu
        Parent newRoot = FXMLLoader.load(HelloApplication.class.getResource("tMenu.fxml"));
        assertNotNull("Tic Tac Toe scene should load", newRoot);
    }

    @Test
    public void testConnect4ButtonClick() throws Exception {
        assertNotNull(connect4PlayButton);

        clickOn(connect4PlayButton);

        // Verify stage changes to Connect 4 menu
        Parent newRoot = FXMLLoader.load(HelloApplication.class.getResource("ConnectFourHomeMenu.fxml"));
        assertNotNull("Connect 4 scene should load", newRoot);
    }

    @Test
    public void testCheckersButtonClick() throws Exception {
        assertNotNull(checkersPlayButton);

        clickOn(checkersPlayButton);

        // Verify stage changes to Checkers menu
        Parent newRoot = FXMLLoader.load(HelloApplication.class.getResource("checkersHomeMenu.fxml"));
        assertNotNull("Checkers scene should load", newRoot);
    }

    @Test
    public void testOnHomeButtonClicked() throws IOException {
        assertNotNull(homeButton);

        clickOn(homeButton);

        // Verify stage changes to Profile Home
        Parent newRoot = FXMLLoader.load(HelloApplication.class.getResource("ProfileHome.fxml"));
        assertNotNull("Profile Home scene should load", newRoot);
    }
}
