package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import ca.ucalgary.seng300.p4.condensation.Networking.Database.Leaderboard.Leaderboard;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.UUID;

public class EditUserController {
    @FXML
    Label usernameLabel;

    @FXML
    Label messageLabel;

    @FXML
    TextField newUserNameField;

    public static UUID playerUUID;

    @FXML
    void onConfirmClicked() throws IOException, ParseException {
        boolean changed = false;
        if (validateUser()) {
            changed =  Leaderboard.getInstance().changeName(playerUUID, newUserNameField.getText());
        }
        if (changed) {
            messageLabel.setText("Changed Username successfully");
            messageLabel.setTextFill(Color.GREEN);

            // TODO change ProfileHomeController.usernameField
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
