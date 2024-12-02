package java.ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import ca.ucalgary.seng300.p4.condensation.Frontend.controller.ConnectFourHomeMenuController;
import ca.ucalgary.seng300.p4.condensation.Frontend.controller.ConnectFourLeaderboard;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;

import static org.junit.Assert.*;

public class ConnectFourLeaderboardTest {
    ConnectFourLeaderboard leaderboard; //create a leaderboard
    private ObservableList<ConnectFourHomeMenuController.Player> mockPlayerList; //create a mock player list

    /**
     * the setUp for leaderboard testing is having a mockPlayerList. We use this playerList to test
     * how the leaderboard displays/removes/adds players.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        //set up the leaderboard
        leaderboard = new ConnectFourLeaderboard();

        //put players into the mockPlayerList
        mockPlayerList = FXCollections.observableArrayList(
                new ConnectFourHomeMenuController.Player("1239", "Sam", 5, 2, 3),
                new ConnectFourHomeMenuController.Player("1012", "Hannah", 2, 2, 0)
        );

        //should be able to assign playerList to leaderboard.playerList
        leaderboard.setPlayerList(mockPlayerList);
    }

    /**
     * Test the initializaiton of Columns. Make sure when initialization is called,
     * the values being created are not null.
     */
    @Test
    public void testInitializeColumns() {
        //call the initialize method
        leaderboard.initialize();

        //left these errors in for now, but this is the basic idea of test #1
        assertNotNull(leaderboard.getRankColumn());
        assertNotNull(leaderboard.getNameColumn());
        assertNotNull(leaderboard.getGamesPlayedColumn());
        assertNotNull(leaderboard.getWinsColumn());
        assertNotNull(leaderboard.getLossesColumn());
        assertNotNull(leaderboard.getDrawsColumn());
        assertNotNull(leaderboard.getWinPercentageColumn());

        //test the column alignment, these all should be center aligned
        assertEquals("-fx-alignment: CENTER;", leaderboard.getRankColumn().getStyle());
        assertEquals("-fx-alignment: CENTER;", leaderboard.getGamesPlayedColumn().getStyle());
        assertEquals("-fx-alignment: CENTER;", leaderboard.getWinsColumn().getStyle());
        assertEquals("-fx-alignment: CENTER;", leaderboard.getLossesColumn().getStyle());
        assertEquals("-fx-alignment: CENTER;", leaderboard.getDrawsColumn().getStyle());
        assertEquals("-fx-alignment: CENTER;", leaderboard.getWinPercentageColumn().getStyle());
    }

    /**
     * test the sorting of the players which is sorted by win percentage.
     */
    @Test
    public void testSorting() {
        leaderboard.initialize();

        //get the sorted player list from leaderboard
        ObservableList<ConnectFourHomeMenuController.Player> sortedList = leaderboard.getLeaderboardTable().getItems();

        //test that players are sorted correctly by their win percentage in descending order
        double prevWinPercent = 100;

        for (ConnectFourHomeMenuController.Player player : sortedList) {
            double currentWinPercent = (player.getWinsInCheckers() * 100.0) / player.getGamesPlayedCheckers();

            assertTrue(currentWinPercent <= prevWinPercent); //check if players are sorted in descending order by win percent

            prevWinPercent = currentWinPercent; //set the prev win percent to be the current one for this player
        }
    }

    /**
     * testing the rank assignment to the sorted list
     */
    @Test
    public void testRankingAssignment() {
        leaderboard.initialize();

        //get the sorted player list from leaderboard
        ObservableList<ConnectFourHomeMenuController.Player> sortedList = leaderboard.getLeaderboardTable().getItems();

        for (int i = 0; i < sortedList.size(); i++) {
            assertEquals(Integer.valueOf(i + 1), leaderboard.getRankColumn().getCellData(sortedList.get(i)));
        }
    }
}
