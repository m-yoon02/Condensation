package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import ca.ucalgary.seng300.p4.condensation.Networking.Managers.NetworkManager;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
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
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javafx.scene.input.MouseEvent;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class LoginController {

    public static final String DEFAULT_PLAYER_ID_FILE = "/Users/nateshoad/Desktop/University /Java_Files/SENG300F24P/seng300-f24-project/Iteration_3/src/main/java/ca/ucalgary/seng300/p4/condensation/Networking/Database/Leaderboard/playerID.json";

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
            if(sendJSONObject()) {
                errorMessage.setTextFill(Color.BLACK);
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

    public boolean sendJSONObject() throws IOException, ParseException {
        try {
            JSONObject returnPayload = NetworkManager.getNetworkInstance().playerLoginHandler(createJSONObject());

            FileWriter fileWriter = new FileWriter(DEFAULT_PLAYER_ID_FILE);
            fileWriter.write(returnPayload.toJSONString());
            fileWriter.flush();
            fileWriter.close();
            return true;
        }
        catch (Exception e){
            String errorString = e.getMessage();
            errorMessage.setTextFill(Color.RED);
            errorMessage.setAlignment(Pos.CENTER);
            errorMessage.setTextAlignment(TextAlignment.CENTER);
            errorMessage.setText(errorString);
            return false;
        }


    }


}
