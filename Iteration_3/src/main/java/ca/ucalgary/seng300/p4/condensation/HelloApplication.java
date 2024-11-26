package ca.ucalgary.seng300.p4.condensation;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the Aftermath.fxml file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/ucalgary/seng300/p4/condensation/Aftermath.fxml"));
        Parent root = loader.load();

        // Set up the stage
        primaryStage.setTitle("Aftermath Screen");
        primaryStage.setScene(new Scene(root, 600, 400)); // Set dimensions
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
