package test.java.ca.ucalgary.seng300.p4.condensation.condensation.Networking.Database.Leaderboard;

import ca.ucalgary.seng300.p4.condensation.Frontend.leaderboard.Leaderboard;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;

import java.io.*;


import static org.junit.Assert.*;
public class LeaderboardTest {
    private Leaderboard leaderboard;
    private static final String filename = "C:\\Users\\Rishabh\\University\\Fall 2024\\SENG 300\\Project-With-JFX-Updated\\seng300-f24-project\\Iteration_3\\src\\test\\java\\ca\\ucalgary\\seng300\\p4\\condensation\\condensation\\Networking\\Database\\Leaderboard\\leaderboard.json";

    @Before
    public void setUp() throws Exception {
        leaderboard = new Leaderboard(filename);
        leaderboard.loadData();
    }


    @Test( expected = FileNotFoundException.class)
    public void loadDataTest1() throws IOException, ParseException {

    }


    @Test
    public void saveData(){

    }

    @Test
    public void getAllUsers() {
    }

    @Test
    public void createUser() {
    }

    @Test
    public void findUserByName() {
    }

    @Test
    public void findUserByPassword() {
    }

    @Test
    public void findUserByNameToString() {
    }

    @Test
    public void findUserById() {
    }

    @Test
    public void updateEloForWinningPlayer() {
    }

    @Test
    public void fetchTop5Players() {
    }

    @Test
    public void updateEloForLosingPlayer() {
    }

    @Test
    public void refreshLeaderboard() {
    }

    @Test
    public void printLeaderboard() {
    }
}