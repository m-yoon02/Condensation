package ca.ucalgary.seng300.p4.condensation.Frontend.profile;

import ca.ucalgary.seng300.p4.condensation.HelloApplication;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX application to launch the "Other Profile" screen.
 * This screen allows users to search and view profiles of other players.
 */
public class OtherProfileApplication extends Application {

    /**
     * Starts the JavaFX application.
     * Loads the FXML file for the "Other Profile" screen and sets the stage.
     *
     * @param stage the primary stage for this application.
     * @throws IOException if the FXML file cannot be loaded.
     */
    @Override
    public void start(Stage stage) throws IOException {
        // Load the FXML file for the "Other Profile" screen
        Parent root = FXMLLoader.load(HelloApplication.class.getResource("otherprofile.fxml"));

        // Create a scene with the loaded FXML
        Scene scene = new Scene(root);

        // Set the scene to the stage and show the application
        stage.setScene(scene);
        stage.setTitle("Other Profile Lookup"); // Set the window title
        stage.show();
    }
}
