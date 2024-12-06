package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import ca.ucalgary.seng300.p4.condensation.HelloApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;


public class WelcomeScreenControllerTest extends ApplicationTest {

    FXMLLoader fxmlLoader;
    Scene scene;
    WelcomeScreenController welcomeScreenController;

    MenuBar menuBar;
    Menu menu;
    MenuItem teamItem;
    MenuItem aboutItem;
    MenuItem progItem;
    Button signUpButton;
    Button loginButton;
    HelloApplication application;

    @Override
    public void start(Stage stage) throws IOException {
        application = new HelloApplication();
        application.start(stage);
        fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("WelcomeScreen.fxml"));
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
        welcomeScreenController = fxmlLoader.getController();
    }

    @Before
    public void setUp() throws Exception {

        menuBar = (MenuBar) lookup("#menuBar").queryAs(MenuBar.class);
        teamItem = menuBar.getMenus().get(0);
        aboutItem = menuBar.getMenus().get(1);
        progItem = menuBar.getMenus().get(2);
        signUpButton = (Button) lookup("#signupButton").queryAs(Button.class);
        loginButton = (Button) lookup("#loginButton").queryAs(Button.class);

    }

    @After
    public void tearDown() throws Exception {


        menuBar = null;
        teamItem = null;
        aboutItem = null;
        progItem = null;
        signUpButton = null;
        loginButton = null;

    }

    @Test
    public void click_on_signup(){

        clickOn(signUpButton);

        Scene welcomeScene = loginButton.getScene();

        Label signupMessage = (Label) lookup("#signupMessage").queryAs(Label.class);
        Scene signupScene = signupMessage.getScene();

        assertFalse(welcomeScene.equals(signupScene));
        assertEquals(signupMessage.getText(), "CREATE A NEW ACCOUNT");

    }

    @Test
    public void click_on_login(){

        clickOn(loginButton);

        Scene welcomeScene = loginButton.getScene();

        Label loginMessage = (Label) lookup("#welcomeMessage").queryAs(Label.class);
        Scene loginScene = loginMessage.getScene();

        assertFalse(welcomeScene.equals(loginScene));
        assertEquals(loginMessage.getText(), "Welcome Back");

    }

    @Test
    public void click_on_team(){

        clickOn("#teamMenu").clickOn("#team");

    }

    @Test
    public void click_on_about(){

        clickOn("#aboutMenu").clickOn("#about");

    }

    @Test
    public void click_on_prog(){

        clickOn("#progMenu").clickOn("#prog");

    }




}