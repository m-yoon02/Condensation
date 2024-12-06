package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import ca.ucalgary.seng300.p4.condensation.HelloApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.json.simple.parser.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import java.io.IOException;

import static org.junit.Assert.*;


public class TicTacToeControllerTest extends ApplicationTest{

    FXMLLoader loader;
    Scene scene;
    TicTacToeController controller;

    Button forfeitBtn;



    @Override
    public void start(Stage stage) throws IOException {
        loader = new FXMLLoader(HelloApplication.class.getResource("tGame.fxml"));
        scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.show();
        controller = loader.getController();
    }

    @Before
    public void setUp() throws Exception {

        forfeitBtn = (Button) lookup("#forfeitBtn").queryAs(Button.class);

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testForfietYesClick() throws IOException, ParseException {

        assertTrue(forfeitBtn.getScene().getWindow().isFocused());

        clickOn(forfeitBtn);
        clickOn("OK");

        assertFalse(forfeitBtn.getScene().getWindow().isFocused());

    }




}