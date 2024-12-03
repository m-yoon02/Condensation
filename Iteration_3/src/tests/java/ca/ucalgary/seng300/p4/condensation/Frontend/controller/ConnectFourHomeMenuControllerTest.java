package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import ca.ucalgary.seng300.p4.condensation.Frontend.controller.ConnectFourHomeMenuController;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import static org.junit.Assert.*;
// imports for javafx
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.isNotNull;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
//import static org.testfx.matcher.control.ButtonMatchers.hasText;



import java.io.File;


public class ConnectFourHomeMenuControllerTest extends ApplicationTest{
    private ConnectFourHomeMenuController controller;

    @Before
    public void setUp() throws Exception {
        // need to figure out set up
        controller = new ConnectFourHomeMenuController();
        // Load the FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ca/ucalgary/seng300/p4/condensation/checkersHomeMenu.fxml"));
//        StackPane root = loader.load();
//        controller = loader.getController();
//
//        // Prepare the scene and stage
//        Scene scene = new Scene(root, 700, 500);
//        stage.setScene(scene);
//        stage.show();
    }

    @After
    public void tearDown() throws Exception {
        controller = null;
    }

    // issue with packages
    @Test
    public void testButtonsAreVisible(){

    }
}