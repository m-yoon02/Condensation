package ca.ucalgary.seng300.p4.condensation.Frontend.controller;


import ca.ucalgary.seng300.p4.condensation.Backend.DTOs.Vector2;
import ca.ucalgary.seng300.p4.condensation.Backend.Enums.GameResult;
import ca.ucalgary.seng300.p4.condensation.Networking.Managers.NetworkManager;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

public class CheckersController {

    private Button btn00, btn01, btn02, btn10, btn11, btn12, btn20, btn21, btn22;
    public int numberOfPiecesOnBoard = 0;
    Button[][] buttons = new Button[][] {};

    public static final int NUMBER_OF_ROWS = 8;
    public static final int NUMBER_OF_COLS = 8;
    public GameResult resultOfCheckers;
    public UUID playerOne;
    public UUID playerTwo;
    public UUID sessionID;
    public UUID newTurn;

    public void receiveCheckers(JSONObject backendUpdate){
        String win_or_not_win = (String) backendUpdate.get("UpdateType");
        if (Objects.equals(win_or_not_win, "GAME_END")){
            resultOfCheckers = (GameResult) backendUpdate.get("GameState");
            if (resultOfCheckers == GameResult.WIN){
                disableAllButtons();
                // Switch to aftermath, send data now????
                // Notification
            }
            else{
                //Tie -> send data to Aftermath
            }
        }
        else{
            sessionID = (UUID) backendUpdate.get("SessionID");
            newTurn = (UUID) backendUpdate.get("PlayerTurn");
            String[][] connectFourBackendBoard = (String[][]) backendUpdate.get("Changes");
            int rowIndex = 0;
            int colIndex;
            for (String[] strings : connectFourBackendBoard) {
                colIndex = 0;
                for (String piece : strings) {
                    Button buttonForBoard = buttons[rowIndex][colIndex];
                    if (Objects.equals(piece, "RED")){
                        buttonForBoard.setText("O");
                        buttonForBoard.setTextFill(Color.RED);
                    }
                    else if (Objects.equals(piece, "BLUE")){
                        buttonForBoard.setText("O");
                        buttonForBoard.setTextFill(Color.BLUE);
                    }
                    else{
                        buttonForBoard.setText("");
                    }
                }
                rowIndex += 1;
            }
        }
    }

    // Activates when a tile is clicked.
    public void returnCheckers(javafx.event.ActionEvent event) throws IOException, ParseException {
        Button clickedButton = (Button) event.getSource();

        int row = -1;
        int col = -1;

        boolean buttonFound = false;
        for (int i = 0; i < NUMBER_OF_ROWS; i++) {
            if (buttonFound){
                break;
            }
            for (int j = 0; j < NUMBER_OF_COLS; j++) {
                if (buttons[i][j] == clickedButton) {
                    row = i;
                    col = j;
                    buttonFound = true;
                    break;
                }
            }
        }

        if (row > 0 && col > 0){

            JSONObject checkersJSON = new JSONObject();
            checkersJSON.put("SessionID", sessionID);
            if (newTurn == playerOne){
                checkersJSON.put("PlayerID", playerOne);
            }
            else{
                checkersJSON.put("Player ID", playerTwo);
            }
            checkersJSON.put("New Position", new Vector2(row, col));
            numberOfPiecesOnBoard += 1;
            JSONObject results = NetworkManager.getNetworkInstance().handleQueuePlayer(checkersJSON);
            receiveCheckers(results);
        }
    }

    private void disableAllButtons() { //when game ends
        for (Button[] buttonRow : buttons) {
            for (Button button : buttonRow) {
                button.setDisable(true);
            }
        }
    }
}
