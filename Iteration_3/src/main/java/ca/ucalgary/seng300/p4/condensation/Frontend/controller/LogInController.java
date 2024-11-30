package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import ca.ucalgary.seng300.p4.condensation.Networking.Managers.NetworkManager;
import javafx.fxml.FXML;

import javafx.scene.paint.Color;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;


public class LogInController {

    public static final String DEFAULT_PLAYER_ID_FILE = "playerID.txt";

    @FXML
    private Button loginBtn;

    @FXML
    private TextField passwordField;

    @FXML
    private Label passwordLabel;

    @FXML
    private TextField usernameField;

    @FXML
    private Label usernameLabel;

    @FXML
    boolean validateUser(){
        if(usernameField.getText().isBlank()){

            usernameLabel.setTextFill(Color.RED);
            return false;
        }
        else if (usernameField.getText().length() < 3) {

            usernameLabel.setTextFill(Color.RED);
            return false;
        }
        else {
            usernameLabel.setTextFill(Color.GREEN);
            return true;
        }
    }

    @FXML
    boolean validatePassword(){
        if(passwordField.getText().isBlank()){
            passwordLabel.setTextFill(Color.RED);
            return false;
        }
        else if (passwordField.getText().length() < 3) {
            passwordLabel.setTextFill(Color.RED);
            return false;
        }
        else{
            passwordLabel.setTextFill(Color.GREEN);
            return true;
        }
    }

    public JSONObject createJSONObject(){
        JSONObject obj = new JSONObject();
        obj.put("username", usernameField.getText());
        obj.put("password", passwordField.getText());
        return obj;
    }

    public void sendJSONObject() throws IOException, ParseException {
        JSONObject obj = createJSONObject();
        NetworkManager.getNetworkInstance().signUpHandler(createJSONObject());
    }


}
