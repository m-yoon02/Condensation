package ca.ucalgary.seng300.p4.condensation.condensation.Networking.Managers;

import ca.ucalgary.seng300.p4.condensation.condensation.Networking.DTO.QueuedPlayerMemo;
import ca.ucalgary.seng300.p4.condensation.condensation.Networking.Enums.GameTypes;
import org.json.simple.JSONObject;

import java.util.UUID;

public class NetworkTest {

    // Main method
    public static void main(String[] args) {
        NetworkManager networkManager = NetworkManager.getNetworkInstance();

        JSONObject loginPayload  = new JSONObject();
        networkManager.playerLoginHandler(loginPayload);


        // yeah so when frontEND elo, PlayerID, gameType
        // need a fetch elo method from backend
        QueuedPlayerMemo queuedPlayerMemo = new QueuedPlayerMemo(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), GameTypes.CHECKERS, 100);
        networkManager.clientQueuePlayer(queuedPlayerMemo);

        // we need to hardcode a matchmaking pool




    }
}
