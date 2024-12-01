package ca.ucalgary.seng300.p4.condensation.Backend.MoveData;

import ca.ucalgary.seng300.p4.condensation.Backend.DTOs.Vector2;

import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

public class CheckersMoveDataTest {

    @Test
    public void testGetOldPosition() {
        UUID playerID = UUID.randomUUID();
        Vector2 movePosition = new Vector2(3, 4);
        Vector2 oldPosition = new Vector2(2, 3);
        CheckersMoveData data = new CheckersMoveData(playerID, movePosition, oldPosition);
        assertEquals(oldPosition, data.getOldPosition());
    }

    @Test(expected = NullPointerException.class)
    public void testFromJSON_NullJSON() {
        CheckersMoveData.fromJSON(null);
    }

    @Test(expected = Exception.class)
    public void testFromJSON_InvalidJSON() {
        String invalidJSON = "{ invalid json }";
        CheckersMoveData.fromJSON(invalidJSON);
    }

    @Test(expected = Exception.class)
    public void testFromJSON_MissingFields() {
        String json = "{}";
        CheckersMoveData.fromJSON(json);
    }

    @Test (expected = Exception.class)
    public void testFromJSON_IncorrectFields() {
        String json = "{\"player\":\"wrong\",\"position\":[1,2]}";
        try {
            CheckersMoveData.fromJSON(json);
            fail("Expected IllegalArgumentException");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testInheritanceFromMoveData() {
        UUID playerID = UUID.randomUUID();
        Vector2 movePosition = new Vector2(3, 4);
        Vector2 oldPosition = new Vector2(2, 3);
        CheckersMoveData data = new CheckersMoveData(playerID, movePosition, oldPosition);
        assertEquals(playerID, data.getPlayerID());
        assertEquals(movePosition, data.getMovePosition());
    }

    @Test
    public void testConstructor_NullOldPosition() {
        UUID playerID = UUID.randomUUID();
        Vector2 movePosition = new Vector2(3, 4);
        CheckersMoveData data = new CheckersMoveData(playerID, movePosition, null);
        assertEquals(playerID, data.getPlayerID());
        assertEquals(movePosition, data.getMovePosition());
        assertNull(data.getOldPosition());
    }

    @Test
    public void testFromJSON_ValidJSON_WithOldPosition() {
        UUID playerID = UUID.randomUUID();
        Vector2 movePosition = new Vector2(3, 4);
        Vector2 oldPosition = new Vector2(2, 3);
        String json = String.format("{\"PlayerID\":\"%s\",\"NewPosition\":[%d,%d],\"OldPosition\":[%d,%d]}",
                playerID, movePosition.x(), movePosition.y(), oldPosition.x(), oldPosition.y());
        CheckersMoveData data = CheckersMoveData.fromJSON(json);
        assertEquals(playerID, data.getPlayerID());
        assertEquals(movePosition, data.getMovePosition());
        assertEquals(oldPosition, data.getOldPosition());
    }
}