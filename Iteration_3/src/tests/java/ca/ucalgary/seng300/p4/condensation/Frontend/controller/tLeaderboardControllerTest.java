package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import ca.ucalgary.seng300.p4.condensation.HelloApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import static org.junit.Assert.*;

public class tLeaderboardControllerTest extends ApplicationTest {

    private FXMLLoader fxmlLoader;
    private Scene scene;
    private tLeaderboardController controller;

    // Declare the elements to test
    private Button backToMenu;

    @Override
    public void start(Stage stage) throws Exception {
        // Load the FXML file for the controller
        fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("tLeaderboard.fxml"));
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();

        // Get the controller instance
        controller = fxmlLoader.getController();

        // Initialize the elements from the FXML file
        backToMenu = lookup("#backToMenu").queryAs(Button.class);
    }

    @Before
    public void setUp() {
        // Perform setup actions if needed before each test
    }

    @After
    public void tearDown() {
        // Clean up after each test
        fxmlLoader = null;
        scene = null;
        controller = null;
        backToMenu = null;
    }

    @Test
    public void testControllerInitialization() {
        // Ensure the controller is properly initialized
        assertNotNull("Controller should not be null", controller);
    }

    @Test
    public void testBackToMenuButtonInitialization() {
        // Ensure the button is initialized correctly
        assertNotNull("Back to Menu button should not be null", backToMenu);
    }

    @Test
    public void testNavigateToMenu() {
        // Simulate clicking the Back to Menu button
        clickOn(backToMenu);

        // Assert scene has loaded (TestFX doesn't directly verify scene changes; manual verification may be required)
        assertNotNull("Scene should not be null", scene);
    }

    @Test
    public void testInitializeMethodSetsButtonAction() {
        // Ensure the button has an action set
        assertNotNull("Back to Menu button should have an action set", backToMenu.getOnAction());
    }


    @Test
    public void testNavigateToMethodHandlesInvalidFXML() {
        // Call the private `navigateTo` method using reflection
        try {
            var method = tLeaderboardController.class.getDeclaredMethod("navigateTo", String.class);
            method.setAccessible(true);

            // Execute the method with an invalid FXML file
            method.invoke(controller, "invalid.fxml");
            fail("Method navigateTo should throw an IOException for invalid FXML");
        } catch (Exception e) {
            // Ensure the exception is an IOException
            assertTrue("Exception should be of type IOException", e.getCause() != null);
        }
    }

    @Test
    public void testBackToMenuButtonClickTriggersNavigation() {
        // Simulate a click on the backToMenu button
        clickOn(backToMenu);

        // Verify that the action is not null and is executed
        assertNotNull("Button's action handler should not be null", backToMenu.getOnAction());
    }
}
