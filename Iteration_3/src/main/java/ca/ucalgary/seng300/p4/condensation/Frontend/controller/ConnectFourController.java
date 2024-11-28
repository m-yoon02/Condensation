package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import ca.ucalgary.seng300.p4.condensation.Frontend.game.Game;
import ca.ucalgary.seng300.p4.condensation.condensation.Backend.DTOs.Vector2;
import ca.ucalgary.seng300.p4.condensation.condensation.Backend.Enums.GameResult;
import ca.ucalgary.seng300.p4.condensation.condensation.Backend.Enums.PieceColor;
import ca.ucalgary.seng300.p4.condensation.condensation.Backend.Handlers.TicTacToeHandler;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import org.json.simple.JSONObject;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

public class ConnectFourController {

    private Button btn00, btn01, btn02, btn10, btn11, btn12, btn20, btn21, btn22;
    public int numberOfPiecesOnBoard = 0;
    Button[][] buttons = new Button[][] {
            {btn00, btn01, btn02, btn03, btn04, btn05, btn06},
            {btn10, btn11, btn12, btn13, btn14, btn15, btn16},
            {btn20, btn21, btn22, btn23, btn24, btn25, btn26},
            {btn30, btn31, btn32, btn33, btn34, btn35, btn36},
            {btn40, btn41, btn42, btn43, btn44, btn45, btn46},
            {btn50, btn51, btn52, btn53, btn54, btn55, btn56},
    };

    public static final int NUMBER_OF_ROWS = 6;
    public static final int NUMBER_OF_COLS = 7;
    public GameResult resultOfTicTacToe;
    public UUID playerOne;
    public UUID playerTwo;
    public UUID sessionID;
    public UUID newTurn;
    public void receiveConnectFour(JSONObject backendUpdate){
        String win_or_not_win = (String) backendUpdate.get("UpdateType");
        if (Objects.equals(win_or_not_win, "GAME_END")){
            resultOfTicTacToe = (GameResult) backendUpdate.get("GameState");
            if (resultOfTicTacToe == GameResult.WIN){
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
    public void returnConnectFour(javafx.event.ActionEvent event){
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

            JSONObject connectFourJSON = new JSONObject();
            connectFourJSON.put("SessionID", sessionID);
            if (newTurn == playerOne){
                connectFourJSON.put("PlayerID", playerOne);
            }
            else{
                connectFourJSON.put("Player ID", playerTwo);
            }
            connectFourJSON.put("New Position", new Vector2(row, col));
            numberOfPiecesOnBoard += 1;
            //results = ticTacToeHandler.makeMove(String(ticTacToeJSON));
            // returnToBackendConnectFour(results);
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
