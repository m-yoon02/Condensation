package ca.ucalgary.seng300.p4.condensation.Networking.Services;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ChatService {

    private final Map<String, String> responses = new HashMap<>();



    /**
     * Initializes the ChatService with predefined responses for certain chat messages.
     */
    public ChatService() {
        responses.put("hello", "hello there noob");
        responses.put("you suck at this game noob", "im only trying my best. No need to be rude");
        responses.put("how old are you?", "I dont talk to strangers");
        responses.put("why?", "cause i said so");
        responses.put("who's the weirdest dude you've been matched with", "ohh lol... some random dude named tz");
        responses.put("gg", "ggwp");
    }



    /**
     * Sends a chat message between two users and generates responses based on predefined rules.
     * @param username1 The username of the sender.
     * @param message1  The message sent by the sender.
     * @param username2 The username of the receiver.
     * @return A {@link JSONArray} containing the chat messages and server responses,
     *         or {@code null} if no predefined response matches the message.
     */
    public JSONArray sendChatMessage(String username1, String message1, String username2 ) {
        JSONArray chatMessages = new JSONArray();
        for (String key : responses.keySet()) {
            if (message1.equals(key)) {

                JSONObject clientResponse = new JSONObject();
                clientResponse.put("username", username1);
                clientResponse.put("date", LocalDateTime.now());
                clientResponse.put("message", key);


                JSONObject serverResponses = new JSONObject();
                serverResponses.put("username", username2);
                serverResponses.put("date", LocalDateTime.now());
                serverResponses.put("message", responses.get(key));

                chatMessages.add(clientResponse);
                chatMessages.add(serverResponses);
                return chatMessages;

            }
        }
        return null;
    }








}
