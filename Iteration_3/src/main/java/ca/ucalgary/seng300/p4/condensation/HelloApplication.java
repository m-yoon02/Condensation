package ca.ucalgary.seng300.p4.condensation;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("WelcomeScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Image appIcon = new Image(getClass().getResourceAsStream("logo.png"));
        stage.getIcons().add(appIcon);

        stage.setResizable(false);
        stage.setTitle("Condensation Application");
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(e -> Platform.exit());
    }

    public static void main(String[] args) {
        launch();
    }
}