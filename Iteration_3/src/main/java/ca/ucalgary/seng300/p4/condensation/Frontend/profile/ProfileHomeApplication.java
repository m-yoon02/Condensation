package ca.ucalgary.seng300.p4.condensation.Frontend.profile;

import ca.ucalgary.seng300.p4.condensation.HelloApplication;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ProfileHomeApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent root= FXMLLoader.load(HelloApplication.class.getResource("ProfileHome.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        /*FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("profileHome.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Home");
        stage.setScene(scene);
        stage.show();*/
    }

    /*public static void main(String[] args) {
        launch();
    }*/
}