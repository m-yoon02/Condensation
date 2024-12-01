package ca.ucalgary.seng300.p4.condensation.Frontend.profile;

import ca.ucalgary.seng300.p4.condensation.HelloApplication;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX application to launch the "Profile Home" screen.
 * This screen serves as the main hub for navigating to other sections
 * like Profile Lookup, Game Selection, and Leaderboard.
 */
public class ProfileHomeApplication extends Application {

    /**
     * Starts the JavaFX application.
     * Loads the FXML file for the "Profile Home" screen and sets up the stage.
     *
     * @param stage the primary stage for this application.
     * @throws IOException if the FXML file cannot be loaded.
     */
    @Override
    public void start(Stage stage) throws IOException {
        // Load the FXML file for the "Profile Home" screen
        Parent root = FXMLLoader.load(HelloApplication.class.getResource("ProfileHome.fxml"));

        // Create a scene with the loaded FXML
        Scene scene = new Scene(root);

        // Set up the stage with the scene
        stage.setScene(scene);
        //stage.setTitle("Profile Home"); // Set the window title
        stage.show();
    }
}
