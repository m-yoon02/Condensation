package ca.ucalgary.seng300.p4.condensation.Networking.Database.Leaderboard;

import ca.ucalgary.seng300.p4.condensation.Networking.Enums.GameTypes;
import ca.ucalgary.seng300.p4.condensation.Networking.Managers.NetworkManager;
import org.assertj.core.internal.bytebuddy.implementation.bind.MethodNameEqualityResolver;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.*;

        import java.io.*;
        import java.util.UUID;

import static org.junit.Assert.*;

public class LeaderboardTest {

    private NetworkManager networkManager;
    private Leaderboard leaderboard;
    private static final String MOCK_LEADERBOARD_FILE = "mockLeaderboard.json";

    @Before
    public void setUp() throws Exception {
        // Create a mock JSON file to simulate leaderboard data
        JSONArray mockData = new JSONArray();

        // Add sample user data
        JSONObject user1 = new JSONObject();
        user1.put("playerUUID", UUID.randomUUID().toString());
        user1.put("username", "player1");
        user1.put("password", "password1");
        user1.put("checkersElo", 1200);
        user1.put("recentMatches", new JSONArray());
        mockData.add(user1);

        JSONObject user2 = new JSONObject();
        user2.put("playerUUID", UUID.randomUUID().toString());
        user2.put("username", "player2");
        user2.put("password", "password2");
        user2.put("checkersElo", 1300);
        user2.put("recentMatches", new JSONArray());
        mockData.add(user2);

        try (FileWriter writer = new FileWriter(MOCK_LEADERBOARD_FILE)) {
            writer.write(mockData.toJSONString());
        }

        // Initialize the leaderboard with the mock file
        networkManager.LEADERBOARD_FILE = MOCK_LEADERBOARD_FILE;
        leaderboard = Leaderboard.getInstance();
    }

    @After
    public void tearDown() {
        // Clean up the mock file after tests
        File file = new File(MOCK_LEADERBOARD_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    public void testLoadData() throws IOException, ParseException {
        leaderboard.loadData();
        JSONArray users = leaderboard.getAllUsers();
        assertEquals(2, users.size());
    }

    @Test
    public void testCreateUser() throws IOException {
        leaderboard.createUser("player3", "password3");
        JSONArray users = leaderboard.getAllUsers();
        assertEquals(3, users.size());
    }

    @Test
    public void testFindUserByName() {
        JSONObject user = leaderboard.findUserByName("player1");
        assertNotNull(user);
        assertEquals("player1", user.get("username"));
    }

    @Test
    public void testUpdateForTieGame() {
        UUID player1ID = UUID.randomUUID();
        UUID player2ID = UUID.randomUUID();
        UUID sessionID = UUID.randomUUID();

        leaderboard.updateForTieGame(player1ID, player2ID, sessionID, GameTypes.CHECKERS);

        JSONObject player1 = leaderboard.findUserById(player1ID);
        JSONObject player2 = leaderboard.findUserById(player2ID);

        assertNotNull(player1);
        assertNotNull(player2);

        JSONArray recentMatches1 = (JSONArray) player1.get("recentMatches");
        JSONArray recentMatches2 = (JSONArray) player2.get("recentMatches");

        assertEquals(1, recentMatches1.size());
        assertEquals(1, recentMatches2.size());
    }
}