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
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.io.IOException;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class EditUserControllerTest extends ApplicationTest {

    private TextField newUserNameField;
    private Label usernameLabel;
    private Label messageLabel;
    private Button confirmButton;

    private FXMLLoader fxmlLoader;
    private EditUserController editUserController;
    private UUID testPlayerUUID;

    @Override
    public void start(Stage stage) throws Exception {
        // Load the FXML file for the EditUserController
        fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("editUser.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();

        // Get the controller instance
        editUserController = fxmlLoader.getController();
    }

    @Before
    public void setUp() throws Exception {
        Platform.runLater(() -> {
            // Initialize UI components
            newUserNameField = (TextField) lookup("#newUserNameField").query();
            usernameLabel = (Label) lookup("#usernameLabel").query();
            messageLabel = (Label) lookup("#messageLabel").query();
            confirmButton = (Button) lookup("#confirmButton").query();

            // Set up test UUID and initialize it in the controller
            testPlayerUUID = UUID.randomUUID();
            EditUserController.setPlayerUUID(testPlayerUUID);

            // Create a test user in the Leaderboard
            try {
                Leaderboard.getInstance().createUser("OldUsername", "password123");
                Leaderboard.getInstance().changeName(testPlayerUUID, "OldUsername");
            } catch (IOException | ParseException e) {
                throw new RuntimeException(e);
            }
        });
        WaitForAsyncUtils.waitForFxEvents();
    }

    @After
    public void tearDown() {
        Platform.runLater(() -> {
            // Clean up the test user in the Leaderboard
            try {
                JSONObject testUser = Leaderboard.getInstance().findUserById(testPlayerUUID);
                if (testUser != null) {
                    Leaderboard.getInstance().getAllUsers().remove(testUser);
                    Leaderboard.getInstance().saveData();
                }
            } catch (IOException | ParseException e) {
                throw new RuntimeException(e);
            }
            newUserNameField.clear();
            messageLabel.setText(""); // Reset label text
        });
        WaitForAsyncUtils.waitForFxEvents();
    }

    /*@Test
    public void testUsernameChangeSuccess() throws IOException, ParseException {
        Platform.runLater(() -> {
            // Simulate valid username entry
            newUserNameField.setText("NewValidUsername");

            // Mock profileHomeController dependency
            ProfileHomeController mockController = new ProfileHomeController();
            editUserController.setProfileHomeController(mockController);

            // Simulate clicking the confirm button
            confirmButton.fire();
        });
        WaitForAsyncUtils.waitForFxEvents();

        // Verify that the messageLabel updates correctly
        assertEquals("Changed Username successfully", messageLabel.getText());

        // Verify the username in the Leaderboard
        JSONObject updatedUser = Leaderboard.getInstance().findUserById(testPlayerUUID);
        assertEquals("NewValidUsername", updatedUser.get("username"));
    }*/
    @Test
    public void testUsernameChangeSuccess() throws IOException, ParseException {
        Platform.runLater(() -> {
            // Simulate valid username entry
            newUserNameField.setText("NewValidUsername");

            // Simulate clicking the confirm button
            confirmButton.fire();
        });
        WaitForAsyncUtils.waitForFxEvents();

        // Verify that the messageLabel updates correctly
        assertEquals("Changed Username successfully", messageLabel.getText());

        // Verify the username in the Leaderboard
        JSONObject updatedUser = Leaderboard.getInstance().findUserById(testPlayerUUID);
        assertEquals("NewValidUsername", updatedUser.get("username"));
    }


    /*@Test
    public void testInvalidUsernameBlank() {
        Platform.runLater(() -> {
            // Set an empty username in the text field and simulate the button click
            newUserNameField.setText("");
            confirmButton.fire();
        });
        WaitForAsyncUtils.waitForFxEvents();

        // Verify the message label does not indicate success
        assertEquals("", messageLabel.getText());
        assertEquals("0xff0000ff", usernameLabel.getTextFill().toString()); // RED color
    }*/

    /*@Test
    public void testInvalidUsernameShort() {
        Platform.runLater(() -> {
            // Set a too-short username in the text field and simulate the button click
            newUserNameField.setText("ab");
            confirmButton.fire();
        });
        WaitForAsyncUtils.waitForFxEvents();

        // Verify the message label does not indicate success
        assertEquals("", messageLabel.getText());
        assertEquals("0xff0000ff", usernameLabel.getTextFill().toString()); // RED color
    }*/
}

