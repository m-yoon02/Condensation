package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import ca.ucalgary.seng300.p4.condensation.Networking.Managers.NetworkManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.json.simple.JSONObject;
import javafx.scene.text.Text;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class SignUpController {

    @FXML
    private Button continueBtn;

    @FXML
    private Text errorMessage;

    @FXML
    private TextField passwordField;

    @FXML
    private TextField usernameField;

    @FXML



    public JSONObject createJSONObject(){
        JSONObject obj = new JSONObject();
        obj.put("username", usernameField.getText());
        obj.put("password", passwordField.getText());

        return obj;

    }

    public void sendJSONObject() throws IOException, ParseException {

        NetworkManager.getNetworkInstance().signUpHandler(createJSONObject());


    }


}
