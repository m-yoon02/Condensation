package ca.ucalgary.seng300.p4.condensation.Frontend.controller;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Optional;

public class tGameController
{
    @FXML
    private Button btn00, btn01, btn02, btn10, btn11, btn12, btn20, btn21, btn22;
    @FXML
    //public int numberOfPiecesOnBoard = 0;
    Button[][] buttons = new Button[][] {
            {btn00, btn01, btn02},
            {btn10, btn11, btn12},
            {btn20, btn21, btn22}
    };

    @FXML
    public void initialize() {
    }
    @FXML
    Button forfeitBtn;

    @FXML
    public void onForfeitBtnClick() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to forfeit?");
        alert.setTitle("Forfeit");

        Optional<ButtonType> choice = alert.showAndWait();
        if (choice.isPresent() && choice.get() == ButtonType.OK) {
            onForfeitYesClick();
        } else if (choice.isPresent() && choice.get() == ButtonType.CANCEL) {
            System.out.println("No");
        }
    }
    @FXML
    public void onForfeitYesClick() {
        Stage stage = (Stage) forfeitBtn.getScene().getWindow();
        stage.close();
    }

    @FXML
    TextArea chatBox;

    @FXML
    Button sendBtn;

    @FXML
    ScrollPane chatScrollPane;

    @FXML
    VBox paneInsideScrollPane;


    String username = "Me";
    public void onSendBtnClick() {
        String messageStr = chatBox.getText();
        if (!messageStr.isEmpty()) {
            HBox messageHBox = new HBox();
            messageHBox.setStyle("    -fx-pref-width: 50;\n" +
                    "    -fx-max-width: 200;");
            messageHBox.setPadding(new Insets(5, 10, 5, 5));
            messageHBox.setMaxHeight(Integer.MAX_VALUE);
            Label senderName = new Label(username);
            senderName.setStyle("-fx-font-size: 13;\n" +
                    "    -fx-font-family: \"Oswald\", sans-serif;\n" +
                    "    -fx-text-fill: White;\n" +
                    "    -fx-font-weight: bold;");
            senderName.setPadding(new Insets(0, 0, -3, 0));
            Label message = new Label(messageStr);
            message.setStyle("-fx-font-size: 12;\n" +
                    "    -fx-font-family: \"Oswald\", sans-serif;\n" +
                    "    -fx-text-fill: White;");
            message.setWrapText(true);
            message.setLineSpacing(-5);
            VBox vBox = new VBox(senderName, message);
            vBox.setMaxHeight(Integer.MAX_VALUE);
            messageHBox.getChildren().addAll(vBox);
            paneInsideScrollPane.getChildren().add(messageHBox);
            chatBox.setText("");
            chatScrollPane.setVvalue(1.0);
        }
    }
}


