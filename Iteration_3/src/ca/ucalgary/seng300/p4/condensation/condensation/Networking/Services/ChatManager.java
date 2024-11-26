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

    /**
     * Creates a new chat session and stores them in a HashMap as a key value pair of sessionIDs and Messages respectively
     * @param sessionID -- some unique identifier for the game session
     */
    public void createChatSessions(String sessionID) {
        if(!chatSessions.containsKey(sessionID)){                                       // checks if the messages for a particular sessionID already exists in the HashMap or not.
            chatSessions.put(sessionID,new ArrayList<>());                              // adds new chatSessions into the HashMap as per the sessionIDs where sessionID is the key and the ArrayList containing the messages is the value
            System.out.println("Chat Session created for the session: " + sessionID);   
        }
        else{
            System.out.println("Chat Session already exists. Hence, no new session is created");
        }

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



    // if we want to pursue with more chat options here are some ideas
    // - retrieve chat history
    // - filtering - not likely but a thing to keep in mind
    // - add some sort of game specific chat features? such as [system]: It's Player 1's turn
    // - move notifications? seems more like backend stuff




}
