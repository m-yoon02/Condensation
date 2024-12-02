package ca.ucalgary.seng300.p4.condensation.Frontend.controller;
import ca.ucalgary.seng300.p4.condensation.HelloApplication;

import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

public class LoginControllerTest extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        new HelloApplication().start(stage);
    }

    @Test
    public void testLogin() {
        Button button = (Button) lookup("#loginBtn").queryAs(Button.class);

        assert button.getText().equals("LOGIN");

    }
}