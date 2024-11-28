package ca.ucalgary.seng300.p4.condensation;

import ca.ucalgary.seng300.p4.condensation.condensation.Backend.DTOs.Vector2;
import ca.ucalgary.seng300.p4.condensation.condensation.Backend.Enums.GameResult;
import ca.ucalgary.seng300.p4.condensation.condensation.Backend.Enums.PieceColor;
import ca.ucalgary.seng300.p4.condensation.condensation.Backend.Handlers.TicTacToeHandler;
import ca.ucalgary.seng300.p4.condensation.condensation.Backend.MoveData.MoveData;
import ca.ucalgary.seng300.p4.condensation.condensation.Backend.Pieces.TicTacToePiece;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import org.json.simple.JSONObject;

import java.util.Objects;
import java.util.UUID;

public class HelloController {

    @FXML
    private Button btn00, btn01, btn02, btn10, btn11, btn12, btn20, btn21, btn22;
    public int numberOfPiecesOnBoard = 0;

    public static final int NUMBER_OF_ROWS = 3;
    public static final int NUMBER_OF_COLS = 3;

    public static final int MAX_PIECES = 9;
    Button[][] buttons = new Button[][]{
            {btn00, btn01, btn02},
            {btn10, btn11, btn12},
            {btn20, btn21, btn22}
    };
    public GameResult resultOfTicTacToe;
    public UUID playerOne;
    public UUID playerTwo;
    public UUID sessionID;
    public UUID newTurn;
    public boolean playerOneMoving = true;
    public boolean playerTwoMoving = false;
    public PieceColor playerOnePiece = PieceColor.RED;
    public PieceColor playerTwoPiece = PieceColor.BLUE;

    public void receiveFromBackendTicTacToe(JSONObject backendUpdate) {
        String win_or_not_win = (String) backendUpdate.get("UpdateType");
        if (Objects.equals(win_or_not_win, "GAME_END")) {
            resultOfTicTacToe = (GameResult) backendUpdate.get("GameState");
            if (resultOfTicTacToe == GameResult.WIN) {
                disableAllButtons();
                // Switch to aftermath, send data now????
                // Notification
            } else {
                //Tie -> send data to Aftermath
            }
        } else {
            sessionID = (UUID) backendUpdate.get("SessionID");
            newTurn = (UUID) backendUpdate.get("PlayerTurn");
            String[][] connectFourBackendBoard = (String[][]) backendUpdate.get("Changes");
            int rowIndex = 0;
            int colIndex;
            for (String[] strings : connectFourBackendBoard) {
                colIndex = 0;
                for (String piece : strings) {
                    Button buttonForBoard = buttons[rowIndex][colIndex];
                    if (Objects.equals(piece, "RED")) {
                        buttonForBoard.setText("X");
                        buttonForBoard.setTextFill(Color.RED);
                    } else if (Objects.equals(piece, "BLUE")) {
                        buttonForBoard.setText("O");
                        buttonForBoard.setTextFill(Color.BLUE);
                    } else {
                        buttonForBoard.setText("");
                    }
                }
                rowIndex += 1;
            }
        }
    }

    // Activates when a tile is clicked.
    public JSONObject returnToBackendConnectFour(javafx.event.ActionEvent event) {
        Button clickedButton = (Button) event.getSource();

        int row = -1, col = -1;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j] == clickedButton) {
                    row = i;
                    col = j;
                    break;
                }
            }
            if (row != -1) break;
        }
        Vector2 newPositionOfPlacedPiece = new Vector2(row, col);

        JSONObject ticTacToeJSON = new JSONObject();
        ticTacToeJSON.put("SessionID", sessionID);
        if (playerOneMoving) {
            ticTacToeJSON.put("PlayerID", playerOne);
        } else {
            ticTacToeJSON.put("Player ID", playerTwo);
        }
        ticTacToeJSON.put("New Position", newPositionOfPlacedPiece);
        numberOfPiecesOnBoard += 1;
        return ticTacToeJSON;
    }

    private void disableAllButtons() { //when game ends
        for (Button[] buttonRow : buttons) {
            for (Button button : buttonRow) {
                button.setDisable(true);
            }
        }
    }
}