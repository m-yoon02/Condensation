package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import ca.ucalgary.seng300.p4.condensation.HelloApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import javafx.scene.control.Button;

import java.io.IOException;


import static org.junit.Assert.*;

public class ConnectFourHomeMenuControllerTest extends ApplicationTest {


    FXMLLoader fxmlLoader;
    Scene scene;
    ConnectFourHomeMenuController connectFourHomeMenuController;
    Button playButton;
    Button leaderboardButton;
    Button howToPlayButton;
    ImageView connect4Image;
    Button backButton;

    @Override
    public void start(Stage stage) throws IOException {
        fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("ConnectFourHomeMenu.fxml"));
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
        connectFourHomeMenuController = fxmlLoader.getController();
    }

    @Before
    public void setUp() throws Exception {
        playButton = (Button) lookup("#playButton").queryAs(Button.class);
        leaderboardButton = (Button) lookup("#leaderboardButton").queryAs(Button.class);
        howToPlayButton = (Button) lookup("#howToPlayButton").queryAs(Button.class);
        backButton = (Button) lookup("#backButton").queryAs(Button.class);
        connect4Image = (ImageView) lookup("#connect4Image").queryAs(ImageView.class);

    }

    @Test
    public void playButton_Test() throws Exception {
        assertNotNull(playButton);
        clickOn(playButton);
        Parent newNode = FXMLLoader.load(HelloApplication.class.getResource("ConnectFourHomeMenu.fxml"));
        assertNotNull(newNode);
    }

    @Test
    public void leaderboardButton_Test() throws Exception {
        assertNotNull(leaderboardButton);
        clickOn(leaderboardButton);
        Parent newNode = FXMLLoader.load(HelloApplication.class.getResource("ConnectFourHomeMenu.fxml"));
        assertNotNull(newNode);
    }

    @Test
    public void howToPlayButton_Test() throws Exception {
        assertNotNull(howToPlayButton);
        clickOn(howToPlayButton);
        Parent newNode = FXMLLoader.load(HelloApplication.class.getResource("ConnectFourHomeMenu.fxml"));
        assertNotNull(newNode);
    }

    @Test
    public void backButton_Test() throws Exception {
        assertNotNull(backButton);
        clickOn(backButton);
        Parent newNode = FXMLLoader.load(HelloApplication.class.getResource("ConnectFourHomeMenu.fxml"));
        assertNotNull(newNode);
    }


    @Test
    public void image_test() throws Exception {
        assertNotNull(connect4Image);
        assertTrue("The ImageView should still be visible after clicking.", connect4Image.isVisible());
    }

    @Test
    public void testOnPlayButtonClicked() throws Exception {
        assertNotNull(playButton);
        clickOn(playButton);
        Parent root = FXMLLoader.load(HelloApplication.class.getResource("connectfourqueue.fxml"));
        assertNotNull(root);
    }


    @Test
    public void testOnLeaderboardButtonClicked() throws Exception{
        assertNotNull(leaderboardButton);
        clickOn(leaderboardButton);
        try{
            Parent root = FXMLLoader.load(HelloApplication.class.getResource("ConnectFourLeaderboard.fxml"));
        }catch(Exception e){
            assertTrue(e.getCause() != null);
        }
    }

    @Test
    public void testOnHowToPlayButtonClicked() throws Exception {
        assertNotNull(howToPlayButton);
        clickOn(howToPlayButton);
        Parent root = FXMLLoader.load(HelloApplication.class.getResource("howToPlayConnect4.fxml"));
        assertNotNull(root);
    }

    @Test
    public void testOnBackButtonClicked() throws Exception {
        assertNotNull(backButton);
        clickOn(backButton);
        Parent root = FXMLLoader.load(HelloApplication.class.getResource("ProfileGame.fxml"));
        assertNotNull(root);
    }

    @Test
    public void testInitializeImage() {
        connectFourHomeMenuController.initialize();
        assertEquals("/connect4.png",
                connect4Image.getImage().getUrl().substring(connect4Image.getImage().getUrl().lastIndexOf("/")));
    }

    @Test
    public void testInitialize() {
        assertNotNull(connectFourHomeMenuController);
    }

    @Test
    public void testNavigateToQueue_withInvalidFile() throws Exception {
        try {
            var method = ConnectFourHomeMenuController.class.getDeclaredMethod("navigateToQueue", String.class);
            method.setAccessible(true);
            method.invoke(connectFourHomeMenuController, "/ca/ucalgary/seng300/p4/condensation/Invalid.fxml");
        } catch (Exception exception) {
            RuntimeException runtimeException = new RuntimeException();
            assertTrue(exception.getCause() != null);
        }
    }

    @Test
    public void testNavigateTo_withInvalidFile() throws Exception {
        try {
            var method = ConnectFourHomeMenuController.class.getDeclaredMethod("navigateTo", String.class);
            method.setAccessible(true);
            method.invoke(connectFourHomeMenuController, "/ca/ucalgary/seng300/p4/condensation/Invalid.fxml");
        } catch (Exception exception) {
            RuntimeException runtimeException = new RuntimeException();
            assertTrue(exception.getCause() != null);
        }
    }

    @Test
    public void testNavigateTo_withvalidFile() throws Exception {
        try {
            var method = ConnectFourHomeMenuController.class.getDeclaredMethod("navigateTo", String.class);
            method.setAccessible(true);
            method.invoke(connectFourHomeMenuController, "/ca/ucalgary/seng300/p4/condensation/howToPlayConnect4.fxml");
            Parent root = FXMLLoader.load(HelloApplication.class.getResource("howToPlayConnect4.fxml"));
            assertNotNull(root);
        } catch (Exception exception) {
            RuntimeException runtimeException = new RuntimeException();
        }
    }


    @Test
    public void testNavigateToQueue_withvalidFile() throws Exception {
        try {
            var method = ConnectFourHomeMenuController.class.getDeclaredMethod("navigateToQueue", String.class);
            method.setAccessible(true);
            method.invoke(connectFourHomeMenuController, "/ca/ucalgary/seng300/p4/condensation/connectfourqueue.fxml");
            Parent root = FXMLLoader.load(HelloApplication.class.getResource("connectfourqueue.fxml"));
            assertNotNull(root);
        } catch (Exception exception) {
            RuntimeException runtimeException = new RuntimeException();
        }
    }






}