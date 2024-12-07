# Condensation

SENG 300 F24 P-4

Online Multiplayer Board Game Platform built with JavaFX for SENG 300 in
the Fall 2024 semester.

## Running

Make sure you have terminal open in the directory where condensation.jar is stored.
A /data directory must exist in this directory and contain the following files:

- Leaderboard.json
- playerID.json

Run the command:

```bash
java --module-path <path-to-javafx-sdk>/lib --add-modules javafx.controls,javafx.fxml -jar condensation.jar
```

The JavaFX sdk may be packaged with condensation.jar

In this case **\<path-to-javafx-sdk>/lib** may be replaced with **javafx-sdk-23.0.1/lib**
