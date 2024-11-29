package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import ca.ucalgary.seng300.p4.condensation.Networking.Managers.NetworkManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.json.simple.JSONObject;
import javafx.scene.text.Text;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.Objects;

public class SignUpController {

    @FXML
    private Button continueBtn;

    @FXML
    private Text errorMessage;

    @FXML
    private TextField passwordField;

    @FXML
    private Label passwordLabel;

    @FXML
    private TextField usernameField;

    @FXML
    private Label usernameLabel;

    @FXML
    private Parent root;

    @FXML
    private Stage window;

    @FXML
    private Scene scene;

    @FXML
    void validateUser(KeyEvent event) {

        if(usernameField.getText().isBlank()){

            usernameLabel.setTextFill(Color.RED);

        }
        else if (usernameField.getText().length() < 3){

            usernameLabel.setTextFill(Color.RED);

        }
        else{

            usernameLabel.setTextFill(Color.GREEN);

        }

    }

    @FXML
    void validatePass(KeyEvent event) {

        if(passwordField.getText().isBlank()){

            passwordLabel.setTextFill(Color.RED);

        }
        else if (passwordField.getText().length() < 3){

            passwordLabel.setTextFill(Color.RED);

        }
        else{

            passwordLabel.setTextFill(Color.GREEN);

        }

    }

    @FXML
    void continueToLogin(MouseEvent event) throws IOException {

        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Login.fxml")));
            window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            window.setScene(scene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }






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
