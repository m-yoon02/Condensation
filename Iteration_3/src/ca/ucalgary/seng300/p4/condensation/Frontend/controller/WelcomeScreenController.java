package ca.ucalgary.seng300.p4.condensation.Frontend.controller;


import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class WelcomeScreenController {

    @FXML
    private Button loginBtn;

    @FXML
    private Button signupBtn;

    @FXML
    public void initialize() {
        loginBtn.setOnAction(
                e -> System.out.println("Login Button is working")
        );
        signupBtn.setOnAction(
                e -> System.out.println("Signup Button is  working")
        );
    }



}
