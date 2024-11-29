package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import ca.ucalgary.seng300.p4.condensation.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.UUID;


public class CheckersController {
    Stage stage;

    public CheckersController(Stage stage) {
        this.stage = stage;
    }




    // Checkers home menu
    public void onCheckersSelected(ActionEvent event, UUID playerUUID) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("checkersHomeMenu.fxml"));
        CheckersController controller = new CheckersController(stage);
        fxmlLoader.setController(controller);
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Checkers Home Menu");
        stage.setScene(scene);
        stage.setMinWidth(600);
        stage.setMinHeight(430);
        stage.show();
    }

    @FXML
    Button homeMenuBackBtn;

    @FXML
    Button playBtn;

    @FXML
    void onHomeMenuBackBtnClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("ProfileGame.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        CheckersController controller = new CheckersController(stage);
        fxmlLoader.setController(controller);
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Select your game");
        stage.setScene(scene);
        stage.setMinWidth(600);
        stage.setMinHeight(430);
        stage.show();
    }


    // play button
    @FXML
    void onPlayBtnClick() {

    }



    @FXML
    Button leaderboardBtn;

    @FXML
    Button leaderboardBackBtn;

    // leaderboard
    @FXML
    void onLeaderboardBtnClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("CheckersLeaderboard.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        CheckersController controller = new CheckersController(stage);
        fxmlLoader.setController(controller);
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Checkers Leaderboard");
        stage.setScene(scene);
        stage.setMinWidth(600);
        stage.setMinHeight(430);
        stage.show();
    }
    @FXML
    public void onLeaderboardBackBtnClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("CheckersHomeMenu.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        CheckersController controller = new CheckersController(stage);
        fxmlLoader.setController(controller);
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Checkers Home Menu");
        stage.setScene(scene);
        stage.setMinWidth(600);
        stage.setMinHeight(430);
        stage.show();
    }


    @FXML
    Button howToPlayBtn;

    @FXML
    Button howToPlayBackBtn;

    // how to play button
    @FXML
    void onHowToPlayBtnClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("description.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        CheckersController controller = new CheckersController(stage);
        fxmlLoader.setController(controller);
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Checkers Description");
        stage.setScene(scene);
        stage.setMinWidth(600);
        stage.setMinHeight(430);
        stage.show();
    }
    @FXML
    public void onHowToPlayBackBtnClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("CheckersHomeMenu.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        CheckersController controller = new CheckersController(stage);
        fxmlLoader.setController(controller);
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Checkers Home Menu");
        stage.setScene(scene);
        stage.setMinWidth(600);
        stage.setMinHeight(430);
        stage.show();
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
    public void onCheckersStart() throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("checkersInstance.fxml"));
//        fxmlLoader.setController(this);
//        Scene scene = new Scene(fxmlLoader.load(), 600, 400);


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