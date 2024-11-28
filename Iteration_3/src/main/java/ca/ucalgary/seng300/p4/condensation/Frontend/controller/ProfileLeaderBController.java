package ca.ucalgary.seng300.p4.condensation.Frontend.controller;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ProfileLeaderBController {
    @FXML
    private Label lblLeaderboard;

    @FXML
    private Button btnHome;

    @FXML
    private TableView<?> tableView1;

    @FXML
    private TableView<?> tableView2;

    @FXML
    private TableView<?> tableView3;

    @FXML
    private TableColumn<?, ?> colRank1;

    @FXML
    private TableColumn<?, ?> colPlayer1;

    @FXML
    private TableColumn<?, ?> colScore1;

    @FXML
    private TableColumn<?, ?> colRank2;

    @FXML
    private TableColumn<?, ?> colPlayer2;

    @FXML
    private TableColumn<?, ?> colScore2;

    @FXML
    private TableColumn<?, ?> colRank3;

    @FXML
    private TableColumn<?, ?> colPlayer3;

    @FXML
    private TableColumn<?, ?> colScore3;


    @FXML
    private Label lblTicTacToe;

    @FXML
    private Label lblCheckers;

    @FXML
    private Label lblConnect4;

    @FXML
    private void initialize() {
        // Initialization logic here
        lblLeaderboard.setText("Leaderboard");
        lblTicTacToe.setText("TIC TAC TOE");
        lblCheckers.setText("CHECKERS");
        lblConnect4.setText("CONNECT 4");
    }

    @FXML
    private void handleHome() {
        System.out.println("Home button clicked!");
        // Add logic to handle the Home button click, like navigating to the home screen
    }
}
