package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import ca.ucalgary.seng300.p4.condensation.Networking.Database.Leaderboard.Leaderboard;
import ca.ucalgary.seng300.p4.condensation.Networking.Managers.NetworkManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

public class EditUserController {
    @FXML
    Label usernameLabel;

    private ProfileHomeController profileHomeController;

    // Setter for dependency injection
    public void setProfileHomeController(ProfileHomeController profileHomeController) {
        this.profileHomeController = profileHomeController;
    }
    @FXML
    Label messageLabel;

    @FXML
    TextField newUserNameField;

    public static UUID playerUUID;

    public static String newUsername;

    @FXML
    void onConfirmClicked() throws IOException, ParseException {
        boolean changed = false;
        if (validateUser()) {
            changed =  Leaderboard.getInstance().changeName(playerUUID, newUserNameField.getText());
            Leaderboard.getInstance().saveData();
        }
        if (changed) {
            messageLabel.setText("Changed Username successfully");
            messageLabel.setTextFill(Color.GREEN);

            newUsername = newUserNameField.getText();
            FileReader read = new FileReader(NetworkManager.PLAYER_ID_FILE);
            JSONParser parser = new JSONParser();
            JSONObject meowlol = (JSONObject) parser.parse(read);
            meowlol.put("username", newUsername);

            String username = (String) meowlol.get("username");
            System.out.println("MEOWLOL" + meowlol);
            FileWriter writer = new FileWriter(NetworkManager.PLAYER_ID_FILE);
            writer.write(meowlol.toJSONString());
            writer.flush();
            writer.close();
            profileHomeController.usernameField.setText(username);
            System.out.println("NEW USER NAME = " + profileHomeController.usernameField.getText());


            usernameLabel.setText(newUsername);
        }
    }


    /**
     * Method: validateUser()
     * @return boolean: returns true if username field is not blank and at least three characters, false otherwise
     * @author Natesh
     *
     * Processes the username field for validity
     */

    @FXML
    boolean validateUser() {

        // Check if the username field is blank
        if(newUserNameField.getText().isBlank()){

            // Set the label colour red and return false to indicate invalid
            usernameLabel.setTextFill(Color.RED);
            return false;

        }
        // Check if the username field is at least three characters
        else if (newUserNameField.getText().length() < 3){

            // Set the label colour red and return false to indicate invalid
            usernameLabel.setTextFill(Color.RED);
            return false;

        }
        // Username is valid, return true
        else{

            // Set the label colour green and return true to indicate valid
            usernameLabel.setTextFill(Color.GREEN);
            return true;
        }
    }

    public static void setPlayerUUID(UUID uuid) {
        EditUserController.playerUUID = uuid;
    }
}
