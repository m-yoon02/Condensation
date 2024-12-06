package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import ca.ucalgary.seng300.p4.condensation.HelloApplication;
import ca.ucalgary.seng300.p4.condensation.Networking.Database.Leaderboard.Leaderboard;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.json.simple.parser.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.io.IOException;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Test class for EditPassController.
 * This class tests the functionality of changing a player's password in the Checkers application.
 */
public class EditPassControllerTest extends ApplicationTest {

    private TextField newPassField;
    private Label passwordLabel;
    private Label messageLabel;
    private Button confirmButton;

    private FXMLLoader fxmlLoader;
    private EditPassController editPassController;
    private UUID testPlayerUUID;

    private Leaderboard leaderboard;

    @Override
    public void start(Stage stage) throws Exception {
        // Load the FXML file for the EditPassController
        fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("editPass.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();

        // Get the controller instance
        editPassController = fxmlLoader.getController();
    }

    @Before
    public void setUp() throws Exception {
        Platform.runLater(() -> {
            // Initialize UI components using TestFX's lookup method
            newPassField = lookup("#newPassField").queryAs(TextField.class);
            passwordLabel = lookup("#passwordLabel").queryAs(Label.class);
            messageLabel = lookup("#messageLabel").queryAs(Label.class);
            confirmButton = lookup("#confirmButton").queryAs(Button.class);

            // Initialize the Leaderboard instance
            try {
                leaderboard = Leaderboard.getInstance();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            // Set up a test UUID and initialize it in the controller
            testPlayerUUID = UUID.randomUUID();
            EditPassController.setPlayerUUID(testPlayerUUID);

            // Set an initial password for the test player in the Leaderboard to ensure a known state
            leaderboard.changePassword(testPlayerUUID, "InitialPass123");
        });

        // Wait for all JavaFX events to be processed
        WaitForAsyncUtils.waitForFxEvents();
    }

    @After
    public void tearDown() {
        Platform.runLater(() -> {
            leaderboard.changePassword(testPlayerUUID, "InitialPass123");

            newPassField.clear();
            messageLabel.setText("");
            messageLabel.setTextFill(Color.BLACK);
            passwordLabel.setTextFill(Color.BLACK);
        });

        WaitForAsyncUtils.waitForFxEvents();

        newPassField = null;
        passwordLabel = null;
        messageLabel = null;
        confirmButton = null;
    }

    /**
     * Test to verify that attempting to change the password with a blank input fails and updates the UI correctly.
     */
    @Test
    public void testPasswordChangeBlank() throws IOException, ParseException {
        Platform.runLater(() -> {
            newPassField.setText("");
            confirmButton.fire();
        });

        WaitForAsyncUtils.waitForFxEvents();

        assertEquals(Color.RED, passwordLabel.getTextFill());

        assertNotEquals("Changed Password successfully", messageLabel.getText());
    }

    /**
     * Test to verify that attempting to change the password with a short input fails and updates the UI correctly.
     */
    @Test
    public void testPasswordChangeTooShort() throws IOException, ParseException {
        Platform.runLater(() -> {
            newPassField.setText("ab"); // Less than 3 characters
            confirmButton.fire();
        });

        WaitForAsyncUtils.waitForFxEvents();

        assertEquals(Color.RED, passwordLabel.getTextFill());

        assertNotEquals("Changed Password successfully", messageLabel.getText());
    }

    /**
     * Test to verify that attempting to change the password fails gracefully when Leaderboard returns false.
     * This requires Leaderboard to be mockable or to simulate failure conditions.
     */
    @Test
    public void testPasswordChangeFailure() throws IOException, ParseException {
        String failingPassword = "fail";

        Platform.runLater(() -> {
            newPassField.setText(failingPassword);
            confirmButton.fire();
        });

        WaitForAsyncUtils.waitForFxEvents();
        assertNotEquals("Changed Password successfully", messageLabel.getText());
    }
}
