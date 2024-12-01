package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import ca.ucalgary.seng300.p4.condensation.Backend.DTOs.Vector2;
import ca.ucalgary.seng300.p4.condensation.Backend.Enums.GameResult;
import ca.ucalgary.seng300.p4.condensation.Networking.Managers.NetworkManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

public class ConnectFourController {

//    private Button btn00, btn01, btn02, btn10, btn11, btn12, btn20, btn21, btn22;
//    public int numberOfPiecesOnBoard = 0;
//    Button[][] buttons = new Button[][] {};
//
//    public static final int NUMBER_OF_ROWS = 6;
//    public static final int NUMBER_OF_COLS = 7;
//    public GameResult resultOfTicTacToe;
//    public UUID playerOne;
//    public UUID playerTwo;
//    public UUID sessionID;
//    public UUID newTurn;
//    public void receiveConnectFour(JSONObject backendUpdate){
//        String win_or_not_win = (String) backendUpdate.get("UpdateType");
//        if (Objects.equals(win_or_not_win, "GAME_END")){
//            resultOfTicTacToe = (GameResult) backendUpdate.get("GameState");
//            if (resultOfTicTacToe == GameResult.WIN){
//                disableAllButtons();
//                // Switch to aftermath, send data now????
//                // Notification
//            }
//            else{
//                //Tie -> send data to Aftermath
//            }
//        }
//        else{
//            sessionID = (UUID) backendUpdate.get("SessionID");
//            newTurn = (UUID) backendUpdate.get("PlayerTurn");
//            String[][] connectFourBackendBoard = (String[][]) backendUpdate.get("Changes");
//            int rowIndex = 0;
//            int colIndex;
//            for (String[] strings : connectFourBackendBoard) {
//                colIndex = 0;
//                for (String piece : strings) {
//                    Button buttonForBoard = buttons[rowIndex][colIndex];
//                    if (Objects.equals(piece, "RED")){
//                        buttonForBoard.setText("O");
//                        buttonForBoard.setTextFill(Color.RED);
//                    }
//                    else if (Objects.equals(piece, "BLUE")){
//                        buttonForBoard.setText("O");
//                        buttonForBoard.setTextFill(Color.BLUE);
//                    }
//                    else{
//                        buttonForBoard.setText("");
//                    }
//                }
//                rowIndex += 1;
//            }
//        }
//    }
//
//    // Activates when a tile is clicked.
//    public void returnConnectFour(javafx.event.ActionEvent event) throws IOException, ParseException {
//        Button clickedButton = (Button) event.getSource();
//
//        int row = -1;
//        int col = -1;
//
//        boolean buttonFound = false;
//        for (int i = 0; i < NUMBER_OF_ROWS; i++) {
//            if (buttonFound){
//                break;
//            }
//            for (int j = 0; j < NUMBER_OF_COLS; j++) {
//                if (buttons[i][j] == clickedButton) {
//                    row = i;
//                    col = j;
//                    buttonFound = true;
//                    break;
//                }
//            }
//        }
//
//        if (row > 0 && col > 0){
//
//            JSONObject connectFourJSON = new JSONObject();
//            connectFourJSON.put("SessionID", sessionID);
//            if (newTurn == playerOne){
//                connectFourJSON.put("Player ID", playerOne);
//            }
//            else{
//                connectFourJSON.put("Player ID", playerTwo);
//            }
//            connectFourJSON.put("New Position", new Vector2(row, col));
//            numberOfPiecesOnBoard += 1;
//            JSONObject results = NetworkManager.getNetworkInstance().handleQueuePlayer(connectFourJSON);
//            receiveConnectFour(results);
//        }
//    }
//
//    private void disableAllButtons() { //when game ends
//        for (Button[] buttonRow : buttons) {
//            for (Button button : buttonRow) {
//                button.setDisable(true);
//            }
//        }
//    }


    /* Part needs Integration */

    @FXML
    private GridPane connectFourGrid;

    @FXML
    private Text forfeitButton;

    @FXML
    private Text playerTurnText;

    private Button[][] buttons = new Button[6][7];

    private String jsonUpdate;

