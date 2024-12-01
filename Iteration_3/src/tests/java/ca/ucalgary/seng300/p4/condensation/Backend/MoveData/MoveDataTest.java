package ca.ucalgary.seng300.p4.condensation.Backend.MoveData;


import ca.ucalgary.seng300.p4.condensation.Backend.DTOs.Vector2;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

public class MoveDataTest {

    @Test(expected = Exception.class)
    public void testFromJSON_InvalidJSON() {
        String invalidJSON = "{ invalid json string }";

        MoveData.fromJSON(invalidJSON);
    }

    @Test(expected = Exception.class)
    public void testFromJSON_NullInput() {

        MoveData.fromJSON(null);
    }

    @Test(expected = Exception.class)
    public void testFromJSON_EmptyInput() {
        String emptyJSON = "";

        MoveData.fromJSON(emptyJSON);
    }

    @Test(expected = Exception.class)
    public void testFromJSON_MissingFields() {
        String missingFieldsJSON = "{ \"playerID\": \"123e4567-e89b-12d3-a456-426614174000\" }"; // Missing "newPosition"

        MoveData.fromJSON(missingFieldsJSON);
    }

    @Test(expected = Exception.class)
    public void testFromJSON_InvalidFieldTypes() {
        String invalidFieldTypesJSON = "{ \"playerID\": \"123e4567-e89b-12d3-a456-426614174000\", \"newPosition\": \"not an array\" }";

        MoveData.fromJSON(invalidFieldTypesJSON);
    }
}