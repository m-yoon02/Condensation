package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import ca.ucalgary.seng300.p4.condensation.Backend.DTOs.Vector2;
import ca.ucalgary.seng300.p4.condensation.Backend.Enums.GameResult;
import ca.ucalgary.seng300.p4.condensation.Networking.Managers.NetworkManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.input.MouseEvent;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ConnectFourController {

    @FXML
    private GridPane connectFourGrid;

    @FXML
    private Text forfeitButton;

    @FXML
    private Text playerTurnText;

    private Button[][] buttons = new Button[6][7];

    public static final int NUMBER_OF_ROWS = 6;
    public static final int NUMBER_OF_COLS = 7;

    private String sessionID;
    private String playerID;

    @FXML
    public void initialize() {
        for (int row = 0; row < NUMBER_OF_ROWS; row++) {
            for (int col = 0; col < NUMBER_OF_COLS; col++) {
                String buttonId = "btn" + row + col;
                Button button = (Button) connectFourGrid.lookup("#" + buttonId);
                buttons[row][col] = button;

                if (button != null) {
                    int finalRow = row;
                    int finalCol = col;
                    button.setOnMouseClicked(event -> {
                        try {
                            returnConnectFour(event, finalRow, finalCol);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        }

//        // Simulated backend update JSON
//        String jsonUpdate = "{ " +
//                "\"UpdateType\": \"TURN_END\", " +
//                "\"SessionID\": \"d290f1ee-6c54-4b01-90e6-d701748f0851\", " +
//                "\"PlayerTurn\": \"345e6789-e89d-45d5-a678-627836285111\", " +
//                "\"Changes\": [ " +
//                "[\"EMPTY\", \"EMPTY\", \"EMPTY\", \"EMPTY\", \"EMPTY\", \"EMPTY\", \"EMPTY\"], " +
//                "[\"EMPTY\", \"EMPTY\", \"EMPTY\", \"EMPTY\", \"EMPTY\", \"EMPTY\", \"EMPTY\"], " +
//                "[\"EMPTY\", \"EMPTY\", \"EMPTY\", \"RED\", \"EMPTY\", \"EMPTY\", \"EMPTY\"], " +
//                "[\"EMPTY\", \"EMPTY\", \"BLUE\", \"RED\", \"EMPTY\", \"EMPTY\", \"EMPTY\"], " +
//                "[\"EMPTY\", \"RED\", \"BLUE\", \"BLUE\", \"EMPTY\", \"EMPTY\", \"EMPTY\"], " +
//                "[\"RED\", \"BLUE\", \"RED\", \"RED\", \"BLUE\", \"EMPTY\", \"EMPTY\"] " +
//                "]" +
//                "}";
//
//        receiveConnectFour(jsonUpdate);
    }

    public void receiveConnectFour(JSONObject update) {
        try {
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
            String playerTurnName = getUsernameFromUUID(playerWon);
            playerTurnText.setText(playerTurnName + " won the game!");
        } else if ("TIE".equals(gameState)) {
            playerTurnText.setText("The game ended in a tie!");
        }
        disableAllButtons();
    }

    private void handleTurnEnd(JSONObject update) {
        sessionID = (String) update.get("SessionID");
        playerID = (String) update.get("PlayerTurn");
        String playerTurnName = getUsernameFromUUID(playerID);
        playerTurnText.setText(playerTurnName + "'s turn!");

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

    public void returnConnectFour(MouseEvent event, int row, int col) throws IOException, ParseException {
        if (sessionID == null || playerID == null) {
            System.err.println("Session ID or Player ID is missing!");
            return;
        }

        // Create JSON for the move
        JSONObject move = new JSONObject();
        move.put("SessionID", sessionID);
        move.put("PlayerID", playerID);

        // Add the new position as Vector2
        Vector2 newPosition = new Vector2(row, col);
        move.put("NewPosition", newPosition);

        System.out.println("Sending move to backend: " + move.toJSONString());

        // Send the move to the backend
        JSONObject backendResponse = NetworkManager.getNetworkInstance().handleQueuePlayer(move);

        // Process the backend response
        receiveConnectFour(backendResponse);
    }

    private void disableAllButtons() {
        for (Button[] row : buttons) {
            for (Button button : row) {
                button.setDisable(true);
            }
        }
    }
}
