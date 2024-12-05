package ca.ucalgary.seng300.p4.condensation.Frontend.controller;
import ca.ucalgary.seng300.p4.condensation.HelloApplication;
import ca.ucalgary.seng300.p4.condensation.Networking.Database.Leaderboard.Leaderboard;
import ca.ucalgary.seng300.p4.condensation.Networking.Managers.NetworkManager;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit.ApplicationTest;
import java.io.IOException;
import java.util.UUID;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static javax.management.Query.times;
import static org.junit.Assert.*;



public class ProfileHomeControllerTest extends ApplicationTest {

    // FXML-bound UI elements
    @FXML
    private TableView<JSONObject> playerTableView;
    @FXML
    private TableColumn<JSONObject, String> gameColumn;
    @FXML
    private TableColumn<JSONObject, String> currentEloColumn;
    @FXML
    private TableColumn<JSONObject, String> totalWinsColumn;
    @FXML
    private TableColumn<JSONObject, String> statusColumn;

    @FXML
    private TableView<JSONObject> playerRecentTableView;
    @FXML
    private TableColumn<JSONObject, String> playerRecentgameColumn;
    @FXML
    private TableColumn<JSONObject, String> playerRecentoutcomeColumn;
    @FXML
    private TableColumn<JSONObject, String> playerRecentcurrentEloColumn;
    @FXML
    private TableColumn<JSONObject, String> playerRecenttotalWinsColumn;
    @FXML
    private TableColumn<JSONObject, String> playerRecentgamesPlayedColumn;

    @FXML
    private Label usernameField;
    @FXML
    private Button leaderboardButton;
    @FXML
    private Button gamesButton;
    @FXML
    private Button playerProfilesButton;
    @FXML
    private Button logoutButton;

    private Leaderboard leaderboard;
    FXMLLoader fxmlLoader;
    Scene scene;
    ProfileHomeController profileHomeController;

    private ByteArrayOutputStream outputStream;
    @Override
    public void start(Stage stage) throws IOException {
        fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("ProfileHome.fxml"));
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
        profileHomeController = fxmlLoader.getController();
    }

    @Before
    public void setUp() throws Exception {
        // Redirect System.out to capture print statements
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));



        usernameField = (Label)lookup("#usernameField").queryAs((Label.class));
        logoutButton = (Button) lookup("#logoutButton").queryAs((Button.class));
        leaderboardButton = (Button) lookup("#leaderboardButton").queryAs((Button.class));
        playerProfilesButton = (Button) lookup("#playerProfilesButton").queryAs((Button.class));
        gamesButton = (Button) lookup("#gamesButton").queryAs((Button.class));



    }

    @After
    public void tearDown(){
        usernameField = null;
        logoutButton = null;
        leaderboardButton = null;
        playerProfilesButton = null;
        gamesButton = null;

    }

    @Test
    public void initializeTest(){
        System.out.println(gameColumn.getColumns().get(0).toString());

        // on action do button.fire()
        // on mouse clicked use clickedOn()
    }

    @Test
    public void emptyLeaderBoardTest(){
        leaderboard = null;

        //test if user is null
        //make playerUIDD nul

        // on action do button.fire()
        // on mouse clicked use clickedOn()
    }
    @Test
    public void displayPlayerStatsTest(){
        System.out.println(gameColumn.getColumns().get(0).toString());

        //test if user is null
        //make playerUIDD null
        UUID id = null;



        // on action do button.fire()
        // on mouse clicked use clickedOn()
    }

    @Test
    public void createGameStatsTest(){
        System.out.println(gameColumn.getColumns().get(0).toString());

        // on action do button.fire()
        // on mouse clicked use clickedOn()
    }

    @Test
    public void displayRecentMatchesTest(){
        System.out.println(gameColumn.getColumns().get(0).toString());

        // on action do button.fire()
        // on mouse clicked use clickedOn()
    }

    /**
     * Finished this test
     */
    @Test
    public void onLeaderboardButtonClickedTest(){
        FxRobot robot = new FxRobot();

        // Simulate clicking the leaderboard button
        robot.clickOn("#leaderboardButton");

        // Get the current scene
        Scene tMenuScene = leaderboardButton.getScene();

        // Find the button on the new scene (check the corresponding fxml file)
        Button homeButton = lookup("#homeButton").queryAs(Button.class);

        // Retrieve the scene from the new element
        Scene leaderBoardScene = homeButton.getScene();

        // The starting scene should not be equal to the new scene
        assertFalse(tMenuScene.equals(leaderBoardScene));
    }

    /**
     * Finished this test
     */
    @Test
    public void onGamesButtonClickedTest(){
        FxRobot robot = new FxRobot();

        // Simulate clicking the leaderboard button
        robot.clickOn("#gamesButton");

        // Get the current scene
        Scene tMenuScene = gamesButton.getScene();

        // Find the button on the new scene (check the corresponding fxml file)
        Button homeButton = lookup("#homeButton").queryAs(Button.class);

        // Retrieve the scene from the new element
        Scene profileGameScene = homeButton.getScene();

        // The starting scene should not be equal to the new scene
        assertFalse(tMenuScene.equals(profileGameScene));
    }

    @Test
    public void onPlayerProfilesButtonClickedTest(){
        System.out.println(gameColumn.getColumns().get(0).toString());

        // on action do button.fire()
        // on mouse clicked use clickedOn()
    }

    @Test
    public void onlogOutButtonClickedTest() throws IOException, ParseException {
        FxRobot robot = new FxRobot();
        robot.clickOn("#logoutButton");

        String output = outputStream.toString().trim();
        assertTrue("The message 'Logged out' was not printed.", output.contains("Logged out"));

        // Verify the logout handler was called

    }

    @Test
    public void navigateToScreenTest(){
        //dont know
    }



}
