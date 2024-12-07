package ca.ucalgary.seng300.p4.condensation.Frontend.Receiver;

import ca.ucalgary.seng300.p4.condensation.Networking.Enums.GameTypes;
import ca.ucalgary.seng300.p4.condensation.Networking.Managers.NetworkManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class ClientReceiver {

    private static final ArrayList<RecieverListener> receiverListeners = new ArrayList<>();

    public static void addClientReceiver(RecieverListener recieverListener){
        receiverListeners.add(recieverListener);
    }

    public static void receive(UUID clientID, GameTypes gameType, JSONObject payload) throws IOException {
        if (!clientID.equals(getCurrentClientPlayerID()))
            return;
        if (payload.get("Changes") != null){
            try {
                JSONArray jsonArray = (JSONArray) new JSONParser().parse((String) payload.get("Changes"));
                payload.put("Changes", jsonArray);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        for (RecieverListener listener : receiverListeners) {
            listener.receive(gameType, payload);
        }
    }

    private static UUID getCurrentClientPlayerID() {
        try (FileReader fileReader = new FileReader(NetworkManager.PLAYER_ID_FILE)) {
            System.out.println("PlayerID_File: " + NetworkManager.PLAYER_ID_FILE);
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(fileReader);
            System.out.println("JSON done" + jsonObject.toJSONString());
            return UUID.fromString((String) jsonObject.get("playerID"));
        } catch (IOException | ParseException e){
            throw new RuntimeException(e);
        }
    }

}
