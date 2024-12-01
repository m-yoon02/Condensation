package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import ca.ucalgary.seng300.p4.condensation.Backend.DTOs.Vector2;
import ca.ucalgary.seng300.p4.condensation.Backend.Enums.GameResult;
import ca.ucalgary.seng300.p4.condensation.Networking.DTO.GameSession;
import ca.ucalgary.seng300.p4.condensation.Networking.DTO.QueuedPlayerMemo;
import ca.ucalgary.seng300.p4.condensation.Networking.Enums.GameTypes;
import ca.ucalgary.seng300.p4.condensation.Networking.Managers.NetworkManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import javafx.scene.control.Label;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

public class TicTacToeController {
    private static Button btn00;
    private static Button btn01;
    private static Button btn02;
    private static Button btn10;
    private static Button btn11;
    private static Button btn12;
    private static Button btn20;
    private static Button btn21;
    private static Button btn22;
    public int numberOfPiecesOnBoard = 0;
    static Button[][] buttons = new Button[][] {
        {btn00, btn01, btn02},
        {btn10, btn11, btn12},
        {btn20, btn21, btn22}
    };

    public static final int NUMBER_OF_ROWS = 3;
    public static final int NUMBER_OF_COLS = 3;
    public static GameResult resultOfTicTacToe;
    public UUID playerOne;
    public static UUID playerTwo;
    public static UUID sessionID;
    public static UUID newTurn;

    @FXML
    public static Label playerStatus;

    @FXML
    public void initialize() throws IOException, ParseException {

        buttons = new Button[][] {
                {btn00, btn01, btn02},
                {btn10, btn11, btn12},
                {btn20, btn21, btn22}
        };

        playerOne = UUID.fromString("234e5678-e89c-34d4-a567-526725175000");
        playerTwo = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        newTurn = playerOne;

        QueuedPlayerMemo queuePlayerOneMemo = new QueuedPlayerMemo(playerOne, GameTypes.TICTACTOE, 1662);
        QueuedPlayerMemo queuedPlayerTwoMemo = new QueuedPlayerMemo(playerTwo,GameTypes.TICTACTOE, 1999);
        GameSession newGameSession = new GameSession(queuePlayerOneMemo, queuedPlayerTwoMemo);
        sessionID = newGameSession.getSessionId();

        playerStatus.setText("RED TURN - YOUR TURN");
        playerStatus.setTextFill(Color.RED);
    }

    public static void receiveFromBackendTicTacToe(JSONObject backendUpdate){
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
                        buttonForBoard.setText("X");
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
            if (newTurn.equals(playerTwo)){
                playerStatus.setText("BLUE TURN - OPPONENT'S TURN");
                playerStatus.setTextFill(Color.BLUE);
            }
            else{
                playerStatus.setText("RED TURN - YOUR TURN");
                playerStatus.setTextFill(Color.RED);
            }
        }
    }

    private static void disableAllButtons() { //when game ends
        for (Button[] buttonRow : buttons) {
            for (Button button : buttonRow) {
                button.setDisable(true);
            }
        }
    }

    @FXML
    public void returnToBackendTicTacToe(javafx.event.ActionEvent event) throws IOException, ParseException {
        Button clickedButton = (Button) event.getSource();

        int row = -1;
        int col = -1;

        boolean buttonFound = false;
        for (int i = 0; i < NUMBER_OF_ROWS; i++) {
            if (buttonFound){
                break;
            }
            for (int j = 0; j < NUMBER_OF_COLS; j++) {
                System.out.println(buttons[i][j]);
                if (buttons[i][j].equals(clickedButton)) {
                    row = i;
                    col = j;
                    buttonFound = true;
                    break;
                }
            }
        }

        if (row >= 0 && col >= 0){

            JSONObject ticTacToeJSON = new JSONObject();
            ticTacToeJSON.put("SessionID", sessionID);
            if (newTurn == playerOne){
                ticTacToeJSON.put("playerID", playerOne.toString());
            }
            else{
                ticTacToeJSON.put("playerID", playerTwo.toString());
            }
            ticTacToeJSON.put("New Position", new Vector2(row, col));
            ticTacToeJSON.put("gameType", GameTypes.TICTACTOE);
            numberOfPiecesOnBoard += 1;
            System.out.println(ticTacToeJSON);
            JSONObject results = NetworkManager.getNetworkInstance().handleQueuePlayer(ticTacToeJSON);
            receiveFromBackendTicTacToe(results);
        }
    }
}


