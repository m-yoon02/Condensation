package ca.ucalgary.seng300.p4.condensation.Networking.Services;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;



public class ChatServiceTest {
    private ChatService chatService;



    @Before
    public void setUp() throws Exception {
        // initialize ChatService
        chatService = new ChatService();
    }



    @Test
    public void testSendChatMessageWithResponses() throws Exception {
        String username1 = "nathan";
        String username2 = "richard";
        String message = "you suck at this game noob";

        JSONArray result = chatService.sendChatMessage(username1,message,username2);

        // want to verify that the message being sent is not null
        assertNotNull("The result should not be null for a message", result);

        // also verify if the size of result matches with responses.keySet in ChatService
        assertEquals("The result should contain two messages", 2, result.size());

        // verify the client message
        JSONObject clientMessage = (JSONObject) result.get(0);
        assertEquals("The client username should match", username1, clientMessage.get("username"));
        assertEquals("The client message should match", message, clientMessage.get("message"));


        // verify the server response
        JSONObject serverMessage = (JSONObject) result.get(1);
        assertEquals("The server username should match", username2, serverMessage.get("username"));
        assertEquals("The server response message should match", "im only trying my best. No need to be rude", serverMessage.get("message"));
    }


    @Test
    public void testSendChatMessageWithEmptyMessage() throws Exception {
        String username1 = "nathan";
        String username2 = "richard";
        String message = "";

        JSONArray result = chatService.sendChatMessage(username1,message,username2);

        // want to verify that the message being sent is null since it is an empty message
        assertNull("The result should be null for an empty message message", result);


    }



}
