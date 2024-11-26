package ca.ucalgary.seng300.p4.condensation.condensation.Networking.Services;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class ChatManager {

    // have a hashmap to store the chat sessions with the sessionID as key and list of messages as value
    // something like this
    private final HashMap<String, List<String>> chatSessions;

    public ChatManager() {
        chatSessions = new HashMap<>(); // for later use that we can store the chats, and if we want to create a method to retrieve chat history or something along the lines of that
    }

    // ANI THIS PART IS FOR YOU

    /**
     * Creates a new chat session
     * @param sessionID -- some unique identifier for the game session
     */
    public void createChatSessions(String sessionID) {

    }


    /**
     * Sends a message in a specific chat session
     * @param sessionID the game session ID
     * @param playerUUID    the UUID of the player sending the message
     * @param message   the message content
     */
    public void sendMessage(String sessionID, String playerUUID, String message) {

        // checking to see if the chatSession contains the sessionID key that matches with the players
        if (chatSessions.containsKey(sessionID)) {
            String formattedMessage = "[" + playerUUID + "]: " + message; // for ex: [nathanphan22]: "hello!"
            chatSessions.get(sessionID).add(formattedMessage); // gets the sessionID number so that the formatted message is associated with that game session

        }
    }




}
