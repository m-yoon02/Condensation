package  ca.ucalgary.seng300.p4.condensation.Frontend.profile;

import ca.ucalgary.seng300.p4.condensation.HelloApplication;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ProfileHomeApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                ProfileHomeApplication.class.getResource("ca.ucalgary.seng300.p4.condensation.ProfileHome.fxml")
                );
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Home - My Profile");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
