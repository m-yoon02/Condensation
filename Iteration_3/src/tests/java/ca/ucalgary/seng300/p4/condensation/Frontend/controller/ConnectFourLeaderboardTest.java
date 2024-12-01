package java.ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import ca.ucalgary.seng300.p4.condensation.Frontend.controller.ConnectFourHomeMenuController;
import ca.ucalgary.seng300.p4.condensation.Frontend.controller.ConnectFourLeaderboard;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;

import static org.junit.Assert.assertNotNull;

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
    }

    @After
    public void tearDown() throws Exception {
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
//        assertNotNull(leaderboard.rankColumn);
//        assertNotNull(leaderboard.nameColumn);
//        assertNotNull(leaderboard.gamesPlayedColumn);
//        assertNotNull(leaderboard.winsColumn);
//        assertNotNull(leaderboard.lossesColumn);
//        assertNotNull(leaderboard.drawsColumn);
//        assertNotNull(leaderboard.winPercentageColumn);
    }
}
