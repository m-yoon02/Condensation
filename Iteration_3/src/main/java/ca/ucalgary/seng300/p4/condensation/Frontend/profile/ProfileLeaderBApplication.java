package ca.ucalgary.seng300.p4.condensation.Frontend.profile;

import ca.ucalgary.seng300.p4.condensation.HelloApplication;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX application to launch the "Profile Leaderboard" screen.
 * This screen displays leaderboards for different games, such as Tic Tac Toe, Connect 4, and Checkers.
 */
public class ProfileLeaderBApplication extends Application {

    /**
     * Starts the JavaFX application.
     * Loads the FXML file for the "Profile Leaderboard" screen and sets up the stage.
     *
     * @param stage the primary stage for this application.
     * @throws IOException if the FXML file cannot be loaded.
     */
    @Override
    public void start(Stage stage) throws IOException {
        // Load the FXML file for the "Profile Leaderboard" screen
        Parent root = FXMLLoader.load(HelloApplication.class.getResource("ProfileLeaderB.fxml"));

        // Create a scene with the loaded FXML
        Scene scene = new Scene(root);

        // Set up the stage with the scene
        stage.setScene(scene);
        //stage.setTitle("Profile Leaderboard"); // Set the window title
        stage.show();
    }
}
