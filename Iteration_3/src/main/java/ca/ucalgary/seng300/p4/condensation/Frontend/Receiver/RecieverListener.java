package ca.ucalgary.seng300.p4.condensation.Frontend.Receiver;

import ca.ucalgary.seng300.p4.condensation.Networking.Enums.GameTypes;
import org.json.simple.JSONObject;

import java.io.IOException;

public abstract class RecieverListener {
    private final GameTypes gameType;

    public RecieverListener(GameTypes gameType) {
        this.gameType = gameType;
    }

    public void receive(GameTypes gameTypeTo, JSONObject payload) throws IOException {
        if (gameType == gameTypeTo) {
            onReceive(payload);
        }
    }

    public abstract void onReceive(JSONObject payload) throws IOException;
}