    @FXML
    public void initialize() {
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                String buttonId = "btn" + row + col;
                Button button = (Button) connectFourGrid.lookup("#" + buttonId);
                buttons[row][col] = button;
            }
        }

        // Example JSON string (simulate backend update)
        jsonUpdate = "{ " +
                "\"UpdateType\": \"TURN_END\", " +
                "\"SessionID\": \"d290f1ee-6c54-4b01-90e6-d701748f0851\", " +
                "\"PlayerTurn\": \"345e6789-e89d-45d5-a678-627836285111\", " +
                "\"Changes\": [ " +
                "[\"EMPTY\", \"EMPTY\", \"EMPTY\", \"EMPTY\", \"EMPTY\", \"EMPTY\", \"EMPTY\"], " +
                "[\"EMPTY\", \"EMPTY\", \"EMPTY\", \"EMPTY\", \"EMPTY\", \"EMPTY\", \"EMPTY\"], " +
                "[\"EMPTY\", \"EMPTY\", \"EMPTY\", \"RED\", \"EMPTY\", \"EMPTY\", \"EMPTY\"], " +
                "[\"EMPTY\", \"EMPTY\", \"BLUE\", \"RED\", \"EMPTY\", \"EMPTY\", \"EMPTY\"], " +
                "[\"EMPTY\", \"RED\", \"BLUE\", \"BLUE\", \"EMPTY\", \"EMPTY\", \"EMPTY\"], " +
                "[\"RED\", \"BLUE\", \"RED\", \"RED\", \"BLUE\", \"EMPTY\", \"EMPTY\"] " +
                "]" +
                "}";

        processUpdate(jsonUpdate);
    }

    public void processUpdate(String jsonString) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject update = (JSONObject) parser.parse(jsonString);
            String updateType = (String) update.get("UpdateType");

            switch (updateType) {
                case "GAME_END":
                    handleGameEnd(update);
                    break;
                case "TURN_END":
                    handleTurnEnd(update);
                    break;
                default:
                    System.out.println("Unknown UpdateType: " + updateType);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleGameEnd(JSONObject update) {
        String gameState = (String) update.get("GameState");
        if ("WIN".equals(gameState)) {
            String playerWon = (String) update.get("PlayerWon");
            playerTurnText.setText("Player " + playerWon + " won the game!");
        } else if ("TIE".equals(gameState)) {
            playerTurnText.setText("The game ended in a tie!");
        }
        disableAllButtons();
    }

    private void handleTurnEnd(JSONObject update) {
        String playerTurnUUID = (String) update.get("PlayerTurn");
        String playerTurnName = getUsernameFromUUID(playerTurnUUID);
        playerTurnText.setText("It's " + playerTurnName + "'s turn!");

        JSONArray changes = (JSONArray) update.get("Changes");
        updateBoard(changes);
    }

    private String getUsernameFromUUID(String playerUUID) {
        try {
            File file = new File("src/main/java/ca/ucalgary/seng300/p4/condensation/Networking/Database/Leaderboard/Leaderboard.json");
            JSONParser parser = new JSONParser();
            JSONArray leaderboard = (JSONArray) parser.parse(new FileReader(file));

            for (Object obj : leaderboard) {
                JSONObject player = (JSONObject) obj;
                if (playerUUID.equals(player.get("playerUUID"))) {
                    return (String) player.get("username");
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading leaderboard.json: " + e.getMessage());
        }
        return null;
    }

    private void updateBoard(JSONArray changes) {
        for (int row = 0; row < changes.size(); row++) {
            JSONArray rowArray = (JSONArray) changes.get(row);
            for (int col = 0; col < rowArray.size(); col++) {
                String state = (String) rowArray.get(col);
                updateButtonState(buttons[row][col], state);
            }
        }
    }

    private void updateButtonState(Button button, String state) {
        switch (state) {
            case "EMPTY":
                button.setStyle("-fx-background-color: white;");
                button.setDisable(false);
                break;
            case "RED":
                button.setStyle("-fx-background-color: red;");
                button.setDisable(true);
                break;
            case "BLUE":
                button.setStyle("-fx-background-color: blue;");
                button.setDisable(true);
                break;
            default:
                button.setStyle("-fx-background-color: gray;");
                button.setDisable(true);
                break;
        }
    }

    private void disableAllButtons() {
        for (Button[] row : buttons) {
            for (Button button : row) {
                button.setDisable(true);
            }
        }
    }
}
