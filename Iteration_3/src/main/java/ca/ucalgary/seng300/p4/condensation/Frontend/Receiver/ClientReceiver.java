package ca.ucalgary.seng300.p4.condensation.Frontend.Receiver;

import ca.ucalgary.seng300.p4.condensation.Networking.Enums.GameTypes;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class ClientReceiver {

    private static final ArrayList<RecieverListener> receiverListeners = new ArrayList<>();

    public static void addClientReceiver(RecieverListener recieverListener){
        receiverListeners.add(recieverListener);
    }

    public static void receive(GameTypes gameType, JSONObject payload) throws IOException {
        for (RecieverListener listener : receiverListeners) {
            listener.receive(gameType, payload);
        }
    }

}
