package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import ca.ucalgary.seng300.p4.condensation.HelloApplication;
import ca.ucalgary.seng300.p4.condensation.Networking.Services.ChatService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;


public class CheckersController {
    Stage stage;

    public CheckersController(Stage stage) {
        this.stage = stage;
    }




    // Checkers home menu
    public void onCheckersSelected(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("checkersHomeMenu.fxml"));
        CheckersController controller = new CheckersController(stage);
        fxmlLoader.setController(controller);
        Scene scene = new Scene(fxmlLoader.load(), 700, 500);
        stage.setTitle("Checkers Home Menu");
        stage.setScene(scene);
        stage.setMinWidth(700);
        stage.setMinHeight(530);
        stage.show();
    }

    @FXML
    Button homeMenuBackBtn;

    @FXML
    Button playBtn;

    @FXML
    public void onHomeMenuBackBtnClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("ProfileGame.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 700, 500);
        stage.setTitle("Select your game");
        stage.setScene(scene);
        stage.setMinWidth(700);
        stage.setMinHeight(530);
        stage.show();
    }


    // play button
    @FXML
    public void onPlayBtnClick() throws IOException {
        onCheckersStart();
    }



    @FXML
    Button leaderboardBtn;

    @FXML
    Button leaderboardBackBtn;

    // leaderboard
    @FXML
    public void onLeaderboardBtnClick(ActionEvent event) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("CheckersLeaderboard.fxml"));
