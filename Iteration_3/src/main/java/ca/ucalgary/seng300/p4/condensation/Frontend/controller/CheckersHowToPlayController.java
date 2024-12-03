package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import ca.ucalgary.seng300.p4.condensation.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CheckersHowToPlayController {

    private Stage stage;
    @FXML
    public void onHowToPlayBackBtnClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("CheckersHomeMenu.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//        CheckersHomeMenuController controller = new CheckersHomeMenuController(stage);
//        fxmlLoader.setController(controller);
        Scene scene = new Scene(fxmlLoader.load(), 700, 500);
        stage.setTitle("Checkers Home Menu");
        stage.setScene(scene);
        stage.setMinWidth(700);
        stage.setMinHeight(530);
        stage.show();
    }
}
