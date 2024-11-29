package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import javafx.fxml.FXML;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;


import java.io.IOException;

public class CheckersController {
    // Checkers home menu
    @FXML
    Button backBtn;

    @FXML
    Button playBtn;

    @FXML
    Button leaderboardBtn;

    @FXML
    void onBackBtnClick() {

    }


    @FXML
    void onPlayBtnClick() {

    }


    @FXML
    void onLeaderboardBtnClick() throws IOException {

    }


    // Checkers instance
    @FXML
    GridPane chessboardGrid;

    final int BOARD_DIM = 8;
    String white = "-fx-background-color: #ffffff;" +
            "-fx-alignment: center;" +
            "-fx-background-radius: 0;";
    String black = "-fx-background-color: #000000;" +
            "-fx-alignment: center;" +
            "-fx-background-radius: 0;";

    Button chessboard[][] = new Button[8][8];

    Button blackPieces[] = new Button[12];
    Button redPieces[] = new Button[12];

    @FXML
    public void onCheckersStart() {
        int blackCount = 0;
        int redCount = 0;
        for (int r = 0; r < BOARD_DIM; r++) {
            for (int c = 0; c < BOARD_DIM; c++) {
                Button square = new Button();
                square.setPrefSize(34, 34);
                square.setPadding(new Insets(1, 1, 1, 1));

                if (r % 2 == 0) {
                    if (c % 2 == 0) {
                        square.setStyle(black);
                    } else {
                        square.setStyle(white);
                    }
                } else {
                    if (c % 2 == 0) {
                        square.setStyle(white);
                    } else {
                        square.setStyle(black);
                    }
                }

//                /*
                if (r <= 2) {
                    Image image = new Image((this.getClass().getResource("/ca/ucalgary/seng300/p4/condensation/black2.png")).toExternalForm());
                    if (r % 2 == 0) { // 0 and 2
                        if (c % 2 == 0) {
                            addPieceToSquare(image, square);
                            blackPieces[blackCount] = square;
                            blackCount++;
                        }
                    } else { // 1
                        if (c % 2 != 0) {
                            addPieceToSquare(image, square);
                            blackPieces[blackCount] = square;
                            blackCount++;
                        }
                    }
                }
                if (r >= 5) {
                    Image image = new Image((this.getClass().getResource("/ca/ucalgary/seng300/p4/condensation/red.png")).toExternalForm());                    if (r % 2 == 0) { // 6
                        if (c % 2 == 0) {
                            addPieceToSquare(image, square);
                            redPieces[redCount] = square;
                            redCount++;
                        }
                    } else { // 5 and 7
                        if (c % 2 != 0) {
                            addPieceToSquare(image, square);
                            redPieces[redCount] = square;
                            redCount++;
                        }
                    }
                }
//                 */

                chessboard[c][r] = square;
                chessboardGrid.add(square, c, r, 1, 1);
            }
        }
    }

    private void addPieceToSquare(Image image, Button square) {
        ImageView piece = new ImageView();
        piece.setFitWidth(32);
        piece.setPreserveRatio(true);
        piece.setImage(image);
//      square.getChildren().add(piece);
        square.setGraphic(piece);
    }

    @FXML
    Button forfeitBtn;


    @FXML
    public void onForfeitBtnClick() {

    }


    @FXML
    public void onPieceClicked() {

    }
}