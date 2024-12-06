package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import ca.ucalgary.seng300.p4.condensation.HelloApplication;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
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
import java.util.ArrayList;
import java.util.UUID;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.testfx.util.WaitForAsyncUtils;

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
    Scene scene;

    CheckersController checkersController;
    LoginController loginController;
    CheckersQueueController checkersQueueController;
    HelloApplication application;

    @Override
    public void start(Stage stage) throws Exception {

        application = new HelloApplication();
        application.start(stage);

        fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Login.fxml"));
        scene = new Scene(fxmlLoader.load());
        loginController = fxmlLoader.getController();
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

        fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("checkersqueue.fxml"));
        scene = new Scene(fxmlLoader.load());
        checkersQueueController = fxmlLoader.getController();
        stage.setScene(scene);
        stage.show();

        checkersQueueController.start();

        fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("checkersInstance.fxml"));
        scene = new Scene(fxmlLoader.load());
        checkersController = fxmlLoader.getController();
        stage.setScene(scene);
        stage.show();

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
    public void testDisableButtons() throws IOException {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("UpdateType", "GAME_END");
        Platform.runLater(() -> {
            try {
                checkersController.receiveCheckers(jsonObject);
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
    public void testYourTurn(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("UpdateType", "YOUR_TURN");
        jsonObject.put("SessionID", UUID.randomUUID());
        jsonObject.put("PlayerTurn", UUID.randomUUID());

        JSONArray jArray = new JSONArray();
        jsonObject.put("Changes",jArray);

        for (int i = 0; i < 8; i++){
            JSONArray outerArray = new JSONArray();
            for (int j = 0; j < 8; j++){
                if(i == 0 || i == 1 || i == 2 || i == 3 || i == 4 || i == 5 || i == 6 || i == 7 || i == 8) {
                    outerArray.add("RED");
                }
                else{
                    outerArray.add("Black");
                }
            }
            jArray.add(outerArray);
        }


        Platform.runLater(() -> {
            try {
                checkersController.receiveCheckers(jsonObject);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }


    @Test
    public void testOnCheckersButtonClick_IncompleteMove() throws IOException, ParseException {
        // Ensure chessboardGrid is not null
        assertNotNull("chessboardGrid should not be null", chessboardGrid);

        int row = 2, col = 2;
        int index = row * 8 + col;

        Node node = chessboardGrid.getChildren().get(index);
        assertTrue("Node at (2,2) should be a Button", node instanceof Button);
        Button button22 = (Button) node;

        assertNotNull("Button at (2,2) should not be null", button22);

        String pieceStyleBefore = button22.getStyle();

        clickOn(button22);

        WaitForAsyncUtils.waitForFxEvents();

        clickOn(button22);

        WaitForAsyncUtils.waitForFxEvents();

        String pieceStyleAfter = button22.getStyle();

        assertEquals("Piece style should remain unchanged after incomplete move", pieceStyleBefore, pieceStyleAfter);

        assertNotNull("Piece should still be present at (2,2)", button22.getGraphic());

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if (r == row && c == col) continue;
                int currentIndex = r * 8 + c;
                Node currentNode = chessboardGrid.getChildren().get(currentIndex);
                if (currentNode instanceof Button) {
                    Button currentButton = (Button) currentNode;
                    if (currentButton.getGraphic() != null) {
                        assertFalse("Piece should not have moved from (2,2) to (" + c + "," + r + ")",
                                currentButton.getGraphic().equals(button22.getGraphic()));
                    }
                }
            }
        }
    }

    // Casting issues from UUID -> String need to fixed. Or else both tests should work


    @Test
    public void testHandleGameEnd_WinByPlayerOne() throws Exception {
        assertNotNull("turnLabel should not be null", turnLabel);

        String playerOneUUIDStr = "123e4567-e89b-12d3-a456-426614174123";

        JSONObject payload = new JSONObject();
        payload.put("GameState", "WIN");
        payload.put("PlayerWon", playerOneUUIDStr);

        Platform.runLater(() -> {
            try {
                checkersController.handleGameEnd(payload);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        Thread.sleep(500);

        String expectedText = String.format("Player %s won the game!", playerOneUUIDStr);
        assertEquals("The turnLabel should indicate that player one has won the game.", expectedText, turnLabel.getText());
    }

    @Test
    public void testHandleGameEnd_Tie() throws Exception {
        JSONObject payload = new JSONObject();
        payload.put("GameState", "TIE");

        Platform.runLater(() -> {
            try {
                checkersController.handleGameEnd(payload);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        WaitForAsyncUtils.waitForFxEvents();

        assertEquals("The game ended in a tie!", turnLabel.getText());
    }


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
        assertNotNull("chatBox should not be null", chatBox);

        interact(() -> {
            chatBox.setText("Test chat message");
        });

        clickOn(sendBtn);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int messageCount = paneInsideScrollPane.getChildren().size();
        assertTrue("Chat pane should have at least one message", messageCount > 0);

        HBox messageHBox = (HBox) paneInsideScrollPane.getChildren().get(messageCount - 1);
        VBox messageBox = (VBox) messageHBox.getChildren().get(0);
        Label messageLabel = (Label) messageBox.getChildren().get(1);

        assertEquals("The message text should match the input text", "Test chat message", messageLabel.getText());

        HBox nameTimeHBox = (HBox) messageBox.getChildren().get(0);
        Label timeSentLabel = (Label) nameTimeHBox.getChildren().get(1);

        String timeSent = timeSentLabel.getText();
        assertNotNull("Time sent should not be null", timeSent);
        assertTrue("Time sent should match the expected format yyyy-MM-dd HH:mm",
                timeSent.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}"));

        assertEquals("Chat box should be cleared after sending the message", "", chatBox.getText());
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