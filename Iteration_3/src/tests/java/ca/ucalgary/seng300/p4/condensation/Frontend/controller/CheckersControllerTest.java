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
        sendBtn = null;
        chatBox = null;
        chatScrollPane = null;
        paneInsideScrollPane = null;
    }

    @Test
    public void testInitializeChessboardGrid() throws IOException, ParseException {
        checkersController.initialize();
        assertTrue(true);
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
    public void testForfeitButtonClick()  {
        ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(consoleOutput));

        checkersController.onForfeitBtnClick();

        Alert alert = checkersController.getAlert();
        assertNotNull(alert);

        assertEquals("Are you sure you want to forfeit?", alert.getContentText());
    }

    @Test
    public void testForfeitYesButtonCallsCorrectLogic() {
        ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(consoleOutput));

        checkersController.onForfeitYesClick();  // Call the public forfeit-yes method

        System.setOut(System.out);  // Reset System.out
        assertTrue(consoleOutput.toString().contains("Yes"));  // Assert console output contains "Yes"
    }

    @Test
    public void testForfeitNoButtonCallsCorrectLogic() {
        ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(consoleOutput));

        checkersController.onForfeitNoClick();  // Call the public forfeit-yes method

        System.setOut(System.out);  // Reset System.out
        assertTrue(consoleOutput.toString().contains("No"));  // Assert console output contains "Yes"
    }

    @Test
    public void testSendButtonMessageHandling() {
        // Set up the chatBox with a test message
        chatBox.setText("Test chat message");

        // Trigger the send button click, which should process the message
        checkersController.onSendBtnClick();  // Call the send button method

        // Ensure the chatBox is cleared after sending the message
        assertEquals("", chatBox.getText());

        // Verify that a new message was added to the paneInsideScrollPane
        assertFalse(paneInsideScrollPane.getChildren().isEmpty());  // Ensure that the pane is not empty
        HBox lastMessage = (HBox) paneInsideScrollPane.getChildren().get(paneInsideScrollPane.getChildren().size() - 1);
        VBox messageBox = (VBox) lastMessage.getChildren().get(0);
        Label messageLabel = (Label) messageBox.getChildren().get(1);

        // Verify that the last message is the one we sent
        assertEquals("Test chat message", messageLabel.getText());  // Verify that the last message is the one we sent
    }
}