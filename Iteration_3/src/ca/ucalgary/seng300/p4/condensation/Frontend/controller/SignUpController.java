package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import ca.ucalgary.seng300.p4.condensation.condensation.Networking.Managers.NetworkManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.json.simple.JSONObject;
import javafx.scene.text.Text;

public class SignUpController {

    @FXML
    private Button continueBtn;

    @FXML
    private TextField dayField;

    @FXML
    private TextField displayField;

    @FXML
    private TextField emailField;

    @FXML
    private Text errorMessage;

    @FXML
    private TextField monthField;

    @FXML
    private TextField passwordField;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField yearField;

    @FXML
    public void authenticateEmail(ActionEvent event) {
        System.out.println("Key has been typed!");
    }

    // After information has been authenticated
    public JSONObject createJSONObject(){
        JSONObject obj = new JSONObject();
        obj.put("day", dayField.getText());
        obj.put("month", monthField.getText());
        obj.put("year", yearField.getText());
        obj.put("display", displayField.getText());
        obj.put("email", emailField.getText());
        obj.put("month", monthField.getText());
        obj.put("password", passwordField.getText());
        obj.put("username", usernameField.getText());

        return obj;

    }

    public void sendJSONObject(){
        NetworkManager.getNetworkInstance().signUpHandler(createJSONObject());

    }


}
