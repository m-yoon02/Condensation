package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import ca.ucalgary.seng300.p4.condensation.HelloApplication;
import javafx.application.Platform;
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
import org.junit.jupiter.api.Order;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

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

    @Override
    public void start(Stage stage) throws IOException {
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
    @Order(1)
    public void click_on_signup(){

        clickOn(signUpButton);

        Scene welcomeScene = loginButton.getScene();

        Label signupMessage = (Label) lookup("#signupMessage").queryAs(Label.class);
        Scene signupScene = signupMessage.getScene();

        assertFalse(welcomeScene.equals(signupScene));
        assertEquals(signupMessage.getText(), "CREATE A NEW ACCOUNT");

    }

    @Test
    @Order(2)
    public void click_on_login(){

        clickOn(loginButton);

        Scene welcomeScene = loginButton.getScene();

        Label loginMessage = (Label) lookup("#welcomeMessage").queryAs(Label.class);
        Scene loginScene = loginMessage.getScene();

        assertFalse(welcomeScene.equals(loginScene));
        assertEquals(loginMessage.getText(), "Welcome Back");

    }

    @Test
    @Order(3)
    public void click_on_team(){

        clickOn("#teamMenu").clickOn("#team");

    }

    @Test
    @Order(4)
    public void click_on_about(){

        clickOn("#aboutMenu").clickOn("#about");

    }

    @Test
    @Order(5)
    public void click_on_prog(){

        clickOn("#progMenu").clickOn("#prog");

    }




}