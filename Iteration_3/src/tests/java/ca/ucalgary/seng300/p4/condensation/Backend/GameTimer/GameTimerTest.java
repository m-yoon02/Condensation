package ca.ucalgary.seng300.p4.condensation.Backend.GameTimer;

import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import java.util.UUID;

import static org.junit.Assert.*;

public class GameTimerTest {

    private static class MockTimerEndListener implements TimerEndListener {
        public UUID winnerPlayerID;

        @Override
        public void onTimerEnd(UUID winnerPlayerID) {
            this.winnerPlayerID = winnerPlayerID;
        }
    }

    private UUID p1ID;
    private UUID p2ID;
    private int timeInSeconds;
    private GameTimer gameTimer;
    private MockTimerEndListener mockListener;

    @Before
    public void setUp() throws Exception {

        p1ID = UUID.randomUUID();
        p2ID = UUID.randomUUID();

        Random rand = new Random();

        timeInSeconds = rand.nextInt(100);

        gameTimer = new GameTimer(p1ID, p2ID, timeInSeconds);
        mockListener = new MockTimerEndListener();
        gameTimer.addTimerEndListener(mockListener);
    }

    @Test
    public void testInitializationForTimerHashMaps_NotNull(){
        assertNotNull("Timer should not be null!",gameTimer.getTimerHashMap());
    }

    @Test
    public void testInitializationForTimerHashMaps_1(){
        assertEquals("Times should be equal", (int) gameTimer.getTimerHashMap().get(p1ID), timeInSeconds);
    }

    @Test
    public void testInitializationForTimerHashMaps_2(){
        assertEquals("Times should be equal", (int) gameTimer.getTimerHashMap().get(p2ID), timeInSeconds);
    }

    @Test
    public void testGetOtherPlayerUUID_1(){
        assertEquals("UUID of other player should match!", gameTimer.getOtherPlayerID(p1ID), p2ID);
    }

    @Test
    public void testGetOtherPlayerUUID_2(){
        assertEquals("UUID of other player should match!", gameTimer.getOtherPlayerID(p2ID), p1ID);
    }

//    @Test
//    public void startTimer() throws InterruptedException {
//        gameTimer.startTimer();
//        assertTrue("Timer should be active", gameTimer.isActive());
//        gameTimer.stopTimer();
//        assertFalse("Timer ends after", gameTimer.isActive());
//    }

//    @Test
//    public void stopTimer() {
//        gameTimer.stopTimer();
//        assertFalse("Timer should not be active", gameTimer.isActive());
//    }

    @Test
    public void testSwitchTimer_ValidPlayer() {
        gameTimer.switchTimer(p1ID);
        UUID currentPlayerID = gameTimer.getCurrentPlayerID();
        assertEquals(p2ID, currentPlayerID);
    }

    @Test
    public void testSwitchTimer_InvalidPlayer() {
        UUID invalidPlayerID = UUID.randomUUID();
        gameTimer.switchTimer(invalidPlayerID);
        UUID currentPlayerID = gameTimer.getCurrentPlayerID();
        assertEquals(p1ID, currentPlayerID);
    }

    @Test
    public void testSetTimerTo_ValidPlayer() {
        gameTimer.setTimerTo(p2ID);
        UUID currentPlayerID = gameTimer.getCurrentPlayerID();
        assertEquals(p2ID, currentPlayerID);
    }

    @Test
    public void testSetTimerTo_InvalidPlayer() {
        UUID invalidPlayerID = UUID.randomUUID();
        gameTimer.setTimerTo(invalidPlayerID);
        UUID currentPlayerID = gameTimer.getCurrentPlayerID();
        assertEquals(p1ID, currentPlayerID);
    }

    @Test
    public void testAddTimerEndListener() {
        assertTrue(gameTimer.getTimerEndListeners().contains(mockListener));
    }

    @Test
    public void testStopTimer() throws InterruptedException {
        gameTimer.startTimer();
        gameTimer.stopTimer();
        assertFalse(gameTimer.isActive());
    }

    @Test
    public void testGetOtherPlayerID() {
        UUID otherPlayer = gameTimer.getOtherPlayerID(p1ID);
        assertEquals(p2ID, otherPlayer);
    }
}