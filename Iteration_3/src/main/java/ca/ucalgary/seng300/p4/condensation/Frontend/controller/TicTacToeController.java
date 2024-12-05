package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import ca.ucalgary.seng300.p4.condensation.Backend.DTOs.Vector2;
import ca.ucalgary.seng300.p4.condensation.Backend.Enums.GameResult;
import ca.ucalgary.seng300.p4.condensation.Frontend.GameSessionClient.GameSessionRecord;
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
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class TicTacToeController {
    private static Button btn00;
    private static Button btn01;
    private static Button btn02;
    private static Button btn10;
    private static Button btn11;
    private static Button btn12;
    private static Button btn20;
    private static Button btn21;
    private static Button btn22;
    public int numberOfPiecesOnBoard = 0;
    static Button[][] buttons = new Button[][] {
        {btn00, btn01, btn02},
        {btn10, btn11, btn12},
        {btn20, btn21, btn22}
    };

    public static final int NUMBER_OF_ROWS = 3;
    public static final int NUMBER_OF_COLS = 3;
    public static GameResult resultOfTicTacToe;
    public static UUID playerOne;
    public static UUID playerTwo;
    public static UUID sessionID;
    public static UUID newTurn;

    @FXML
    private static Stage stage;
    @FXML
    public static Label playerStatus;

    @FXML
    public static Button forfeitBtn;

    @FXML
    public void initialize() throws IOException, ParseException {

        buttons = new Button[][] {
                {btn00, btn01, btn02},
                {btn10, btn11, btn12},
                {btn20, btn21, btn22}
        };

        GameSessionRecord gameSessionRecord = GameSessionRecord.getGameSessionRecord();
        playerOne = gameSessionRecord.getPlayer1ID();
        playerTwo = gameSessionRecord.getPlayer2ID();
        newTurn = gameSessionRecord.getCurrentPlayerID();
        sessionID = gameSessionRecord.getSessionID();

        playerStatus.setText("RED TURN - YOUR TURN");
        playerStatus.setTextFill(Color.RED);
    }

    public static void receiveFromBackendTicTacToe(JSONObject backendUpdate) throws IOException {
        String win_or_not_win = (String) backendUpdate.get("UpdateType");
        if (Objects.equals(win_or_not_win, "GAME_END")){
            resultOfTicTacToe = (GameResult) backendUpdate.get("GameState");
            if (resultOfTicTacToe == GameResult.WIN){
                UUID winningPlayer = (UUID) backendUpdate.get("PlayerWon");
                String nameOfWinningPlayer = getUsernameFromUUID(String.valueOf(winningPlayer));
                playerStatus.setText(String.format("Player %s won the game!",nameOfWinningPlayer));
                if (winningPlayer.equals(playerOne)){
                    playerStatus.setTextFill(Color.GREEN);
                }
                else{
                    playerStatus.setTextFill(Color.RED);
                }
            }
            else{
                playerStatus.setText("The game ended in a tie!");
                playerStatus.setTextFill(Color.BLACK);
            }
            disableAllButtons();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("connect4-board.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            stage.setTitle("Condensation Application - Aftermath");
            stage.setScene(scene);
            stage.show();
        }
        else{
            sessionID = (UUID) backendUpdate.get("SessionID");
            newTurn = (UUID) backendUpdate.get("PlayerTurn");
            String[][] connectFourBackendBoard = (String[][]) backendUpdate.get("Changes");
            int rowIndex = 0;
            int colIndex;
            for (String[] strings : connectFourBackendBoard) {
                colIndex = 0;
                for (String piece : strings) {
                    Button buttonForBoard = buttons[rowIndex][colIndex];
                    if (Objects.equals(piece, "RED")){
                        buttonForBoard.setText("X");
                        buttonForBoard.setTextFill(Color.RED);
                    }
                    else if (Objects.equals(piece, "BLUE")){
                        buttonForBoard.setText("O");
                        buttonForBoard.setTextFill(Color.BLUE);
                    }
                    else{
                        buttonForBoard.setText("");
                    }
                }
                rowIndex += 1;
            }
            if (newTurn.equals(playerTwo)){
                String nameOfOpponentPlayer = getUsernameFromUUID(String.valueOf(newTurn));
                playerStatus.setText(String.format("It's %s's turn!", nameOfOpponentPlayer));
                playerStatus.setTextFill(Color.BLUE);
            }
            else{
                String nameOfMainPlayer = getUsernameFromUUID(String.valueOf(newTurn));
                playerStatus.setText(String.format("It's %s's turn!", nameOfMainPlayer));
                playerStatus.setTextFill(Color.RED);
            }
        }
    }

    private static void disableAllButtons() { //when game ends
        for (Button[] buttonRow : buttons) {
            for (Button button : buttonRow) {
                button.setDisable(true);
            }
        }
    }

    @FXML
    public void returnToBackendTicTacToe(javafx.event.ActionEvent event) throws IOException, ParseException {
        Button clickedButton = (Button) event.getSource();

        int row = -1;
        int col = -1;

        boolean buttonFound = false;
        for (int i = 0; i < NUMBER_OF_ROWS; i++) {
            if (buttonFound){
                break;
            }
            for (int j = 0; j < NUMBER_OF_COLS; j++) {
                System.out.println(buttons[i][j]);
                if (buttons[i][j].equals(clickedButton)) {
                    row = i;
                    col = j;
                    buttonFound = true;
                    break;
                }
            }
        }

        if (row >= 0 && col >= 0){

            JSONObject ticTacToeJSON = new JSONObject();
            ticTacToeJSON.put("SessionID", sessionID);
            if (newTurn == playerOne){
                ticTacToeJSON.put("playerID", playerOne.toString());
            }
            else{
                ticTacToeJSON.put("playerID", playerTwo.toString());
            }
            ticTacToeJSON.put("New Position", new Vector2(row, col));
            ticTacToeJSON.put("gameType", GameTypes.TICTACTOE);
            NetworkManager.getNetworkInstance().receiveGameDataFromClient(ticTacToeJSON);
        }
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
            System.err.printf("Error reading leaderboard.json: %s%n", e.getMessage());
        }
        return null;
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

        // Close the current window
        Stage stage = (Stage) forfeitBtn.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void onForfeitNoClick() {
        System.out.println("Forfeit canceled. The window remains open.");
    }

    @FXML
    TextArea chatBox;

    @FXML
    Button sendBtn;

    @FXML
    ScrollPane chatScrollPane;

    @FXML
    VBox paneInsideScrollPane;

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


    String username = "Me";
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




