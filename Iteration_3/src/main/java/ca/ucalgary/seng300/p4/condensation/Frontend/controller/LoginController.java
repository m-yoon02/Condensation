package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import ca.ucalgary.seng300.p4.condensation.Networking.Managers.NetworkManager;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.awt.event.MouseEvent;
import java.io.IOException;


public class LoginController {

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
    private Label errorMessage;

    @FXML
    private FXMLLoader loader;

    @FXML
    private Pane root;

    @FXML
    private Stage window;

    @FXML
    private Scene scene;

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
    boolean validatePass(){
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

    @FXML
    void continueToMain(MouseEvent event) throws IOException, ParseException {
        if(validateUser() && validatePass()) {
            errorMessage.setTextFill(Color.BLACK);
            sendJSONObject();

            try {
                loader = new FXMLLoader(getClass().getResource("/ca/ucalgary/seng300/p4/condensation/profileHome.fxml"));
                root = loader.load();

                window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                window.setScene(scene);
                window.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            errorMessage.setText("Invalid Login Credentials!");
            errorMessage.setTextAlignment(TextAlignment.CENTER);
            errorMessage.setTextFill(Color.RED);

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
