package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import ca.ucalgary.seng300.p4.condensation.HelloApplication;
import ca.ucalgary.seng300.p4.condensation.Networking.Database.Leaderboard.Leaderboard;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.simple.parser.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.io.IOException;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class EditPassControllerTest extends ApplicationTest {

    private TextField newPassField;
    private Label passwordLabel;
    private Label messageLabel;
    private Button confirmButton;

    private FXMLLoader fxmlLoader;
    private EditPassController editPassController;
    private UUID testPlayerUUID;

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
            // Initialize UI components
            newPassField = (TextField) lookup("#newPassField").query();
            passwordLabel = (Label) lookup("#passwordLabel").query();
            messageLabel = (Label) lookup("#messageLabel").query();
            confirmButton = (Button) lookup("#confirmButton").query();

            // Set up test UUID and initialize it in the controller
            testPlayerUUID = UUID.randomUUID();
            EditPassController.setPlayerUUID(testPlayerUUID);

            // Set up the Leaderboard instance
            try {
                Leaderboard.getInstance().changePassword(testPlayerUUID, null); // Ensure a clean slate for testing
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });
        WaitForAsyncUtils.waitForFxEvents();
    }

    @After
    public void tearDown() {
        Platform.runLater(() -> {
            // Clean up any test data from Leaderboard
            try {
                Leaderboard.getInstance().changePassword(testPlayerUUID, null);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            newPassField.clear();
        });
        WaitForAsyncUtils.waitForFxEvents();

        newPassField = null;
        passwordLabel = null;
        messageLabel = null;
        confirmButton = null;
    }

    @Test
    public void testPasswordChangeSuccess() throws IOException, ParseException {
        Platform.runLater(() -> {
            // Set a valid password in the text field and simulate the button click
            newPassField.setText("ValidPassword123");
            confirmButton.fire();
        });
        WaitForAsyncUtils.waitForFxEvents();

        // Verify the message label updates correctly
        assertEquals("Changed Password successfully", messageLabel.getText());
        assertEquals("0x008000ff", messageLabel.getTextFill().toString()); // GREEN color

        // Verify the password in the Leaderboard
        String updatedPassword = String.valueOf(Leaderboard.getInstance().changePassword(testPlayerUUID, null));
        assertEquals("ValidPassword123", updatedPassword);
    }
}
