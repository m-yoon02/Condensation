package ca.ucalgary.seng300.p4.condensation.Frontend.controller;
import ca.ucalgary.seng300.p4.condensation.Frontend.controller.LoginController;
import ca.ucalgary.seng300.p4.condensation.HelloApplication;
import javafx.scene.layout.Pane;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.testfx.framework.junit.ApplicationTest;

import static org.junit.Assert.*;

public class LoginControllerTest extends ApplicationTest {

    TextField usernameField;
    TextField passwordField;
    Button loginButton;
    Label errorMessage;
    Label usernameLabel;
    Label passwordLabel;
    Button returnWelcomeButton;
    Pane profileHomePane;

    FXMLLoader fxmlLoader;
    Scene scene;

    LoginController loginController;

    HelloApplication application;


    @Override
    public void start(Stage stage) throws Exception {
        application = new HelloApplication();
        application.start(stage);
        fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Login.fxml"));
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
        loginController = fxmlLoader.getController();
    }

    @Before
    public void setUp() throws Exception {
        usernameField = (TextField) lookup("#usernameField").queryAs(TextField.class);
        usernameLabel = (Label) lookup("#usernameLabel").queryAs(Label.class);
        passwordField = (TextField) lookup("#passwordField").queryAs(TextField.class);
        passwordLabel = (Label) lookup("#passwordLabel").queryAs(Label.class);
        loginButton = (Button) lookup("#loginBtn").queryAs(Button.class);
        errorMessage = (Label) lookup("#errorMessage").queryAs(Label.class);
        returnWelcomeButton = (Button) lookup("#returnWelcome").queryAs(Button.class);

    }

    @After
    public void tearDown() throws Exception {
        usernameField = null;
        usernameLabel = null;
        passwordField = null;
        passwordLabel = null;
        loginButton = null;
        errorMessage = null;
        returnWelcomeButton = null;
    }

//    @Test
//    public void check_empty_username() {
//
//        usernameField.setText("");
//
//        assertTrue(usernameField.getText().isBlank());
//        assertFalse(loginController.validateUser());
//        assertEquals(usernameLabel.getTextFill(), Color.RED);
//
//    }
//
//    @Test
//    public void check_username_characters() {
//
//        usernameField.setText("Hi");
//
//        assertFalse(usernameField.getText().isBlank());
//        assertFalse(loginController.validateUser());
//        assertEquals(usernameLabel.getTextFill(), Color.RED);
//
//    }

//    @Test
//    public void check_valid_username() {
//
//        usernameField.setText("player123");
//
//        assertFalse(usernameField.getText().isBlank());
//        assertTrue(loginController.validateUser());
//        assertEquals(usernameLabel.getTextFill(), Color.GREEN);
//    }

    @Test
    public void check_empty_password() {

        passwordField.setText("");

        assertTrue(passwordField.getText().isBlank());
        assertFalse(loginController.validatePass());
        assertEquals(passwordLabel.getTextFill(), Color.RED);

    }

//    @Test
//    public void check_username_password() {
//
//        passwordField.setText("Hi");
//
//        assertFalse(passwordField.getText().isBlank());
//        assertFalse(loginController.validatePass());
//        assertEquals(passwordLabel.getTextFill(), Color.RED);
//
//    }

    @Test
    public void check_valid_password() {

        passwordField.setText("player123");

        assertFalse(passwordField.getText().isBlank());
        assertTrue(loginController.validatePass());
        assertEquals(passwordLabel.getTextFill(), Color.GREEN);
    }

//    @Test
//    public void test_invalid_username_login() {
//
//        usernameField.setText("Hi");
//        passwordField.setText("123456");
//
//        assertFalse(usernameField.getText().isBlank());
//        assertFalse(loginController.validateUser());
//        assertEquals(usernameLabel.getTextFill(), Color.RED);
//
//        assertFalse(passwordField.getText().isBlank());
//        assertTrue(loginController.validatePass());
//        assertEquals(passwordLabel.getTextFill(), Color.GREEN);
//
//        clickOn(loginButton);
//
//        assertEquals(errorMessage.getText(), "Invalid Login Credentials!");
//
//    }

//    @Test
//    public void test_invalid_password_login() {
//
//        usernameField.setText("player123");
//        passwordField.setText("12");
//
//        assertFalse(usernameField.getText().isBlank());
//        assertTrue(loginController.validateUser());
//        assertEquals(usernameLabel.getTextFill(), Color.GREEN);
//
//        assertFalse(passwordField.getText().isBlank());
//        assertFalse(loginController.validatePass());
//        assertEquals(passwordLabel.getTextFill(), Color.RED);
//
//        clickOn(loginButton);
//
//        assertEquals(errorMessage.getText(), "Invalid Login Credentials!");
//
//    }

//    @Test
//    public void test_valid_login(){
//
//        usernameField.setText("Samuel");
//        passwordField.setText("password123");
//
//        assertFalse(usernameField.getText().isBlank());
//        assertTrue(loginController.validateUser());
//        assertEquals(usernameLabel.getTextFill(), Color.GREEN);
//
//        assertFalse(passwordField.getText().isBlank());
//        assertTrue(loginController.validatePass());
//        assertEquals(passwordLabel.getTextFill(), Color.GREEN);
//
//        clickOn(loginButton);
//
//
//        Label profileHomeUsername = (Label) lookup("#usernameField").queryAs(Label.class);
//        Scene profileHomeWindow = profileHomeUsername.getScene();
//        Scene loginWindow = loginButton.getScene();
//
//
//
//
//    }
//


}