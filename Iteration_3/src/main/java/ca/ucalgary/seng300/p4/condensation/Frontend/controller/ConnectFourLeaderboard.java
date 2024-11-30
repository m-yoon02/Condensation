package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

public class ConnectFourLeaderboard {

    @FXML
    private TableView<ConnectFourHomeMenuController.Player> leaderboardTable;

    @FXML
    private TableColumn<ConnectFourHomeMenuController.Player, Integer> rankColumn;

    @FXML
    private TableColumn<ConnectFourHomeMenuController.Player, String> nameColumn;

    @FXML
    private TableColumn<ConnectFourHomeMenuController.Player, Integer> gamesPlayedColumn;

    @FXML
    private TableColumn<ConnectFourHomeMenuController.Player, Integer> winsColumn;

    @FXML
    private TableColumn<ConnectFourHomeMenuController.Player, Integer> lossesColumn;

    @FXML
    private TableColumn<ConnectFourHomeMenuController.Player, Integer> drawsColumn;

    @FXML
    private TableColumn<ConnectFourHomeMenuController.Player, Double> winPercentageColumn;

    private ObservableList<ConnectFourHomeMenuController.Player> playerList;

    @FXML
    public void initialize() {
        // Bind each column's preferred width to a percentage of the table's total width
        rankColumn.prefWidthProperty().bind(leaderboardTable.widthProperty().multiply(0.05));
        nameColumn.prefWidthProperty().bind(leaderboardTable.widthProperty().multiply(0.4));
        gamesPlayedColumn.prefWidthProperty().bind(leaderboardTable.widthProperty().multiply(0.1));
        winsColumn.prefWidthProperty().bind(leaderboardTable.widthProperty().multiply(0.1));
        lossesColumn.prefWidthProperty().bind(leaderboardTable.widthProperty().multiply(0.1));
        drawsColumn.prefWidthProperty().bind(leaderboardTable.widthProperty().multiply(0.1));
        winPercentageColumn.prefWidthProperty().bind(leaderboardTable.widthProperty().multiply(0.15));

        // Initialize the columns
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        gamesPlayedColumn.setCellValueFactory(new PropertyValueFactory<>("gamesPlayedCheckers"));
        winsColumn.setCellValueFactory(new PropertyValueFactory<>("winsInCheckers"));
        lossesColumn.setCellValueFactory(new PropertyValueFactory<>("lossInCheckers"));

        // Center-align all numeric columns
        TableColumn<?, ?>[] numericColumns = {
                rankColumn, gamesPlayedColumn, winsColumn, lossesColumn, drawsColumn, winPercentageColumn
        };

        for (TableColumn<?, ?> column : numericColumns) {
            column.setStyle("-fx-alignment: CENTER;"); // Center-align the whole column
        }



        // Calculate draws
        drawsColumn.setCellValueFactory(data -> {
            ConnectFourHomeMenuController.Player player = data.getValue();
            int totalGames = player.getGamesPlayedCheckers();
            int totalOutcomes = player.getWinsInCheckers() + player.getLossInCheckers();
            int draws = totalGames - totalOutcomes;
            return new ReadOnlyObjectWrapper<>(Math.max(draws, 0));
        });

        // Calculate win percentage
        winPercentageColumn.setCellValueFactory(data -> {
            ConnectFourHomeMenuController.Player player = data.getValue();
            int totalGames = player.getGamesPlayedCheckers();
            double percentage = totalGames > 0 ? (player.getWinsInCheckers() * 100.0) / totalGames : 0;
            return new ReadOnlyObjectWrapper<>(Math.round(percentage * 100.0) / 100.0);
        });

        // Populate the player list
        playerList = FXCollections.observableArrayList(ConnectFourHomeMenuController.mainTeam.getPlayers());

        // Sort the player list by win percentage (descending)
        ObservableList<ConnectFourHomeMenuController.Player> sortedList = FXCollections.observableArrayList(playerList);
        sortedList.sort((p1, p2) -> {
            double p1Percentage = p1.getGamesPlayedCheckers() > 0
                    ? (p1.getWinsInCheckers() * 100.0) / p1.getGamesPlayedCheckers()
                    : 0;
            double p2Percentage = p2.getGamesPlayedCheckers() > 0
                    ? (p2.getWinsInCheckers() * 100.0) / p2.getGamesPlayedCheckers()
                    : 0;
            return Double.compare(p2Percentage, p1Percentage); // Descending order
        });

        // Dynamically assign ranks based on sorted order
        rankColumn.setCellValueFactory(data -> {
            ConnectFourHomeMenuController.Player player = data.getValue();
            int rank = sortedList.indexOf(player) + 1;
            return new ReadOnlyObjectWrapper<>(rank);
        });

        // Set the sorted list to the table
        leaderboardTable.setItems(sortedList);
    }

    private Scene scene;

    @FXML
    private void handleBackButton(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/ca/ucalgary/seng300/p4/condensation/ConnectFourHomeMenu.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Connect 4 - Menu");
        stage.setScene(scene);
        stage.show();
    }
}
