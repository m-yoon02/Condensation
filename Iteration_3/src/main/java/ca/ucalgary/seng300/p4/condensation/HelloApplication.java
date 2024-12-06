package ca.ucalgary.seng300.p4.condensation;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    private static Stage mainStage;

    @Override
    public void start(Stage stage) throws IOException {
        mainStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("WelcomeScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Image appIcon = new Image(getClass().getResourceAsStream("logo.png"));
        stage.getIcons().add(appIcon);

        stage.setResizable(false);
        stage.setTitle("Condensation Application");
        setActiveScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    /**
     * Sets a new active {@link Scene} on the stage, and sets the title
     *
     * @param newActiveScene the new scene to show
     */
    public static void setActiveScene(Scene newActiveScene, String title) {
        mainStage.setScene(newActiveScene);
        mainStage.setMinWidth(newActiveScene.getRoot().minWidth(-1));
        mainStage.setMinHeight(newActiveScene.getRoot().minHeight(-1));
        if (title != null) {
            mainStage.setTitle(title);
        }
    }

    /**
     * Sets a new active {@link Scene} on the stage.
     *
     * @param newActiveScene the new scene to show
     */
    public static void setActiveScene(Scene newActiveScene) {
        setActiveScene(newActiveScene, null);
    }
}