package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import javafx.fxml.FXML;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.json.simple.JSONObject;


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

    public JSONObject createJSONObject(){
        JSONObject obj = new JSONObject();
        obj.put("username", usernameField.getText());
        obj.put("password", passwordField.getText());
        return obj;
    }


}
