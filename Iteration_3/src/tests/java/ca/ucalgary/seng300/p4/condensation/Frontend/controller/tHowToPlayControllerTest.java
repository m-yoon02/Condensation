package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import ca.ucalgary.seng300.p4.condensation.HelloApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import java.io.IOException;

import static org.junit.Assert.assertFalse;


public class tHowToPlayControllerTest extends ApplicationTest {

    FXMLLoader loader;
    Scene scene;
    tHowToPlayController controller;

    Button backbutton;
    HelloApplication application;

    @Override
    public void start(Stage stage) throws IOException {
        HelloApplication application = new HelloApplication();
        application.start(stage);
        loader = new FXMLLoader(HelloApplication.class.getResource("tHowToPlay.fxml"));
        scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.show();
        controller = loader.getController();
    }

    @Before
    public void setUp() throws Exception {
        backbutton = lookup("#backbutton").queryAs(Button.class);

    }

    @After
    public void tearDown() throws Exception {
        backbutton = null;
    }
//
//    @Test
//    public void howToPlayButtonClicked() throws Exception {
//        clickOn(backbutton);
//
//        Scene tHowtoPlayScene = backbutton.getScene();
//
//        Button button = (Button) lookup("#playButton").queryAs(Button.class);
//        Scene tMenuScene = button.getScene();
//
//        assertFalse(tHowtoPlayScene.equals(tMenuScene));
//
//    }

//    @Test
//    public void howToPlayButtonClicked_2() throws Exception {
//        clickOn(backbutton);
//
//        Scene tHowtoPlayScene = backbutton.getScene();
//
//        Button button = (Button) lookup("#leaderboardButton").queryAs(Button.class);
//        Scene tMenuScene = button.getScene();
//    }
}