package ca.ucalgary.seng300.p4.condensation.Frontend.controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Class for the WelcomeScreen, handles all user interactions
 *
 * Last Iteration: November 30, 2024
 */

public class WelcomeScreenController {

    // FXML Elements

    @FXML
    private FXMLLoader loader;

    @FXML
    private Pane root;

    @FXML
    private Stage window;

    @FXML
    private Scene scene;

    /**
     * Function: moveToLogin
     * @param event Triggered by mouse click
     * @throws IOException Exception for when the FXML file does not open
     *
     * Transitions user from the welcome screen to the login
     */
    @FXML
    public void moveToLogin(MouseEvent event) throws IOException {

        // Load the Login.fxml file to change scene
        loader = new FXMLLoader(getClass().getResource("/ca/ucalgary/seng300/p4/condensation/Login.fxml"));
        root = loader.load();

        // Set the window to the current viewing screen
        window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        window.setScene(scene);
        // Set the window and scene to the login screen
        window.setScene(scene);
        window.show();
    }

    /**
     * Function: moveToSignup
     * @param event Triggered by mouse click
     * @throws IOException Exception for when the FXML file does not open
     *
     * Transitions user from the welcome screen to signup screen
     */

    @FXML
    public void moveToSignup(MouseEvent event) throws IOException {

        // Load the Login.fxml file to change scene
        loader = new FXMLLoader(getClass().getResource("/ca/ucalgary/seng300/p4/condensation/SignUp.fxml"));
        root = loader.load();

        // Set the window to the current viewing screen
        window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        window.setResizable(false);
        // Set the window and scene to the login screen
        window.setScene(scene);
        window.show();

    }

    /**
     * Function: progMenu
     * @param event Event for when menu is clicked
     * @throws URISyntaxException URL Exception for invalid url links
     * @throws IOException IOException for invalid url addressing
     */
    @FXML
    void progMenu(ActionEvent event) throws URISyntaxException, IOException {

        // Browse the README page when program instructions menu item is clicked
        Desktop.getDesktop().browse(new URI("https://csgit.ucalgary.ca/ssafe/seng300-f24-project/-/blob/main/Iteration_3/Deliverables/README.md?ref_type=heads"));

    }

    /**
     * Function: teamMenu
     * @param event Event for when menu is clicked
     * @throws URISyntaxException URL Exception for invalid url links
     * @throws IOException IOException for invalid url addressing
     */
    @FXML
    void teamMenu(ActionEvent event) throws URISyntaxException, IOException {

        // Browse the team page when program instructions menu item is clicked
        Desktop.getDesktop().browse(new URI("https://csgit.ucalgary.ca/ssafe/seng300-f24-project/-/blob/main/team.md?ref_type=heads"));

    }

    /**
     * Function: aboutMenu
     * @param event Event for when menu is clicked
     * @throws URISyntaxException URL Exception for invalid url links
     * @throws IOException IOException for invalid url addressing
     */
    @FXML
    void aboutMenu(ActionEvent event) throws URISyntaxException, IOException {

        // Browse the README page when program instructions menu item is clicked
        Desktop.getDesktop().browse(new URI("https://csgit.ucalgary.ca/ssafe/seng300-f24-project/-/blob/main/README.md?ref_type=heads"));

    }


}
