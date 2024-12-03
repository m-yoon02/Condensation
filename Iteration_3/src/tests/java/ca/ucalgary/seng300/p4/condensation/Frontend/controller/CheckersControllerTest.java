package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import ca.ucalgary.seng300.p4.condensation.HelloApplication;
import org.testfx.framework.junit.ApplicationTest;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.*;

public class CheckersControllerTest extends ApplicationTest {

    GridPane chessboardGrid;
    Label turnLabel;
    Button forfeitBtn;
    Button forfeitNoBtn;
    Button forfeitYesBtn;
    Button sendBtn;
    TextArea chatBox;
    ScrollPane chatScrollPane;
    VBox paneInsideScrollPane;

    FXMLLoader fxmlLoader;
    Scene scene;

    CheckersController checkersController;

    @Override
    public void start(Stage stage) throws Exception {
        fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("checkersInstance.fxml"));
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
        checkersController = fxmlLoader.getController();
    }

    @Before
    public void setUp() throws Exception {
        chessboardGrid = (GridPane) lookup("#chessboardGrid").queryAs(GridPane.class);
        turnLabel = (Label) lookup("#turnLabel").queryAs(Label.class);
        forfeitBtn = (Button) lookup("#forfeitBtn").queryAs(Button.class);
        forfeitNoBtn = (Button) lookup("#forfeitNoBtn").queryAs(Button.class);
        forfeitYesBtn = (Button) lookup("#forfeitYesBtn").queryAs(Button.class);
        sendBtn = (Button) lookup("#sendBtn").queryAs(Button.class);
        chatBox = (TextArea) lookup("#chatBox").queryAs(TextArea.class);
        chatScrollPane = (ScrollPane) lookup("#chatScrollPane").queryAs(ScrollPane.class);
        paneInsideScrollPane = (VBox) lookup("#paneInsideScrollPane").queryAs(VBox.class);
    }

    @After
    public void tearDown() throws Exception {
        chessboardGrid = null;
        turnLabel = null;
        forfeitBtn = null;
        forfeitNoBtn = null;
        forfeitYesBtn = null;
        sendBtn = null;
        chatBox = null;
        chatScrollPane = null;
        paneInsideScrollPane = null;
    }

    @Test
    public void initialize() {
    }

    @Test
    public void receiveCheckers() {
    }

    @Test
    public void returnToBackendCheckers() {
    }

    @Test
    public void onForfeitBtnClick() {
    }

    @Test
    public void onForfeitYesClick() {
    }

    @Test
    public void onSendBtnClick() {
    }
}