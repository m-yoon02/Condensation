package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import ca.ucalgary.seng300.p4.condensation.HelloApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CheckersControllerTest {

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

    @BeforeEach
    void setUp() throws Exception {
        chessboardGrid = (GridPane) lookup("#chessboardGrid").queryAs(GridPane.class);
        turnLabel = (Label) lookup("#turnLabel").queryAs(Label.class);
        forfeitBtn = (Button) lookup("#forfeitBtn").queryAs(Button.class);
        forfeitNoBtn = (GridPane) lookup("#forfeitNoBtn").queryAs(Button.class);
        forfeitYesBtn = (GridPane) lookup("#forfeitYesBtn").queryAs(Button.class);
        sendBtn = (GridPane) lookup("#sendBtn").queryAs(Button.class);
        chatBox = (GridPane) lookup("#chatBox").queryAs(TextArea.class);
        chatScrollPane = (GridPane) lookup("#chatScrollPane").queryAs(ScrollPane.class);
        paneInsideScrollPane = (GridPane) lookup("#paneInsideScrollPane").queryAs(VBox.class);
    }

    @AfterEach
    void tearDown() throws Exception {
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
    void initialize() {
    }

    @Test
    void receiveCheckers() {
    }

    @Test
    void returnToBackendCheckers() {
    }

    @Test
    void onForfeitBtnClick() {
    }

    @Test
    void onForfeitYesClick() {
    }

    @Test
    void onSendBtnClick() {
    }
}