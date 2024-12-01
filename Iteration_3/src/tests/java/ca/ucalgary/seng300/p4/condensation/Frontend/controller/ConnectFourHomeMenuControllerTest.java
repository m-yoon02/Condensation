package java.ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import ca.ucalgary.seng300.p4.condensation.Frontend.controller.ConnectFourHomeMenuController;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.File;


public class ConnectFourHomeMenuControllerTest {
    private ConnectFourHomeMenuController controller;

    @Before
    public void setUp() throws Exception {
        // create a new controller
        controller = new ConnectFourHomeMenuController();

    }

    @After
    public void tearDown() throws Exception {
        controller = null;
    }

    // issue with packages
    @Test
    public void testLoad_withInvalidTeam() throws Exception {
        // create invalid file
        File invalidFile = new File("src/test/resources/invalidTeam.json");
        // load invalid file
        ConnectFourHomeMenuController.Team team = ConnectFourHomeMenuController.load(invalidFile);
        // team should be null
        Assert.assertNull(team);

    }
}