//    @FXML
//    private Label playerLabel;
//    private int playerTurn = 0; // Tracks turn (0 = RED, 1 = BLUE)
//
//    @FXML
//    private Button btn00, btn01, btn02, btn10, btn11, btn12, btn20, btn21, btn22;
//
//    private TicTacToeHandler gameHandler;
//    private Button[][] buttons;
//    private UUID player1ID;
//    private UUID player2ID;
//
//    @FXML
//    public void initialize() {
//
//        player1ID = UUID.randomUUID();
//        player2ID = UUID.randomUUID();
//
//        gameHandler = new TicTacToeHandler(player1ID, player2ID);
//
//        buttons = new Button[][] {
//                {btn00, btn01, btn02},
//                {btn10, btn11, btn12},
//                {btn20, btn21, btn22}
//        };
//
//        playerLabel.setText("Current Player: RED");
//    }
//
//    Stage stage;
//
//    public HelloController(Stage stage) {
//        this.stage = stage;
//    }
//    @FXML
//    void onLeaderboardBtnClick(ActionEvent event) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("TicTacToeLeaderboard.fxml"));
//        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//        HelloController controller = new HelloController(stage);
//        fxmlLoader.setController(controller);
//        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
//        stage.setTitle("Checkers Leaderboard");
//        stage.setScene(scene);
//        stage.setMinWidth(600);
//        stage.setMinHeight(430);
//        stage.show();
//    }
//    @FXML
//    public void handleButtonClick(javafx.event.ActionEvent event) {
//        Button clickedButton = (Button) event.getSource();
//
//        int row = -1, col = -1;
//        for (int i = 0; i < 3; i++) {
//            for (int j = 0; j < 3; j++) {
//                if (buttons[i][j] == clickedButton) {
//                    row = i;
//                    col = j;
//                    break;
//                }
//            }
//            if (row != -1) break;
//        }
//
//        if (row == -1 || col == -1) return;
//
////        Vector2 position = new Vector2(row, col);
////        MoveData moveData = new MoveData(
////                playerTurn == 0 ? player1ID : player2ID,
////                position
////        );
//
//       // if (gameHandler.makeMove(moveData.toJSON())) { //
//            setPlayerSymbol(clickedButton, position);
//            checkGameStatus(); // Check for win or tie
//      //  } else {
//       //     System.out.println("Invalid move at (" + row + ", " + col + ")");
//      //  }
//    }
//
//    private void setPlayerSymbol(Button button, Vector2 position) {
//        System.out.println(position);
//        if (playerTurn == 0) {
//            TicTacToePiece t1 = new TicTacToePiece(position,PieceColor.RED);
//            gameHandler.getBoard().placePiece(t1 ,position); // Place RED piece
//            //gameHandler.makeMoveNOJSON(t1, position);
//            button.setText("X");
//            button.setTextFill(Color.RED);
//            playerLabel.setText("Next Player"+ (1 - playerTurn));
//            } else {
//            TicTacToePiece t2 = new TicTacToePiece(position,PieceColor.BLUE);
//            gameHandler.getBoard().placePiece(t2, position); // Place BLUE piece
//            //gameHandler.makeMoveNOJSON(t2, position);
//            button.setText("O");
//            button.setTextFill(Color.BLUE);
//            playerLabel.setText("Next Player"+(1 - playerTurn));
//        }
//        button.setDisable(true);
//        playerTurn = 1 - playerTurn;
//    }
//
//    private void checkGameStatus() {
//
//        System.out.println("Test");
//        //playerLabel.setText("checking");
//        //PieceColor color1 = gameHandler.getBoard().getAt(0, 0).getColor();
//        if (gameHandler.checkForThreeInARow(PieceColor.RED)) {
//            playerLabel.setText("RED Wins!");
//            disableAllButtons();
//        } else if (gameHandler.checkForThreeInARow(PieceColor.BLUE)) {
//            playerLabel.setText("BLUE Wins!");
//            disableAllButtons();
//        }
//        else if (isBoardFull()) { // Handle ties
//            playerLabel.setText("It's a Tie!");
//            disableAllButtons();
//        } else {
//            playerLabel.setText(playerTurn == 0 ? "Current Player: RED" : "Current Player: BLUE");
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
//
//    @FXML
//    public void resetGame() {
//        player1ID = UUID.randomUUID();
//        player2ID = UUID.randomUUID();
//        gameHandler = new TicTacToeHandler(player1ID, player2ID);
//        playerTurn = 0;
//
//        for (Button[] buttonRow : buttons) {
//            for (Button button : buttonRow) {
//                button.setText("");
//                button.setDisable(false);
//                button.setTextFill(Color.BLACK);
//            }
//        }
//        playerLabel.setText("Current Player: RED");
//    }
//
//    private boolean isBoardFull() {
//        for (int row = 0; row < 3; row++) {
//            for (int col = 0; col < 3; col++) {
//                if (gameHandler.getBoard().getAt(row, col) == null) {
//                    return false;
//                }
//            }
//        }
//        return true;
//    }
//}

