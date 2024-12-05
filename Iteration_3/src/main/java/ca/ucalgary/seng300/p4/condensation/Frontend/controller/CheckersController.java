package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import ca.ucalgary.seng300.p4.condensation.Backend.Enums.PieceColor;
import ca.ucalgary.seng300.p4.condensation.Frontend.GameSessionClient.GameSessionRecord;
import ca.ucalgary.seng300.p4.condensation.Frontend.Receiver.ClientReceiver;
import ca.ucalgary.seng300.p4.condensation.Frontend.Receiver.RecieverListener;
import ca.ucalgary.seng300.p4.condensation.HelloApplication;
import ca.ucalgary.seng300.p4.condensation.Networking.Enums.GameTypes;
import ca.ucalgary.seng300.p4.condensation.Networking.Managers.NetworkManager;
import ca.ucalgary.seng300.p4.condensation.Networking.Services.ChatService;
import static ca.ucalgary.seng300.p4.condensation.Frontend.GameSessionClient.GameSessionRecord.updateBoardRepresentationFromJSON;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;


public class CheckersController {
    private static final String[] COLORS = {PieceColor.RED.toString(), PieceColor.BLACK.toString()};

    // Checkers instance
    @FXML
    private GridPane chessboardGrid;

    private Stage stage;

    private final int BOARD_DIM = 8;
    private final String white = "-fx-background-color: #ffffff;" +
            "-fx-alignment: center;" +
            "-fx-background-radius: 0;";
    private final String black = "-fx-background-color: #000000;" +
            "-fx-alignment: center;" +
            "-fx-background-radius: 0;";

    private final Button[][] chessboard = new Button[BOARD_DIM][BOARD_DIM];

    private UUID playerOne;
    private UUID playerTwo;
    private UUID sessionID;
    private UUID newTurn;

    private final String[][] boardRepresentation = new String[BOARD_DIM][BOARD_DIM];
    private int[] lastClickedPosition = null;

    private static final Image BLACK_IMAGE =  new Image((CheckersController.class.getResource("/ca/ucalgary/seng300/p4/condensation/black2.png")).toExternalForm());
    private static final Image RED_IMAGE = new Image((CheckersController.class.getResource("/ca/ucalgary/seng300/p4/condensation/red.png")).toExternalForm());

    @FXML
    private Label turnLabel;

    private class CheckersRecieverListener extends RecieverListener {

        public CheckersRecieverListener() {
            super(GameTypes.CHECKERS);
        }

