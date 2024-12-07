package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import ca.ucalgary.seng300.p4.condensation.Backend.Enums.GameResult;
import ca.ucalgary.seng300.p4.condensation.Backend.Enums.PieceColor;
import ca.ucalgary.seng300.p4.condensation.Frontend.GameSessionClient.GameSessionRecord;
import ca.ucalgary.seng300.p4.condensation.Frontend.Receiver.ClientReceiver;
import ca.ucalgary.seng300.p4.condensation.Frontend.Receiver.RecieverListener;
import ca.ucalgary.seng300.p4.condensation.HelloApplication;
import ca.ucalgary.seng300.p4.condensation.Networking.Enums.GameTypes;
import ca.ucalgary.seng300.p4.condensation.Networking.Managers.NetworkManager;
import ca.ucalgary.seng300.p4.condensation.Networking.Services.ChatService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class TicTacToeController {
    @FXML
    private Button btn00;

    @FXML
    private Button btn01;

    @FXML
    private Button btn02;

    @FXML
    private Button btn10;

    @FXML
    private Button btn11;

    @FXML
    private Button btn12;

    @FXML
    private Button btn20;

    @FXML
    private Button btn21;

    @FXML
    private Button btn22;

    private Button[][] buttons;

    private String[][] boardRepresentation = new String[NUMBER_OF_ROWS][NUMBER_OF_COLS];

    public static final int NUMBER_OF_ROWS = 3;
    public static final int NUMBER_OF_COLS = 3;
    private static final String[] COLORS = {PieceColor.RED.toString(), PieceColor.BLUE.toString()};

    private UUID playerOne;
    private UUID playerTwo;
    private UUID sessionID;
    private UUID newTurn;

    @FXML
    public Button forfeitBtn;

    private class TicTacToeReceiver extends RecieverListener {

        public TicTacToeReceiver() {
            super(GameTypes.TICTACTOE);
        }

        @Override
        public void onReceive(JSONObject payload) throws IOException {
            receiveFromBackendTicTacToe(payload);
        }
    }

    public void start() throws IOException, ParseException {
        GameSessionRecord gameSessionRecord = GameSessionRecord.getGameSessionRecord();
        ClientReceiver.addClientReceiver(new TicTacToeReceiver());
        playerOne = gameSessionRecord.getPlayer1ID();
        playerTwo = gameSessionRecord.getPlayer2ID();
        newTurn = gameSessionRecord.getCurrentPlayerID();
        sessionID = gameSessionRecord.getSessionID();
        buttons = new Button[][] {
            {btn00, btn01, btn02},
            {btn10, btn11, btn12},
            {btn20, btn21, btn22}
        };
        for (int i = 0; i < NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < NUMBER_OF_COLS; j++) {
                int fi = i;
                int fj = j;
                buttons[i][j].setOnMouseClicked(_ -> {
                    try {
                        onTicTacToeButtonPress(fi, fj);
                    } catch (IOException | ParseException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }
        for (String[] strings : boardRepresentation){
            Arrays.fill(strings, "EMPTY");
        }
    }

    public void receiveFromBackendTicTacToe(JSONObject backendUpdate) throws IOException {
        String win_or_not_win = (String) backendUpdate.get("UpdateType");
        if (Objects.equals(win_or_not_win, "GAME_END")){
            disableAllButtons();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Aftermath.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            HelloApplication.setActiveScene(scene, "Condensation Application - Aftermath");
        } else{
            onTurnEnd(backendUpdate);
        }
    }

    private void disableAllButtons() { //when game ends
        for (Button[] buttonRow : buttons) {
            for (Button button : buttonRow) {
                button.setDisable(true);
            }
        }
    }

    private void onTurnEnd(JSONObject jsonObject) {
        sessionID = (UUID) jsonObject.get("SessionID");
        newTurn = (UUID) jsonObject.get("PlayerTurn");
        GameSessionRecord.updateBoardRepresentationFromJSON(boardRepresentation, (JSONArray) jsonObject.get("Changes"));
        for (int i = 0; i < NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < NUMBER_OF_COLS; j++) {
                setButtonToPiece(boardRepresentation[i][j], buttons[i][j]);
            }
        }
    }

    private void onTicTacToeButtonPress(int i, int j) throws IOException, ParseException {
        if (boardRepresentation[i][j].equals("EMPTY")) {
            NetworkManager.getNetworkInstance().receiveGameDataFromClient(GameSessionRecord.parseMoveData(
                    sessionID,
                    newTurn,
                    new int[] {j, i}
            ));
        }
    }

    private void setButtonToPiece(String color, Button buttonForBoard) {
        if (color.equals("RED")) {
            buttonForBoard.setTextFill(Color.RED);
            buttonForBoard.setText("X");
        } else if (color.equals("BLUE")) {
            buttonForBoard.setTextFill(Color.BLUE);
            buttonForBoard.setText("O");
        } else {
            buttonForBoard.setText("");
        }
    }

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
        System.out.println("Forfeit confirmed. Closing the application window...");
        try {
            NetworkManager.getNetworkInstance().forfeitGame(sessionID, playerOne);
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
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
    // TODOL: Connect
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
}




