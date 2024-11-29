package ca.ucalgary.seng300.p4.condensation.Networking.Services;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ChatService {

    private final Map<String, String> responses = new HashMap<>();



    public ChatService() {
        responses.put("hello", "hello");
        responses.put("you suck at this game noob", "im only trying my best. No need to be rude");
        responses.put("how old are you?", "I dont talk to strangers");
        responses.put("why?", "cause i said so");
        responses.put("gg", "ggwp");
    }



    public JSONArray sendChatMessage(String username1, String message1, String username2 ) {
        JSONArray chatMessages = new JSONArray();
        for (String key : responses.keySet()) {
            if (message1.equals(key)) {

                JSONObject clientResponse = new JSONObject();
                clientResponse.put("username", username1);
                clientResponse.put("date", LocalDateTime.now());
                clientResponse.put("message", key);


                JSONObject serverResponses = new JSONObject();
                serverResponses.put("username", "WorldD3story3r80664");
                serverResponses.put("date", LocalDateTime.now());
                serverResponses.put("message", key);

                chatMessages.add(clientResponse);
                chatMessages.add(serverResponses);
                return chatMessages;

            }
        }
        return null;
    }








}
