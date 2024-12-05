package ca.ucalgary.seng300.p4.condensation;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("WelcomeScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 500);

        Image appIcon = new Image(getClass().getResourceAsStream("logo.png"));
        stage.getIcons().add(appIcon);

        stage.setResizable(false);
        stage.setTitle("Condensation Application");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        if (args.length == 2) {
            // represents input file
            System.out.println("Usage: java HelloApplication <file_path> <in_or_out>");
            String filePath = args[0];
            boolean toJson = Boolean.parseBoolean(args[1]);

            if (toJson) {
                // Convert text file to JSON
                try {
                    String content = new String(Files.readAllBytes(Paths.get(filePath))); // Read the file content
                    JSONObject jsonObject = new JSONObject();
                    // TODO: Put the json in leaderboard file - format the text file into json object so tthat can be written to

                    // Write JSON to the output file
                    try (FileWriter fileWriter = new FileWriter(Paths.get("ca/ucalgary/seng300/p4/condensation/Networking/Database/Leaderboard/Leaderboard.json").toFile())) {
                        fileWriter.write(jsonObject.toJSONString());
                        System.out.println("Leaderboard JSON file updated at");
                    }
                } catch (IOException e) {
                    System.err.println("Error reading or writing file: " + e.getMessage());
                }
            } else {
                // Convert JSON file to text to ret
                try {
                    JSONParser parser = new JSONParser();
                    JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("ca/ucalgary/seng300/p4/condensation/Networking/Database/Leaderboard/Leaderboard.json"));

                    // TODO: pass in the json


                    // Write text to the output file
                } catch (IOException | ParseException e) {
                    System.err.println("Error reading or parsing JSON: " + e.getMessage());
                }
            }
        }

        launch();
    }
}