        @Override
        public void onReceive(JSONObject payload) throws IOException {
            receiveCheckers(payload);
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() throws IOException, ParseException {

        GameSessionRecord gameSessionRecord = GameSessionRecord.getGameSessionRecord();

        playerOne = gameSessionRecord.getPlayer1ID();
        playerTwo = gameSessionRecord.getPlayer2ID();
        sessionID = gameSessionRecord.getSessionID();
        newTurn = gameSessionRecord.getCurrentPlayerID();

        for (int r = 0; r < BOARD_DIM; r++) {   // loop through rows
            for (int c = 0; c < BOARD_DIM; c++) {   // loop through columns
                Button square = new Button();
                int finalR = r;
                int finalC = c;
                square.setPrefSize(40, 40);
                square.setPadding(new Insets(1, 1, 1, 1));
                if ((r % 2 == 0 && c % 2 == 0) || (r % 2 != 0 && c % 2 != 0)){
                    square.setStyle(black);
                } else {
                    square.setStyle(white);
                }
                square.setOnMouseClicked(event -> {
                    try {
                        onCheckersButtonClick(finalR, finalC);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                chessboard[r][c] = square;  // add square to chessboard array
                chessboardGrid.add(square, c, r, 1, 1); // add square to grid pane
            }
        }
        initializeBoardRepresentation();
        updateUIToMatchBoardRepresentation();
        ClientReceiver.addClientReceiver(new CheckersRecieverListener());
    }

    public void receiveCheckers(JSONObject payload) throws IOException {
        String gameResult = (String) payload.get("UpdateType");

        if (gameResult.equals("TURN_END")){
            handleTurnEnd(payload);
        } else{
            handleGameEnd(payload);
        }
    }

    private void handleTurnEnd(JSONObject payload) {
        sessionID = (UUID) payload.get("SessionID");
        newTurn = (UUID) payload.get("PlayerTurn");
        // Here we will update the board representation from payload
        updateBoardRepresentationFromJSON(boardRepresentation, (JSONArray) payload.get("Changes"));
        updateUIToMatchBoardRepresentation();
    }

    private void handleGameEnd(JSONObject payload) throws IOException {
        String gameState = (String) payload.get("GameState");
        if ("WIN".equals(gameState)) {
            String playerWon = (String) payload.get("PlayerWon");
            turnLabel.setText(String.format("Player %s won the game!", playerWon));
            if (UUID.fromString(playerWon).equals(playerOne)){
                turnLabel.setTextFill(Color.GREEN);
            }
            else{
                turnLabel.setTextFill(Color.RED);
            }
        } else if ("TIE".equals(gameState)) {
            turnLabel.setText("The game ended in a tie!");
        }
        disableAllButtons();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Aftermath.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Condensation Application - Aftermath");
        stage.setScene(scene);
        stage.show();
    }

    private static void addPieceToSquare(Image image, Button square) {
        ImageView piece = new ImageView();
        piece.setFitWidth(36);
        piece.setPreserveRatio(true);
        piece.setImage(image);
        square.setGraphic(piece);
    }

    @FXML
    private Button forfeitBtn;

    @FXML
    private Button forfeitYesBtn;

    @FXML
    private Button forfeitNoBtn;

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
    private TextArea chatBox;

    @FXML
    private Button sendBtn;

    @FXML
    private ScrollPane chatScrollPane;

    @FXML
    private VBox paneInsideScrollPane;

    private String messageHBoxStyle = "    -fx-pref-width: 50;\n" +
            "    -fx-max-width: 200;";
    private String senderNameStyle = "-fx-font-size: 13;\n" +
            "    -fx-font-family: \"Oswald\", sans-serif;\n" +
            "    -fx-text-fill: White;\n" +
            "    -fx-font-weight: bold;";
    private String timeSentStyle = "-fx-font-size: 8;\n" +
            "    -fx-font-family: \"Oswald\", sans-serif;\n" +
            "    -fx-text-fill: White;\n";
    private String nameTimeHBoxStyle = "-fx-alignment: CENTER_LEFT";
    private String messageStyle = "-fx-font-size: 12;\n" +
            "    -fx-font-family: \"Oswald\", sans-serif;\n" +
            "    -fx-text-fill: White;";

    public void onSendBtnClick() {
        String username1 = getUsernameFromUUID(String.valueOf(playerOne));
        String username2 = getUsernameFromUUID(String.valueOf(playerTwo));
        String messageStr = chatBox.getText();
        if (!messageStr.isEmpty()) {
            ChatService chatService = new ChatService();
            JSONArray chatMessages = chatService.sendChatMessage(username1, messageStr, username2);

            String date1;
            String date2 = "";
            String messageStr2 = "";
            boolean responded = false;
            if (chatMessages != null) {
                LocalDateTime localDateTime1 = (LocalDateTime) ((JSONObject) chatMessages.get(0)).get("date");
                date1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(localDateTime1);

                LocalDateTime localDateTime2 = (LocalDateTime) ((JSONObject) chatMessages.get(1)).get("date");
                date2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(localDateTime2);
                ;
                messageStr2 = ((JSONObject) chatMessages.get(1)).get("message").toString();

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

    private String getUsernameFromUUID(String playerUUID) {
        try {
            File file = new File(NetworkManager.LEADERBOARD_FILE);
            JSONParser parser = new JSONParser();
            JSONArray leaderboard = (JSONArray) parser.parse(new FileReader(file));

            for (Object obj : leaderboard) {
                JSONObject player = (JSONObject) obj;
                if (playerUUID.equals(player.get("playerUUID"))) {
                    return (String) player.get("username");
                }
            }
        } catch (Exception e) {
            System.err.println(String.format("Error reading leaderboard.json: %s", e.getMessage()));
        }
        return null;
    }

    private void disableAllButtons() { //when game ends
        for (Button[] buttonRow : chessboard) {
            for (Button button : buttonRow) {
                button.setDisable(true);
            }
        }
    }

    private void initializeBoardRepresentation() {
        for (String[] strings : boardRepresentation)
            Arrays.fill(strings, "EMPTY");
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < BOARD_DIM; j++){
                if ((i % 2 == 0 && j % 2 == 0) || (i % 2 != 0 && j % 2 != 0)) {
                    boardRepresentation[i][j] = "BLACK";
                }
            }
        }
        for (int i = BOARD_DIM - 3; i < BOARD_DIM; i++){
            for (int j = 0; j < BOARD_DIM; j++) {
                if ((i % 2 == 0 && j % 2 == 0) || (i % 2 != 0 && j % 2 != 0)) {
                    boardRepresentation[i][j] = "RED";
                }
            }
        }
    }

    private void updateUIToMatchBoardRepresentation() {
        for (int r = 0; r < BOARD_DIM; r++) {   // loop through rows
            for (int c = 0; c < BOARD_DIM; c++) {   // loop through columns
                Button square = chessboard[r][c];
                square.setGraphic(null);
                if (boardRepresentation[r][c].equals("RED")) {
                    addPieceToSquare(RED_IMAGE, square);
                } else if (boardRepresentation[r][c].equals("BLACK")){
                    addPieceToSquare(BLACK_IMAGE, square);
                }
            }
        }
    }

    private void onCheckersButtonClick(int i, int j) throws IOException, ParseException {
        if (lastClickedPosition == null){
            int checkIndex = newTurn.equals(playerOne) ? 0 : 1;
            if (boardRepresentation[i][j].equals(COLORS[checkIndex])){
                lastClickedPosition = new int[]{j, i};
            }
        } else {
            NetworkManager.getNetworkInstance().receiveGameDataFromClient(GameSessionRecord.parseMoveData(
                    sessionID,
                    newTurn,
                    new int[]{j, i},
                    lastClickedPosition
            ));
            lastClickedPosition = null;
        }
    }
}