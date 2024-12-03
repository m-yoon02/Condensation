package ca.ucalgary.seng300.p4.condensation;

import ca.ucalgary.seng300.p4.condensation.Frontend.controller.CheckersController;
import ca.ucalgary.seng300.p4.condensation.Frontend.controller.CheckersHomeMenuController;
import ca.ucalgary.seng300.p4.condensation.Frontend.controller.ProfileGameController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("checkersHomeMenu.fxml"));
//        fxmlLoader.setController(null);
//        fxmlLoader.setController(new CheckersHomeMenuController(stage));
        Scene scene = new Scene(fxmlLoader.load(), 700, 500);

        Image appIcon = new Image(getClass().getResourceAsStream("logo.png"));
        stage.getIcons().add(appIcon);

        stage.setResizable(false);
        stage.setTitle("Condensation Application");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}