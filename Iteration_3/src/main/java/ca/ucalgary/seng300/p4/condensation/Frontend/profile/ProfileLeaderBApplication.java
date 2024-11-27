package ca.ucalgary.seng300.p4.condensation.Frontend.profile;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ProfileLeaderBApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ProfileLeaderB.fxml"));
        Scene scene = new Scene(loader.load());
        primaryStage.setTitle("Leaderboard");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
