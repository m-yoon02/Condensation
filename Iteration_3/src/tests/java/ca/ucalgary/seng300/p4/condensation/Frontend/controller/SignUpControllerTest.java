package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import ca.ucalgary.seng300.p4.condensation.HelloApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import static org.junit.Assert.*;

public class SignUpControllerTest extends ApplicationTest {

    TextField usernameField;
    TextField passwordField;
    Button continueBtn;
    Label errorMessage;
    Label usernameLabel;
    Label passwordLabel;
    Button returnWelcomeButton;
    Pane profileHomePane;

    FXMLLoader fxmlLoader;
    Scene scene;

    SignUpController signUpController;


    @Override
    public void start(Stage stage) throws Exception {
        fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("SignUp.fxml"));
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
        signUpController = fxmlLoader.getController();
    }

    @Before
    public void setUp() throws Exception {
        usernameField = (TextField) lookup("#usernameField").queryAs(TextField.class);
        usernameLabel = (Label) lookup("#usernameLabel").queryAs(Label.class);
        passwordField = (TextField) lookup("#passwordField").queryAs(TextField.class);
        passwordLabel = (Label) lookup("#passwordLabel").queryAs(Label.class);
        continueBtn = (Button) lookup("#continueBtn").queryAs(Button.class);
        errorMessage = (Label) lookup("#errorMessage").queryAs(Label.class);
        returnWelcomeButton = (Button) lookup("#returnWelcome").queryAs(Button.class);

    }

    @After
    public void tearDown() throws Exception {
        usernameField = null;
        usernameLabel = null;
        passwordField = null;
        passwordLabel = null;
        continueBtn = null;
        errorMessage = null;
        returnWelcomeButton = null;
    }

    @Test
    public void check_empty_username() {

        usernameField.setText("");

        assertTrue(usernameField.getText().isBlank());
        assertFalse(signUpController.validateUser());
        assertEquals(usernameLabel.getTextFill(), Color.RED);

    }

    @Test
    public void check_username_characters() {

        usernameField.setText("Hi");

        assertFalse(usernameField.getText().isBlank());
        assertFalse(signUpController.validateUser());
        assertEquals(usernameLabel.getTextFill(), Color.RED);

    }

    @Test
    public void check_valid_username() {

        usernameField.setText("player123");

        assertFalse(usernameField.getText().isBlank());
        assertTrue(signUpController.validateUser());
        assertEquals(usernameLabel.getTextFill(), Color.GREEN);
    }

    @Test
    public void check_empty_password() {

        passwordField.setText("");

        assertTrue(passwordField.getText().isBlank());
        assertFalse(signUpController.validatePass());
        assertEquals(passwordLabel.getTextFill(), Color.RED);

    }

    @Test
    public void check_password_characters() {

        passwordField.setText("Hi");

        assertFalse(passwordField.getText().isBlank());
        assertFalse(signUpController.validatePass());
        assertEquals(passwordLabel.getTextFill(), Color.RED);

    }

    @Test
    public void check_valid_password() {

        passwordField.setText("player123");

        assertFalse(passwordField.getText().isBlank());
        assertTrue(signUpController.validatePass());
        assertEquals(passwordLabel.getTextFill(), Color.GREEN);
    }

    @Test
    public void test_invalid_username_signup() {

        usernameField.setText("Hi");
        passwordField.setText("123456");

        assertFalse(usernameField.getText().isBlank());
        assertFalse(signUpController.validateUser());
        assertEquals(usernameLabel.getTextFill(), Color.RED);

        assertFalse(passwordField.getText().isBlank());
        assertTrue(signUpController.validatePass());
        assertEquals(passwordLabel.getTextFill(), Color.GREEN);

        clickOn(continueBtn);

        assertEquals(errorMessage.getText(), "Invalid Sign Up Credentials!");

    }

    @Test
    public void test_invalid_password_signup() {

        usernameField.setText("player123");
        passwordField.setText("12");

        assertFalse(usernameField.getText().isBlank());
        assertTrue(signUpController.validateUser());
        assertEquals(usernameLabel.getTextFill(), Color.GREEN);

        assertFalse(passwordField.getText().isBlank());
        assertFalse(signUpController.validatePass());
        assertEquals(passwordLabel.getTextFill(), Color.RED);

        clickOn(continueBtn);

        assertEquals(errorMessage.getText(), "Invalid Sign Up Credentials!");

    }

    @Test
    public void test_valid_signup(){

        usernameField.setText("Samuel");
        passwordField.setText("password123");

        assertFalse(usernameField.getText().isBlank());
        assertTrue(signUpController.validateUser());
        assertEquals(usernameLabel.getTextFill(), Color.GREEN);

        assertFalse(passwordField.getText().isBlank());
        assertTrue(signUpController.validatePass());
        assertEquals(passwordLabel.getTextFill(), Color.GREEN);

        clickOn(continueBtn);

        Label loginUsernameLabel = (Label) lookup("#usernameLabel").queryAs(Label.class);
        Scene loginScene = loginUsernameLabel.getScene();

        Scene signupScene = continueBtn.getScene();

        assertFalse(loginScene.equals(signupScene));

    }

    @Test
    public void check_return_to_welcome() {

        clickOn(returnWelcomeButton);

        MenuBar welcomeMenuBar = (MenuBar) lookup("#menuBar").queryAs(MenuBar.class);

        Scene welcomeScene = welcomeMenuBar.getScene();
        Scene signup = continueBtn.getScene();

        assertFalse(signup.equals(welcomeScene));


    }


}