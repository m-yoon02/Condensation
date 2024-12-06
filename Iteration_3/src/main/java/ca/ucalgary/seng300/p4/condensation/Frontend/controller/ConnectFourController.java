package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import ca.ucalgary.seng300.p4.condensation.Frontend.GameSessionClient.GameSessionRecord;
import ca.ucalgary.seng300.p4.condensation.Frontend.Receiver.ClientReceiver;
import ca.ucalgary.seng300.p4.condensation.Frontend.Receiver.RecieverListener;
import ca.ucalgary.seng300.p4.condensation.HelloApplication;
import ca.ucalgary.seng300.p4.condensation.Networking.Enums.GameTypes;
import ca.ucalgary.seng300.p4.condensation.Networking.Managers.NetworkManager;
import ca.ucalgary.seng300.p4.condensation.Networking.Services.ChatService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.UUID;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class ConnectFourController {

    @FXML
    private GridPane connectFourGrid;

    @FXML
    private Label playerTurnText;

    private final Button[][] buttons = new Button[6][7];

    public static final int NUMBER_OF_ROWS = 6;
    public static final int NUMBER_OF_COLS = 7;

    public UUID playerOne;
    public UUID playerTwo;
    public UUID sessionID;
    public UUID newTurn;

    private class ConnectFourReceiver extends RecieverListener {

        public ConnectFourReceiver() {
            super(GameTypes.CONNECT4);
        }

        @Override
        public void onReceive(JSONObject payload) throws IOException {
            receiveConnectFour(payload);
        }
    }

    @FXML
    public void initialize() throws ParseException, IOException {
        for (int row = 0; row < NUMBER_OF_ROWS; row++) {
            for (int col = 0; col < NUMBER_OF_COLS; col++) {
                String buttonId = "btn" + row + col;
                Button button = (Button) connectFourGrid.lookup("#" + buttonId);
                buttons[row][col] = button;

                if (button != null) {
                    int finalRow = row;
                    int finalCol = col;
                    button.setOnMouseClicked(event -> {
                        try {
                            returnConnectFour(finalRow, finalCol);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        }

        GameSessionRecord gameSessionRecord = GameSessionRecord.getGameSessionRecord();
        ClientReceiver.addClientReceiver(new ConnectFourReceiver());

        playerOne = gameSessionRecord.getPlayer1ID();
        playerTwo = gameSessionRecord.getPlayer2ID();
        newTurn = gameSessionRecord.getCurrentPlayerID();
        sessionID = gameSessionRecord.getSessionID();
    }

    public void receiveConnectFour(JSONObject payload) {
        try {
            String updateType = (String) payload.get("UpdateType");
            switch (updateType) {
                case "GAME_END":
                    handleGameEnd(payload);
                    break;
                case "TURN_END":
                    handleTurnEnd(payload);
                    break;
                default:
                    System.out.println("Unknown UpdateType: " + updateType);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleGameEnd(JSONObject update) throws IOException {
        String gameState = (String) update.get("GameState");
        if ("WIN".equals(gameState)) {
            String playerWon = (String) update.get("PlayerWon");
            playerTurnText.setText(String.format("Player %s won the game!", playerWon));
        } else if ("TIE".equals(gameState)) {
            playerTurnText.setText("The game ended in a tie!");
        }
        disableAllButtons();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Aftermath.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        HelloApplication.setActiveScene(scene, "Condensation Application - Aftermath");
    }

    private void handleTurnEnd(JSONObject update) {
        sessionID = (UUID) update.get("SessionID");
        newTurn = (UUID) update.get("PlayerTurn");
        String playerTurnName = getUsernameFromUUID(newTurn.toString());
        playerTurnText.setText(String.format("It's %s's turn!", playerTurnName));

        JSONArray changes = (JSONArray) update.get("Changes");
        updateBoard(changes);
    }

    private static String getUsernameFromUUID(String playerUUID) {
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
            System.err.println("Error reading leaderboard.json: " + e.getMessage());
        }
        return null;
    }

    private void updateBoard(JSONArray changes) {
        for (int row = 0; row < changes.size(); row++) {
            JSONArray rowArray = (JSONArray) changes.get(row);
            for (int col = 0; col < rowArray.size(); col++) {
                String state = (String) rowArray.get(col);
                updateButtonState(buttons[row][col], state);
            }
        }
    }

    private void handleForfeit() throws IOException {
        disableAllButtons();
        try {
            NetworkManager.getNetworkInstance().forfeitGame(sessionID, playerOne);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private static void updateButtonState(Button button, String state) {
        switch (state) {
            case "EMPTY":
                button.setStyle("-fx-background-color: white;");
                button.setDisable(false);
                break;
            case "ORANGE":
                button.setStyle("-fx-background-color: orange;");
                button.setDisable(true);
                break;
            case "BLUE":
                button.setStyle("-fx-background-color: blue;");
                button.setDisable(true);
                break;
            default:
                button.setStyle("-fx-background-color: gray;");
                button.setDisable(true);
                break;
        }
    }

    public void returnConnectFour(int row, int col) throws IOException, ParseException {
        NetworkManager.getNetworkInstance().receiveGameDataFromClient(GameSessionRecord.parseMoveData(
                sessionID,
                newTurn,
                new int[]{col, row}
        ));
    }

    private void disableAllButtons() {
        for (Button[] row : buttons) {
            for (Button button : row) {
                button.setDisable(true);
            }
        }
    }

    //chat
    @FXML
    private TextArea chatBox;

    @FXML
    private Button sendBtn;

    @FXML
    private ScrollPane chatScrollPane;

    @FXML
    private VBox paneInsideScrollPane;

    private final String username1 = "Me";
    private final String username2 = "RoboAI";

    private final String messageHBoxStyle = "    -fx-pref-width: 50;\n    -fx-max-width: 200;";
    private final String senderNameStyle = "-fx-font-size: 13;\n    -fx-font-family: 'Oswald', sans-serif;\n    -fx-text-fill: White;\n    -fx-font-weight: bold;";
    private final String timeSentStyle = "-fx-font-size: 8;\n    -fx-font-family: 'Oswald', sans-serif;\n    -fx-text-fill: White;";
    private final String nameTimeHBoxStyle = "-fx-alignment: CENTER_LEFT";
    private final String messageStyle = "-fx-font-size: 12;\n    -fx-font-family: 'Oswald', sans-serif;\n    -fx-text-fill: White;";

    @FXML
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
                LocalDateTime localDateTime1 = LocalDateTime.parse(((JSONObject) chatMessages.get(0)).get("date").toString());
                date1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(localDateTime1);

                LocalDateTime localDateTime2 = LocalDateTime.parse(((JSONObject) chatMessages.get(1)).get("date").toString());
                date2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(localDateTime2);

                messageStr2 = ((JSONObject) chatMessages.get(1)).get("message").toString();
                responded = true;
            } else {
                LocalDateTime localDateTime1 = LocalDateTime.now().truncatedTo(java.time.temporal.ChronoUnit.MINUTES);
                date1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(localDateTime1);
            }

            addMessageToChat(username1, messageStr, date1);

            if (responded) {
                addMessageToChat(username2, messageStr2, date2);
            }
            chatBox.setText("");
            chatScrollPane.setVvalue(1.0);
        }
    }

    private void addMessageToChat(String sender, String messageContent, String timestamp) {
        HBox messageHBox = new HBox();
        messageHBox.setStyle(messageHBoxStyle);
        messageHBox.setPadding(new Insets(5, 10, 5, 5));

        Label senderName = new Label(sender);
        senderName.setStyle(senderNameStyle);
        senderName.setPadding(new Insets(0, 0, -3, 0));

        Label timeSent = new Label(timestamp);
        timeSent.setStyle(timeSentStyle);
        timeSent.setPadding(new Insets(0, 0, 0, 3));

        HBox nameTimeHBox = new HBox(senderName, timeSent);
        nameTimeHBox.setStyle(nameTimeHBoxStyle);

        Label message = new Label(messageContent);
        message.setStyle(messageStyle);
        message.setWrapText(true);

        VBox vBox = new VBox(nameTimeHBox, message);
        messageHBox.getChildren().add(vBox);

        paneInsideScrollPane.getChildren().add(messageHBox);
    }

    @FXML
    private Button forfeitBtn;

    @FXML
    public void onForfeitBtnClick() throws IOException {
        // Create a confirmation alert for the forfeit action
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to forfeit?");
        alert.setTitle("Forfeit");
        alert.setHeaderText("Forfeit Game");

        // Display the alert and capture the user's response
        Optional<ButtonType> choice = alert.showAndWait();

        // Process the user's choice
        if (choice.isPresent() && choice.get() == ButtonType.OK) {
            onForfeitYesClick();
        } else {
            onForfeitNoClick();
        }
    }

    @FXML
    public void onForfeitYesClick() throws IOException {
        System.out.println("Player chose to forfeit the game.");
        // Additional logic to handle forfeiting (e.g., notify opponent, update game state)
        handleForfeit();
    }

    @FXML
    public void onForfeitNoClick() {
        System.out.println("Player canceled the forfeit action.");
    }

}
