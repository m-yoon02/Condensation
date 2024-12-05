package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import ca.ucalgary.seng300.p4.condensation.Networking.Database.Leaderboard.Leaderboard;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.UUID;

public class EditPassController {
    @FXML
    Label passwordLabel;

    @FXML
    Label messageLabel;

    @FXML
    TextField newPassField;

    public static UUID playerUUID;

    @FXML
    void onConfirmClicked() throws IOException, ParseException {
        boolean changed = false;
        if (validatePass()) {
            changed =  Leaderboard.getInstance().changeName(playerUUID, newPassField.getText());
        }
        if (changed) {
            messageLabel.setText("Changed Password successfully");
            messageLabel.setTextFill(Color.GREEN);
        }
    }

    /**
     * Method: validatePass()
     * @return boolean: returns true if password field is not blank and at least three characters, false otherwise
     * @author Natesh
     *
     * Processes the password field for validity
     */
    @FXML
    boolean validatePass() {

        // Check if the password field is blank
        if(newPassField.getText().isBlank()){

            // Set the label colour red and return false to indicate invalid
            passwordLabel.setTextFill(Color.RED);
            return false;

        }
        //Check if the password field contains at least three characters
        else if (newPassField.getText().length() < 3){

            //  Set the label colour red and return false to indicate invalid
            passwordLabel.setTextFill(Color.RED);
            return false;

        }
        // Password is valid, return true
        else{
            // Set the label colour green and return true to indicate valid
            passwordLabel.setTextFill(Color.GREEN);
            return true;

        }
    }

    public static void setPlayerUUID(UUID uuid) {
        EditPassController.playerUUID = uuid;
    }
}
