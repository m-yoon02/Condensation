package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import ca.ucalgary.seng300.p4.condensation.HelloApplication;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import org.json.simple.parser.ParseException;
import org.testfx.framework.junit.ApplicationTest;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.*;

public class CheckersControllerTest extends ApplicationTest {

    GridPane chessboardGrid;
    Label turnLabel;
    Button forfeitBtn;
    Button sendBtn;
    TextArea chatBox;
    ScrollPane chatScrollPane;
    VBox paneInsideScrollPane;

    TextField usernameField;
    TextField passwordField;
    Button loginButton;

    FXMLLoader fxmlLoader;
    FXMLLoader fxmlLoader2;
    FXMLLoader fxmlLoader3;
    Scene scene;

    CheckersController checkersController;
    LoginController loginController;
    CheckersQueueController checkersQueueController;
    HelloApplication application = new HelloApplication();

    @Override
    public void start(Stage stage) throws Exception {

        application.start(stage);

        fxmlLoader2 = new FXMLLoader(HelloApplication.class.getResource("Login.fxml"));
        Scene loginScene = new Scene(fxmlLoader2.load());
        loginController = fxmlLoader2.getController();
        stage.setScene(loginScene);
        stage.show();

        // Access Login.fxml nodes
        usernameField = (TextField) lookup("#usernameField").queryAs(TextField.class);
        passwordField = (TextField) lookup("#passwordField").queryAs(TextField.class);
        loginButton = (Button) lookup("#loginBtn").queryAs(Button.class);

        // Perform login
        usernameField.setText("Samuel");
        passwordField.setText("password123");

        loginController.sendJSONObject();

        fxmlLoader3 = new FXMLLoader(HelloApplication.class.getResource("checkersqueue.fxml"));
        Scene queueScene = new Scene(fxmlLoader3.load());
        checkersQueueController = fxmlLoader3.getController();
        stage.setScene(queueScene);
        stage.show();

        checkersQueueController.start();

        fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("checkersInstance.fxml"));
        scene = new Scene(fxmlLoader.load());
        checkersController = fxmlLoader.getController();
        stage.setScene(scene);
        stage.show();

//        JSONObject playerInfoForGame = new JSONObject();
//        String startGameInfoPayload = NetworkManager.getNetworkInstance().handleQueuePlayer(playerInfoForGame);
//        GameSessionRecord.getGameSessionRecord().fromJSON(startGameInfoPayload);
    }

    @Before
    public void setUp() throws Exception {
        chessboardGrid = (GridPane) lookup("#chessboardGrid").queryAs(GridPane.class);
        turnLabel = (Label) lookup("#turnLabel").queryAs(Label.class);
        forfeitBtn = (Button) lookup("#forfeitBtn").queryAs(Button.class);
        sendBtn = (Button) lookup("#sendBtn").queryAs(Button.class);
        chatBox = (TextArea) lookup("#chatBox").queryAs(TextArea.class);
        chatScrollPane = (ScrollPane) lookup("#chatScrollPane").queryAs(ScrollPane.class);
        paneInsideScrollPane = (VBox) lookup("#paneInsideScrollPane").queryAs(VBox.class);

    }

    @After
    public void tearDown() throws Exception {
        //chessboardGrid = null;
        turnLabel = null;
        forfeitBtn = null;
        sendBtn = null;
        chatBox = null;
        chatScrollPane = null;
        paneInsideScrollPane = null;
    }

    @Test
    public void testInitializeChessboardGrid() throws IOException, ParseException {
        assertNotNull(chessboardGrid);
        assertEquals(8, chessboardGrid.getRowCount());
        assertEquals(8, chessboardGrid.getColumnCount());

        for (Node node : chessboardGrid.getChildren()) {
            // Ensure the node is a Button
            assertNotNull(node);  // Check that the node is not null
            assertTrue(node instanceof Button);  // Check that the node is a Button
        }
    }

    @Test
    public void testOnCheckersButtonClick_IncompleteMove() throws IOException, ParseException {
        // Simulate clicking a valid piece at position (2, 2)
        int i = 2, j = 2;
        Button button22 = (Button) chessboardGrid.getChildren().get(18); // Adjust index based on layout
        clickOn(button22); // Simulate the first click
        String pieceStyleBefore = button22.getStyle(); // Store the initial style

        clickOn(button22); // Simulate the first click

        // Verify that the piece has not moved
        String pieceStyleAfter = button22.getStyle();
        assertEquals(pieceStyleBefore, pieceStyleAfter);
    }


//    @Test
//    public void testForfeitYesButtonClick()  {
//
//        assertTrue(forfeitBtn.getScene().getWindow().isFocused());
//        clickOn(forfeitBtn);
//        assertFalse(forfeitBtn.getScene().getWindow().isFocused());
//
//        assertTrue(true);
//
//        checkersController.onForfeitYesClick();
//
//    }

    @Test
    public void testForfeitNoButtonClick() {

        assertTrue(true);

        ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(consoleOutput));

        checkersController.onForfeitNoClick();  // Call the public forfeit-yes method

        System.setOut(System.out);  // Reset System.out
        assertTrue(consoleOutput.toString().contains("No"));  // Assert console output contains "Yes"
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
}