/**
 * JavaFX application to launch the "Profile Game" screen.
 * This screen allows users to select a game (Tic Tac Toe, Connect 4, or Checkers) to play.
 *
 * Note: This class won't be invoked when the program starts from ProfileHomeApplication.
 * It is intended for standalone testing or direct access to the "Profile Game" screen.
 */

package ca.ucalgary.seng300.p4.condensation.Frontend.profile;

import ca.ucalgary.seng300.p4.condensation.HelloApplication;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ProfileGameApplication extends Application {

    /**
     * Starts the JavaFX application.
     * Loads the FXML file for the "Profile Game" screen and sets up the stage.
     *
     * @param stage the primary stage for this application.
     * @throws IOException if the FXML file cannot be loaded.
     */
    @Override
    public void start(Stage stage) throws IOException {
        // Load the FXML file for the "Profile Game" screen
        Parent root = FXMLLoader.load(HelloApplication.class.getResource("ProfileGame.fxml"));

        // Create a scene with the loaded FXML
        Scene scene = new Scene(root);

        // Set up the stage with the scene
        stage.setScene(scene);
        stage.show();
    }
}
