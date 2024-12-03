package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import ca.ucalgary.seng300.p4.condensation.Backend.DTOs.Vector2;
import ca.ucalgary.seng300.p4.condensation.HelloApplication;
import ca.ucalgary.seng300.p4.condensation.Networking.Enums.GameTypes;
import ca.ucalgary.seng300.p4.condensation.Networking.Managers.NetworkManager;
import ca.ucalgary.seng300.p4.condensation.Networking.Services.ChatService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
    static Stage stage;

    // Checkers instance
    @FXML
    static GridPane chessboardGrid;

    static final int BOARD_DIM = 8;
    static String white = "-fx-background-color: #ffffff;" +
            "-fx-alignment: center;" +
            "-fx-background-radius: 0;";
    static String black = "-fx-background-color: #000000;" +
            "-fx-alignment: center;" +
            "-fx-background-radius: 0;";

    static Button[][] chessboard = new Button[8][8];

    static Button[] blackPieces = new Button[12];
    static Button[] redPieces = new Button[12];

    public static UUID playerOne;
    public static UUID playerTwo;
    public static UUID sessionID;
    public static UUID newTurn;

    public List<List<Integer>> coordinatesOfTwoClicks = new ArrayList<>();

    public static List<List<Integer>> locationOfBlackPieces = new ArrayList<>();

    public static List<List<Integer>> locationOfRedPieces = new ArrayList<>();

    static Image BLACK_IMAGE =  new Image((CheckersController.class.getResource("/ca/ucalgary/seng300/p4/condensation/black2.png")).toExternalForm());
    static Image RED_IMAGE = new Image((CheckersController.class.getResource("/ca/ucalgary/seng300/p4/condensation/red.png")).toExternalForm());

    @FXML
    public static Label turnLabel;

    @FXML
    public void initialize() throws IOException, ParseException {

        Object newPlayerJSONFileObject = new JSONParser().parse(new FileReader("newplayer.json"));
        JSONObject newPlayerJSONFile = (JSONObject) newPlayerJSONFileObject;

        playerOne = (UUID) newPlayerJSONFile.get("Player1ID");
        playerTwo = (UUID) newPlayerJSONFile.get("player2ID");
        newTurn =  (UUID) newPlayerJSONFile.get("currentPlayerTurn");
        sessionID = (UUID) newPlayerJSONFile.get("sessionID");

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
                    if (r % 2 == 0) { // 0 and 2
                        if (c % 2 == 0) {
                            addPieceToSquare(BLACK_IMAGE, square);
                            blackPieces[blackCount] = square;
                            blackCount++;
                        }
                    } else { // 1
                        if (c % 2 != 0) {
                            addPieceToSquare(BLACK_IMAGE, square);
                            blackPieces[blackCount] = square;
                            blackCount++;
                        }
                    }
                    List<Integer> coordinateForBlacks = new ArrayList<>();
                    coordinateForBlacks.add(r);
                    coordinateForBlacks.add(c);
                    locationOfBlackPieces.add(coordinateForBlacks);
                }
                if (r >= 5) {   // last 3 rows
                    if (r % 2 == 0) { // 6
                        if (c % 2 == 0) {
                            addPieceToSquare(RED_IMAGE, square);
                            redPieces[redCount] = square;
                            redCount++;
                        }
                    } else { // 5 and 7
                        if (c % 2 != 0) {
                            addPieceToSquare(RED_IMAGE, square);
                            redPieces[redCount] = square;
                            redCount++;
                        }
                    }
                    List<Integer> coordinateForReds = new ArrayList<>();
                    coordinateForReds.add(r);
                    coordinateForReds.add(c);
                    locationOfRedPieces.add(coordinateForReds);
                }

                final int finalRow = r;
                final int finalCol = c;
                square.setOnMouseClicked(event -> {
                    try {
                        List<Integer> coordinate = new ArrayList<>();
                        coordinate.add(finalRow);
                        coordinate.add(finalCol);
                        coordinatesOfTwoClicks.add(coordinate);
                        returnToBackendCheckers(event);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                chessboard[c][r] = square;  // add square to chessboard array
                chessboardGrid.add(square, c, r, 1, 1); // add square to grid pane
            }
        }
    }

    public static void receiveCheckers(JSONObject payload) throws IOException {
        String gameResult = (String) payload.get("UPDATE_TYPE");

        if (Objects.equals(gameResult, "TURN_END")){
            handleTurnEnd(payload);
        }
        else{
            handleGameEnd(payload);
        }
    }

    private static void handleTurnEnd(JSONObject payload){
        sessionID = (UUID) payload.get("SessionID");
        newTurn = (UUID) payload.get("PlayerTurn");
        updateBoardAndPieces((JSONArray) payload.get("Changes"));
    }

    private static void updateBoardAndPieces(JSONArray allUpdatedPieces){
        locationOfBlackPieces = new ArrayList<>();
        locationOfRedPieces = new ArrayList<>();
        chessboardGrid.getChildren().clear();
        // Removes all the buttons in the grid pane for now
        // https://stackoverflow.com/questions/11147788/clean-gridpane-in-javafx-and-maintain-the-grid-line
        for (int row = 0; row < allUpdatedPieces.size(); row++) {
            JSONArray rowArray = (JSONArray) allUpdatedPieces.get(row);
            for (int col = 0; col < rowArray.size(); col++) {
                String state = (String) rowArray.get(col);
                Button newButton = chessboard[col][row];
                if (row % 2 == 0) {   // even rows
                    if (col % 2 == 0) {   // even columns
                        newButton.setStyle(black);
                    } else {    // odd columns
                        newButton.setStyle(white);
                    }
                } else {    // odd rows
                    if (col % 2 == 0) {   // even columns
                        newButton.setStyle(white);
                    } else {    // odd columns
                        newButton.setStyle(black);
                    }
                }
                if(state.equals("RED")){
                    addPieceToSquare(RED_IMAGE, newButton);
                    List<Integer> newRedCoordinates = new ArrayList<>();
                    newRedCoordinates.add(row);
                    newRedCoordinates.add(col);
                    locationOfRedPieces.add(newRedCoordinates);
                }
                else if (state.equals("BLACK")){
                    addPieceToSquare(BLACK_IMAGE, newButton);
                    List<Integer> newBlackCoordinates = new ArrayList<>();
                    newBlackCoordinates.add(row);
                    newBlackCoordinates.add(col);
                    locationOfBlackPieces.add(newBlackCoordinates);
                }
                chessboard[col][row] = newButton;
                chessboardGrid.add(newButton, col, row, 1, 1);
            }
        }
    }

    private static void handleGameEnd(JSONObject payload) throws IOException {
        String gameState = (String) payload.get("GameState");
        if ("WIN".equals(gameState)) {
            String playerWon = (String) payload.get("PlayerWon");
            turnLabel.setText(String.format("Player %s won the game!", playerWon));
            if (playerWon.equals(playerOne)){
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

    @FXML
    public void returnToBackendCheckers(MouseEvent event) throws IOException, ParseException {
        List<Integer> firstMove = coordinatesOfTwoClicks.getFirst();
        List<Integer> secondMove = coordinatesOfTwoClicks.getLast();
        int firstMoveIndex = -1;
        int secondMoveIndex = -1;

        if (newTurn.equals(playerOne)){
            firstMoveIndex = locationOfRedPieces.indexOf(firstMove);
            secondMoveIndex = locationOfBlackPieces.indexOf(secondMove);
        }
        else{
            firstMoveIndex = locationOfBlackPieces.indexOf(firstMove);
            secondMoveIndex = locationOfRedPieces.indexOf(secondMove);
        }
        if (coordinatesOfTwoClicks.size() == 2 && (firstMoveIndex >= 0 && secondMoveIndex >= 0)){
            JSONObject checkersJSON = new JSONObject();
            checkersJSON.put("SessionID", sessionID);
            if (newTurn.equals(playerOne)){
                checkersJSON.put("playerID", playerOne.toString());
            }
            else{
                checkersJSON.put("playerID", playerTwo.toString());
            }
            checkersJSON.put("OldPosition", new Vector2(firstMove.getFirst(), firstMove.getLast()));
            checkersJSON.put("NewPosition", new Vector2(secondMove.getFirst(), secondMove.getLast()));
            checkersJSON.put("gameType", GameTypes.CHECKERS);
            NetworkManager.getNetworkInstance().receiveGameDataFromClient(checkersJSON);
        }
        coordinatesOfTwoClicks.removeFirst();
        coordinatesOfTwoClicks.removeFirst();

    }


    private static void addPieceToSquare(Image image, Button square) {
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

    private static String getUsernameFromUUID(String playerUUID) {
        try {
            File file = new File("src/main/java/ca/ucalgary/seng300/p4/condensation/Networking/Database/Leaderboard/Leaderboard.json");
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

    private static void disableAllButtons() { //when game ends
        for (Button[] buttonRow : chessboard) {
            for (Button button : buttonRow) {
                button.setDisable(true);
            }
        }
    }
}