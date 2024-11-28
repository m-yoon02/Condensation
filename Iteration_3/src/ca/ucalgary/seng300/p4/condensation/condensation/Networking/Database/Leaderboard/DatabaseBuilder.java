package ca.ucalgary.seng300.p4.condensation.condensation.Networking.Database.Leaderboard;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import java.util.UUID;

public class DatabaseBuilder {
    public static void main(String[] args) {
        try {
            Leaderboard leaderboard = new Leaderboard("/Users/mortezafaraji/Desktop/school/SENG300/seng300-f24-project/Iteration_3/src/ca/ucalgary/seng300/p4/condensation/condensation/Networking/Database/Leaderboard/Leaderboard.json");

            System.out.println(leaderboard.findUserByName("Michelle"));
            UUID id = UUID.fromString("345e6789-e89d-45d5-a678-627836485231");
            leaderboard.updateEloForLosingPlayer(id);
            leaderboard.refreshLeaderboard();


        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * A method that creates a user to use for the builder
     * @param name the name
     * @param elo the elo
     * @return the JSONObject
     */
    private static JSONObject createUser(String name, int elo) {
        JSONObject user = new JSONObject(); //create new object

        user.put("id", UUID.randomUUID().toString()); //assign random uuid
        user.put("name", name);
        user.put("elo", elo);

        return user;
    }
}