//        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//        CheckersController controller = new CheckersController(stage);
//        fxmlLoader.setController(controller);
//        Scene scene = new Scene(fxmlLoader.load(), 700, 500);
//        stage.setTitle("Checkers Leaderboard");
//        stage.setScene(scene);
//        stage.setMinWidth(700);
//        stage.setMinHeight(530);
//        stage.show();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ca/ucalgary/seng300/p4/condensation/CheckersLeaderboard.fxml"));
            Stage stage = (Stage) leaderboardBtn.getScene().getWindow();
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);
            stage.setTitle("Checkers - Leaderboard");
            stage.setScene(scene);
            stage.show();
    }
    @FXML
    public void onLeaderboardBackBtnClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("CheckersHomeMenu.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        CheckersController controller = new CheckersController(stage);
        fxmlLoader.setController(controller);
        Scene scene = new Scene(fxmlLoader.load(), 700, 500);
        stage.setTitle("Checkers Home Menu");
        stage.setScene(scene);
        stage.setMinWidth(700);
        stage.setMinHeight(530);
        stage.show();
    }


    @FXML
    Button howToPlayBtn;

    @FXML
    Button howToPlayBackBtn;

    // how to play button
    @FXML
    void onHowToPlayBtnClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("checkersDescription.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        CheckersController controller = new CheckersController(stage);
        fxmlLoader.setController(controller);
        Scene scene = new Scene(fxmlLoader.load(), 700, 500);
        stage.setTitle("Checkers Description");
        stage.setScene(scene);
        stage.setMinWidth(700);
        stage.setMinHeight(530);
        stage.show();
    }
    @FXML
    public void onHowToPlayBackBtnClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("CheckersHomeMenu.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        CheckersController controller = new CheckersController(stage);
        fxmlLoader.setController(controller);
        Scene scene = new Scene(fxmlLoader.load(), 700, 500);
        stage.setTitle("Checkers Home Menu");
        stage.setScene(scene);
        stage.setMinWidth(700);
        stage.setMinHeight(530);
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
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("CheckersInstance.fxml"));
//        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        CheckersController controller = this;
        fxmlLoader.setController(controller);
        Scene scene = new Scene(fxmlLoader.load(), 700, 500);
        stage.setTitle("Checkers");
        stage.setScene(scene);
        stage.setMinWidth(700);
        stage.setMinHeight(530);
        stage.show();


        int blackCount = 0;
        int redCount = 0;
        for (int r = 0; r < BOARD_DIM; r++) {   // loop through rows
            for (int c = 0; c < BOARD_DIM; c++) {   // loop through columns
                Button square = new Button();
                square.setPrefSize(40, 40);
                square.setPadding(new Insets(1, 1, 1, 1));

                if (r % 2 == 0) {   // even rows
                    if (c % 2 == 0) {   // even columns
                        square.setStyle(black);
                    } else {    // odd columns
                        square.setStyle(white);
                    }
                } else {    // odd rows
                    if (c % 2 == 0) {   // even columns
                        square.setStyle(white);
                    } else {    // odd columns
                        square.setStyle(black);
                    }
                }

                if (r <= 2) {   // first 3 rows
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
                if (r >= 5) {   // last 3 rows
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

                chessboard[c][r] = square;  // add square to chessboard array
                chessboardGrid.add(square, c, r, 1, 1); // add square to grid pane
            }
        }
    }

    private void addPieceToSquare(Image image, Button square) {
        ImageView piece = new ImageView();
        piece.setFitWidth(36);
        piece.setPreserveRatio(true);
        piece.setImage(image);
        square.setGraphic(piece);
    }

    @FXML
    Button forfeitBtn;

    @FXML
    Button forfeitYesBtn;

    @FXML
    Button forfeitNoBtn;

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
        System.out.println("Yes");
    }



    // chat
    @FXML
    TextArea chatBox;

    @FXML
    Button sendBtn;

    @FXML
    ScrollPane chatScrollPane;

    @FXML
    VBox paneInsideScrollPane;

    String username1 = "Me";
    String username2 = "You";

    String messageHBoxStyle = "    -fx-pref-width: 50;\n" +
            "    -fx-max-width: 200;";
    String senderNameStyle = "-fx-font-size: 13;\n" +
            "    -fx-font-family: \"Oswald\", sans-serif;\n" +
            "    -fx-text-fill: White;\n" +
            "    -fx-font-weight: bold;";
    String timeSentStyle = "-fx-font-size: 8;\n" +
            "    -fx-font-family: \"Oswald\", sans-serif;\n" +
            "    -fx-text-fill: White;\n";
    String nameTimeHBoxStyle = "-fx-alignment: CENTER_LEFT";
    String messageStyle = "-fx-font-size: 12;\n" +
            "    -fx-font-family: \"Oswald\", sans-serif;\n" +
            "    -fx-text-fill: White;";

    public void onSendBtnClick() {
        String messageStr = chatBox.getText();
        if (!messageStr.isEmpty()) {
            ChatService chatService = new ChatService();
            JSONArray chatMessages = chatService.sendChatMessage(username1, messageStr, username2);

            String date1;
            String date2 = "";
            String messageStr2 = "";
            boolean responded = false;
            if (chatMessages != null) {
                LocalDateTime localDateTime1 = (LocalDateTime) ((JSONObject)chatMessages.get(0)).get("date");
                date1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(localDateTime1);

                LocalDateTime localDateTime2 = (LocalDateTime) ((JSONObject)chatMessages.get(1)).get("date");
                date2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(localDateTime2);;
                messageStr2 = ((JSONObject)chatMessages.get(1)).get("message").toString();

                responded = true;
            } else {
                LocalDateTime localDateTime1 = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
                date1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(localDateTime1);
            }

            HBox messageHBox = new HBox();
            messageHBox.setStyle(messageHBoxStyle);
            messageHBox.setPadding(new Insets(5, 10, 5, 5));
            messageHBox.setMaxHeight(Integer.MAX_VALUE);

            Label senderName = new Label(username1);
            senderName.setStyle(senderNameStyle);
            senderName.setPadding(new Insets(0, 0, -3, 0));

            Label timeSent = new Label(date1);
            timeSent.setStyle(timeSentStyle);
            timeSent.setPadding(new Insets(0, 0, 0, 3));

            HBox nameTimeHBox = new HBox(senderName, timeSent);
            nameTimeHBox.setStyle(nameTimeHBoxStyle);

            Label message = new Label(messageStr);
            message.setStyle(messageStyle);
            message.setWrapText(true);
            message.setLineSpacing(-5);

            VBox vBox = new VBox(nameTimeHBox, message);
            vBox.setMaxHeight(Integer.MAX_VALUE);
            messageHBox.getChildren().addAll(vBox);
            paneInsideScrollPane.getChildren().add(messageHBox);

            chatBox.setText("");
            chatScrollPane.setVvalue(1.0);


            // response
            if (responded) {
                HBox messageHBox2 = new HBox();
                messageHBox2.setStyle(messageHBoxStyle);
                messageHBox2.setPadding(new Insets(5, 10, 5, 5));
                messageHBox2.setMaxHeight(Integer.MAX_VALUE);

                Label senderName2 = new Label(username2);
                senderName2.setStyle(senderNameStyle);
                senderName2.setPadding(new Insets(0, 0, -3, 0));

                Label timeSent2 = new Label(date2);
                timeSent2.setStyle(timeSentStyle);
                timeSent2.setPadding(new Insets(0, 0, 0, 3));

                HBox nameTimeHBox2 = new HBox(senderName2, timeSent2);
                nameTimeHBox2.setStyle(nameTimeHBoxStyle);

                Label message2 = new Label(messageStr2);
                message2.setStyle(messageStyle);
                message2.setWrapText(true);
                message2.setLineSpacing(-5);

                VBox vBox2 = new VBox(nameTimeHBox2, message2);
                vBox2.setMaxHeight(Integer.MAX_VALUE);
                messageHBox2.getChildren().addAll(vBox2);
                paneInsideScrollPane.getChildren().add(messageHBox2);

                chatScrollPane.setVvalue(1.0);
            }

        }
    }

}