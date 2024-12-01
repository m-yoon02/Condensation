package ca.ucalgary.seng300.p4.condensation.Frontend.profile;

import ca.ucalgary.seng300.p4.condensation.HelloApplication;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class OtherProfileApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent root= FXMLLoader.load(HelloApplication.class.getResource("otherprofile.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
}

