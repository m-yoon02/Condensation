package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import ca.ucalgary.seng300.p4.condensation.HelloApplication;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.*;


public class TicTacToeControllerTest extends ApplicationTest{

    FXMLLoader loader;
    Scene scene;
    TicTacToeController ticTacToeController;

    Button forfeitBtn;

    HelloApplication application;

    LoginController loginController;
    TextField usernameField;
    TextField passwordField;
    Button loginButton;

    TicTacToeQueueController queueController;

    GridPane chessboardGrid;
    Label turnLabel;
    Button sendBtn;
    TextArea chatBox;
    ScrollPane chatScrollPane;
    VBox paneInsideScrollPane;



    @Override
    public void start(Stage stage) throws IOException, ParseException {
        application = new HelloApplication();
        application.start(stage);
        loader = new FXMLLoader(HelloApplication.class.getResource("Login.fxml"));
        scene = new Scene(loader.load());
        loginController = loader.getController();
        stage.setScene(scene);
        stage.show();

        // Access Login.fxml nodes
        usernameField = (TextField) lookup("#usernameField").queryAs(TextField.class);
        passwordField = (TextField) lookup("#passwordField").queryAs(TextField.class);
        loginButton = (Button) lookup("#loginBtn").queryAs(Button.class);

        // Perform login
        usernameField.setText("Samuel");
        passwordField.setText("password123");

        loginController.sendJSONObject();

        loader = new FXMLLoader(HelloApplication.class.getResource("tictactoequeue.fxml"));
        scene = new Scene(loader.load());
        queueController = loader.getController();
        stage.setScene(scene);
        stage.show();

        queueController.start();

        loader = new FXMLLoader(HelloApplication.class.getResource("tGame.fxml"));
        scene = new Scene(loader.load());
        ticTacToeController = loader.getController();
        stage.setScene(scene);
        stage.show();

        ticTacToeController.start();



    }

    @Before
    public void setUp() throws Exception {

        forfeitBtn = (Button) lookup("#forfeitBtn").queryAs(Button.class);
        chessboardGrid = (GridPane) lookup("#chessboardGrid").queryAs(GridPane.class);
        turnLabel = (Label) lookup("#titleLabel").queryAs(Label.class);
        forfeitBtn = (Button) lookup("#forfeitBtn").queryAs(Button.class);
        sendBtn = (Button) lookup("#sendBtn").queryAs(Button.class);
        chatBox = (TextArea) lookup("#chatBox").queryAs(TextArea.class);
        chatScrollPane = (ScrollPane) lookup("#chatScrollPane").queryAs(ScrollPane.class);
        paneInsideScrollPane = (VBox) lookup("#paneInsideScrollPane").queryAs(VBox.class);

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testSendButtonMessageHandling() {

        // Set the text in the chat box
        chatBox.setText("Test chat message");
        assertNotNull(chatBox);

        // Simulate clicking the send button
        clickOn(sendBtn);

        // After clicking the send button, verify that the message is added to the pane
        // We check the first child in the paneInsideScrollPane, which should be an HBox containing the message
        HBox messageHBox = (HBox) paneInsideScrollPane.getChildren().get(0);
        VBox messageBox = (VBox) messageHBox.getChildren().get(0);
        Label messageLabel = (Label) messageBox.getChildren().get(1);

        // Verify that the message text in the Label is the same as the chatBox content
        assertEquals("Test chat message", messageLabel.getText());

        HBox nameTimeHBox = (HBox) messageBox.getChildren().get(0);
        Label timeSentLabel = (Label) nameTimeHBox.getChildren().get(1);

        String timeSent = timeSentLabel.getText();
        assertNotNull(timeSent);
        assertTrue(timeSent.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}"));

        // Verify that the chatBox is cleared after sending the message
        assertEquals("", chatBox.getText());
    }

    @Test
    public void testPlayerResponse() {
        // Set the text in the chat box (simulate player 1's message)
        chatBox.setText("hello");

        // Simulate clicking the send button for player 1's message
        clickOn(sendBtn);

        // Verify Player 2's response
        HBox messageHBox2 = (HBox) paneInsideScrollPane.getChildren().get(1);  // Player 2's response is the second child
        VBox messageBox2 = (VBox) messageHBox2.getChildren().get(0);
        Label messageLabel2 = (Label) messageBox2.getChildren().get(1);  // The response message label

        // Verify that the message matches Player 2's response
        assertEquals("hello there noob", messageLabel2.getText());
    }

    @Test
    public void testDisableButtons() throws IOException {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("UpdateType", "GAME_END");
        Platform.runLater(() -> {
            try {
                ticTacToeController.receiveFromBackendTicTacToe(jsonObject);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        ArrayList<Button> buttons = new ArrayList<>();
        Parent parent = forfeitBtn.getParent();
        parent.lookupAll(".button").forEach(node -> {
            if (node instanceof Button && node.getId() != null && node.getId().startsWith("btn")) {;
            buttons.add((Button) node);}
        });
        buttons.forEach(button -> {
           assertTrue(button.isDisabled());

        });

    }

    @Test
    public void testForfeitNoButtonClick() {

        assertTrue(true);

        ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(consoleOutput));

       clickOn(forfeitBtn).clickOn(ButtonType.CANCEL.getText());

        System.setOut(System.out);  // Reset System.out
        assertTrue(consoleOutput.toString().contains("No"));  // Assert console output contains "Yes"
    }






}