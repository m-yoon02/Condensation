package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import ca.ucalgary.seng300.p4.condensation.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class ProfileHomeController {

    @FXML
    private TableView<?> tableView;

    @FXML
    private TextField profileTitle;

    @FXML
    private TextField usernameField;

    @FXML
    private Button logoutButton;

    @FXML
    private Button leaderboardButton;

    @FXML
    private Button playerProfilesButton;

    @FXML
    private Button gamesButton;

    /**When this method is called, it will change the scene back to log in page.*/
    @FXML
    public void logOutButtonPushed(ActionEvent event) {
        System.out.println("Logging out...");
        // Add logout functionality here
    }

    //When this method is called, it will change the scene to leaderboard page.
    @FXML
    void handleLeaderboard(ActionEvent event) {
        System.out.println("Opening Leaderboard...");
        // Add functionality to open the leaderboard page
    }

    //When this method is called, it will change the scene back to player profile page.
    @FXML
    void handlePlayerProfiles(ActionEvent event) {
        System.out.println("Opening Player Profiles...");
        // Add functionality to open the player profiles page
    }

    //When this method is called, it will change the scene back to game selection page.
    @FXML
    void onGamesButtonClicked(ActionEvent event) throws IOException {
        System.out.println("Opening Games...");
        // Add functionality to open the games page
        Parent gamesParent = FXMLLoader.load(HelloApplication.class.getResource("ProfileGame.fxml"));
        Scene gamesScene = new Scene(gamesParent);

        //This line gets the Stage information
        Stage window= (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(gamesScene);
        window.show();

    }
}


