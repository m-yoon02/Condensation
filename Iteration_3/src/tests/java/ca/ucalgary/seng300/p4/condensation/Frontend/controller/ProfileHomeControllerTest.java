package ca.ucalgary.seng300.p4.condensation.Frontend.controller;
import ca.ucalgary.seng300.p4.condensation.HelloApplication;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import java.io.IOException;

import static org.junit.Assert.*;
public class ProfileHomeControllerTest extends ApplicationTest {
    
    private TableView<JSONObject> tableView;
    private TableColumn<JSONObject, String> gameColumn;
    private TableColumn<JSONObject, String> outcomeColumn;
    private TableColumn<JSONObject, Number> currentEloColumn;
    private TableColumn<JSONObject, Number> totalWinsColumn;
    private TableColumn<JSONObject, Number> gamesPlayedColumn;
    private TextField profileTitle;
    private Label usernameField;
    private Button logoutButton;
    private Button leaderboardButton;
    private Button playerProfilesButton;
    private Button gamesButton;
    FXMLLoader fxmlLoader;
    Scene scene;
    ProfileHomeController profileHomeController;
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
        usernameField = (Label)lookup("#usernameField").queryAs((Label.class));
        profileTitle = (TextField)lookup("#profileTitle").queryAs((TextField.class));
        logoutButton = (Button) lookup("#logoutButton").queryAs((Button.class));
        leaderboardButton = (Button) lookup("#leaderboardButton").queryAs((Button.class));
        playerProfilesButton = (Button) lookup("#playerProfilesButton").queryAs((Button.class));
        gamesButton = (Button) lookup("#gamesButton").queryAs((Button.class));

      //  tableView = (TableView<JSONObject>)
       // gameColumn.setCellValueFactory();

    }

    @After
    public void tearDown(){
        usernameField = null;
        profileTitle = null;
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




}
