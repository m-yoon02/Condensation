package ca.ucalgary.seng300.p4.condensation.Backend.MoveData;

import java.util.UUID;

import org.junit.Test;

import static org.junit.Assert.*;

public class JSONParseResultTest {

    @Test
    public void testParseJSON_ValidJSON_WithOldPosition() {
        UUID playerID = UUID.randomUUID();
        int[] newPosition = {3, 4};
        int[] oldPosition = {2, 3};
        String json = String.format("{\"PlayerID\":\"%s\",\"NewPosition\":[%d,%d],\"OldPosition\":[%d,%d]}",
                playerID.toString(), newPosition[0], newPosition[1], oldPosition[0], oldPosition[1]);
        JSONParseResult result = JSONParseResult.parseJSON(json);
        assertEquals(playerID, result.playerID());
        assertArrayEquals(newPosition, result.newPosition());
        assertArrayEquals(oldPosition, result.oldPosition());
    }

    @Test
    public void testParseJSON_ValidJSON_NullOldPosition() {
        UUID playerID = UUID.randomUUID();
        int[] newPosition = {3, 4};
        String json = String.format("{\"PlayerID\":\"%s\",\"NewPosition\":[%d,%d]}",
                playerID.toString(), newPosition[0], newPosition[1]);
        JSONParseResult result = JSONParseResult.parseJSON(json);
        assertEquals(playerID, result.playerID());
        assertArrayEquals(newPosition, result.newPosition());
        assertNull(result.oldPosition());
    }

    @Test(expected = RuntimeException.class)
    public void testParseJSON_InvalidJSON() {
        String invalidJSON = "{ invalid json }";
        JSONParseResult.parseJSON(invalidJSON);
    }

    @Test(expected = NullPointerException.class)
    public void testParseJSON_NullJSON() {
        JSONParseResult.parseJSON(null);
    }

    @Test(expected = RuntimeException.class)
    public void testParseJSON_MissingFields() {
        String json = "{}";
        JSONParseResult.parseJSON(json);
    }

    @Test
    public void testParseJSON_IncorrectFields() {
        String json = "{\"player\":\"wrong\",\"position\":[1,2]}";
        try {
            JSONParseResult.parseJSON(json);
            fail("Expected RuntimeException");
        } catch (RuntimeException e) {
            // Expected exception
        }
    }

    @Test
    public void testParseJSON_InvalidUUID() {
        String json = "{\"PlayerID\":\"invalid-uuid\",\"NewPosition\":[5,6]}";
        try {
            JSONParseResult.parseJSON(json);
            fail("Expected RuntimeException");
        } catch (RuntimeException e) {
            // Expected exception
        }
    }

    @Test
    public void testParseJSON_NonNumericPosition() {
        String json = "{\"PlayerID\":\"00000000-0000-0000-0000-000000000000\",\"NewPosition\":[\"a\",6]}";
        try {
            JSONParseResult.parseJSON(json);
            fail("Expected RuntimeException");
        } catch (RuntimeException e) {
            // Expected exception
        }
    }
}