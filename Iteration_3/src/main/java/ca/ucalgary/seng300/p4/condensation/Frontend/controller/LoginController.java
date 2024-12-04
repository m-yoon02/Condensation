package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import ca.ucalgary.seng300.p4.condensation.Networking.Managers.NetworkManager;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import javafx.scene.input.MouseEvent;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Class for the Login screen, handles all user interactions.
 *
 * Last Iteration: November 30, 2024
 */
public class LoginController {

    // Path to the json file for storing the username and UUID
    public static final String DEFAULT_PLAYER_ID_FILE = "Iteration_3/src/main/java/ca/ucalgary/seng300/p4/condensation/Networking/Database/Leaderboard/playerID.json";

    @FXML
    public Button loginBtn;


    // FXML Elements

    @FXML
    public TextField passwordField;

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

    /**
     * Method: validateUser()
     * @return boolean: returns true if username field is not blank and at least three characters, false otherwise
     *
     * Processes the username field for validity
     */

    @FXML
    boolean validateUser() {

        // Check if the username field is blank
        if(usernameField.getText().isBlank()){

            // Set the label colour red and return false to indicate invalid
            usernameLabel.setTextFill(Color.RED);
            return false;

        }
        // Check if the username field is at least three characters
        else if (usernameField.getText().length() < 3){

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

    /**
     * Method: validatePass()
     * @return boolean: returns true if password field is not blank and at least three characters, false otherwise
     *
     * Processes the password field for validity
     */
    @FXML
    boolean validatePass() {

        // Check if the password field is blank
        if(passwordField.getText().isBlank()){

            // Set the label colour red and return false to indicate invalid
            passwordLabel.setTextFill(Color.RED);
            return false;

        }
        //Check if the password field contains at least three characters
        else if (passwordField.getText().length() < 3){

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

    /**
     * Method: continueToMain
     * @param event Triggered by the mouseclick
     * @throws IOException Exception for when the FXML file does not exist
     * @throws ParseException Exception for creating a JSON Object
     *
     * Verifies information using sub-methods and creates a json object, proceeds to the main page
     */
    @FXML
    void continueToMain(MouseEvent event) throws IOException, ParseException {

        // Check if the user provided data is valid
        if(validateUser() && validatePass()) {

            // Check if the user credentials match
            if(sendJSONObject()) {

                // Set error message to black to indicate valid credentials
                errorMessage.setTextFill(Color.BLACK);
                try {
                    // Load the profileHome.fxml file to change scene
                    loader = new FXMLLoader(getClass().getResource("/ca/ucalgary/seng300/p4/condensation/profileHome.fxml"));
                    root = loader.load();

                    // Set the window and scene to the login screen
                    window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    window.setScene(scene);
                    window.setResizable(false);
                    window.show();
                } catch (IOException e) {
                    // Catch exception and print call stack
                    e.printStackTrace();
                }
            }
        }
        // Credentials do not match, show error
        else{

            // Show error message to user
            errorMessage.setText("Invalid Login Credentials!");
            errorMessage.setTextAlignment(TextAlignment.CENTER);
            errorMessage.setTextFill(Color.RED);

        }
    }

    /**
     * Method: createJSONObject
     * @return JSONObject: a JSON object with the username and password
     *
     * Creates a JSONObject to be sent to the NetworkManager
     */
    public JSONObject createJSONObject(){

        // Initialize a JSONObject
        JSONObject obj = new JSONObject();

        // Put the username and password from textfields
        obj.put("username", usernameField.getText());
        obj.put("password", passwordField.getText());

        return obj;

    }


    /**
     * Method: sendJSONObject
     * @throws IOException: IOException may appear when saving the JSONObject in leaderboard.json
     * @throws ParseException: ParseException may appear when creating JSONObject
     *
     * @return boolean: True if the user exists in the database and has provided valid credentials, false otherwise
     */

    public boolean sendJSONObject() throws IOException, ParseException {
        try {
            // Initialize the return payload to the return JSON object of playerLoginHandler
            JSONObject returnPayload = NetworkManager.getNetworkInstance().playerLoginHandler(createJSONObject());

            // Initialize a file writer to save username and UUID
            FileWriter fileWriter = new FileWriter(DEFAULT_PLAYER_ID_FILE);

            // Write the username and UUI
            fileWriter.write(returnPayload.toJSONString());
            fileWriter.flush();

            // Close file writer
            fileWriter.close();

            // Return true, user has provided correct credentials
            return true;
        }
        // Catch any exceptions
        catch (Exception e){

            // Store the message return by loginHandler
            String errorString = e.getMessage();

            // Show the error message to user
            errorMessage.setTextFill(Color.RED);
            errorMessage.setAlignment(Pos.CENTER);
            errorMessage.setTextAlignment(TextAlignment.CENTER);
            errorMessage.setText(errorString);

            // Return false, user has provided incorrect credentials
            return false;
        }


    }

    /**
     * Method: returnBack
     * @param event Triggered by the mouseclick
     * @throws IOException Exception for when the FXML file does not exist
     *
     * Returns the user back to the welcome screen
     */
    @FXML
    void returnBack(MouseEvent event) throws IOException {

        try {
            // Load the WelcomeScreen.fxml file to change scene
            loader = new FXMLLoader(getClass().getResource("/ca/ucalgary/seng300/p4/condensation/WelcomeScreen.fxml"));
            root = loader.load();

            // Set the window and scene to the login screen
            window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            window.setScene(scene);
            window.setResizable(false);
            window.show();
        } catch (IOException e) {
            // Catch exception and print call stack
            e.printStackTrace();
        }

    }


}
