package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import ca.ucalgary.seng300.p4.condensation.Frontend.controller.ConnectFourHomeMenuController;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import static org.junit.Assert.*;


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