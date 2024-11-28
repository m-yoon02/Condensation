package ca.ucalgary.seng300.p4.condensation;

import ca.ucalgary.seng300.p4.condensation.condensation.Backend.DTOs.Vector2;
import ca.ucalgary.seng300.p4.condensation.condensation.Backend.Enums.PieceColor;
import ca.ucalgary.seng300.p4.condensation.condensation.Backend.Handlers.TicTacToeHandler;
import ca.ucalgary.seng300.p4.condensation.condensation.Backend.MoveData.MoveData;
import ca.ucalgary.seng300.p4.condensation.condensation.Backend.Pieces.TicTacToePiece;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import java.util.UUID;

public class HelloController {
    @FXML
    private Label playerLabel;
    private int playerTurn = 0; // Tracks turn (0 = RED, 1 = BLUE)

    @FXML
    private Button btn00, btn01, btn02, btn10, btn11, btn12, btn20, btn21, btn22;

    private TicTacToeHandler gameHandler;
    private Button[][] buttons;
    private UUID player1ID;
    private UUID player2ID;

    @FXML
    public void initialize() {

        player1ID = UUID.randomUUID();
        player2ID = UUID.randomUUID();

        gameHandler = new TicTacToeHandler(player1ID, player2ID);

        buttons = new Button[][] {
                {btn00, btn01, btn02},
                {btn10, btn11, btn12},
                {btn20, btn21, btn22}
        };

        playerLabel.setText("Current Player: RED");
    }

    @FXML
    public void handleButtonClick(javafx.event.ActionEvent event) {
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

        if (row == -1 || col == -1) return;

        Vector2 position = new Vector2(row, col);
        MoveData moveData = new MoveData(
                playerTurn == 0 ? player1ID : player2ID,
                position
        );

       // if (gameHandler.makeMove(moveData.toJSON())) { //
            setPlayerSymbol(clickedButton, position);
            checkGameStatus(); // Check for win or tie
      //  } else {
       //     System.out.println("Invalid move at (" + row + ", " + col + ")");
      //  }
    }

    private void setPlayerSymbol(Button button, Vector2 position) {
        if (playerTurn == 0) {
            TicTacToePiece t1 = new TicTacToePiece(position,PieceColor.RED);
            gameHandler.getBoard().placePiece(t1 ,position); // Place RED piece

            button.setText("X");
            button.setTextFill(Color.RED);
            playerLabel.setText("Next Player"+ (1 - playerTurn));
            } else {
            TicTacToePiece t2 = new TicTacToePiece(position,PieceColor.BLUE);
            gameHandler.getBoard().placePiece(t2, position); // Place BLUE piece
            button.setText("O");
            button.setTextFill(Color.BLUE);
            playerLabel.setText("Next Player"+(1 - playerTurn));
        }
        button.setDisable(true);
        playerTurn = 1 - playerTurn;
    }

    private void checkGameStatus() {

        playerLabel.setText("checking");
        PieceColor color1 = gameHandler.getBoard().getAt(0, 0).getColor();

        if (gameHandler.checkForThreeInARow(PieceColor.RED)) {
            playerLabel.setText("RED Wins!");
            disableAllButtons();
        } else if (gameHandler.checkForThreeInARow(PieceColor.BLUE)) {
            playerLabel.setText("BLUE Wins!");
            disableAllButtons();
        }
//        else if (gameHandler.isBoardFull()) { // Handle ties
//            playerLabel.setText("It's a Tie!");
//            disableAllButtons();
//        } else {
//            // Update the label to reflect the next player's turn
//            playerLabel.setText(playerTurn == 0 ? "Current Player: RED" : "Current Player: BLUE");
//        }
    }

    private void disableAllButtons() { //when game ends
        for (Button[] buttonRow : buttons) {
            for (Button button : buttonRow) {
                button.setDisable(true);
            }
        }
    }

    @FXML
    public void resetGame() {
        player1ID = UUID.randomUUID();
        player2ID = UUID.randomUUID();
        gameHandler = new TicTacToeHandler(player1ID, player2ID);
        playerTurn = 0;

        for (Button[] buttonRow : buttons) {
            for (Button button : buttonRow) {
                button.setText("");
                button.setDisable(false);
                button.setTextFill(Color.BLACK);
            }
        }

        playerLabel.setText("Current Player: RED");
    }
}
