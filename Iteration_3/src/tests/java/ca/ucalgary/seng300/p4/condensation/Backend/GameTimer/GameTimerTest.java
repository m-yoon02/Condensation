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

    @Test
    public void stopTimer() {
        gameTimer.stopTimer();
        assertFalse("Timer should not be active", gameTimer.isActive());
    }

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

    @Test
    public void testGetOtherPlayerID_ReturnsNullWhenOnlyOnePlayer() {

        UUID singlePlayerID = UUID.randomUUID();
        GameTimer singlePlayerTimer = new GameTimer(singlePlayerID, singlePlayerID, 60);

        singlePlayerTimer.getTimerHashMap().remove(singlePlayerID);
        singlePlayerTimer.getTimerHashMap().put(singlePlayerID, 60);

        UUID otherPlayerID = singlePlayerTimer.getOtherPlayerID(singlePlayerID);

        assertNull("getOtherPlayerID should return null when there is no other player", otherPlayerID);
    }

    @Test
    public void testSwitchTimer_ValidPlayerID() {
        UUID initialPlayerID = gameTimer.getCurrentPlayerID();
        UUID otherPlayerID = gameTimer.getOtherPlayerID(initialPlayerID);

        gameTimer.switchTimer(initialPlayerID);

        assertEquals("Current player should be switched to the other player", otherPlayerID, gameTimer.getCurrentPlayerID());
    }

    @Test
    public void testSwitchTimer_InvalidPlayerID_DoesNotChangeCurrentPlayer() {
        UUID invalidPlayerID = UUID.randomUUID();
        UUID initialPlayerID = gameTimer.getCurrentPlayerID();

        gameTimer.switchTimer(invalidPlayerID);

        assertEquals("Current player should remain unchanged when invalid ID is provided", initialPlayerID, gameTimer.getCurrentPlayerID());
    }

    @Test
    public void testDisplayTimer_LessThanOneMinute() {
        String result = gameTimer.displayTimer(0);
        assertEquals("00", result);

        result = gameTimer.displayTimer(5);
        assertEquals("05", result);

        result = gameTimer.displayTimer(59);
        assertEquals("59", result);
    }

    @Test
    public void testDisplayTimer_ExactlyOneMinute() {
        String result = gameTimer.displayTimer(60);
        assertEquals("01:00", result);
    }

    @Test
    public void testDisplayTimer_LessThanOneHour() {
        String result = gameTimer.displayTimer(61);
        assertEquals("01:01", result);

        result = gameTimer.displayTimer(3599);
        assertEquals("59:59", result);
    }

    @Test
    public void testDisplayTimer_ExactlyOneHour() {
        String result = gameTimer.displayTimer(3600);
        assertEquals("01:00:00", result);
    }

    @Test
    public void testDisplayTimer_MoreThanOneHour() {
        String result = gameTimer.displayTimer(3601);
        assertEquals("01:00:01", result);

        result = gameTimer.displayTimer(3661);
        assertEquals("01:01:01", result);

        result = gameTimer.displayTimer(86399);
        assertEquals("23:59:59", result);
    }

    @Test
    public void testDisplayTimer_ExactlyOneDay() {
        String result = gameTimer.displayTimer(86400);
        assertEquals("24:00:00", result);
    }

    @Test
    public void testDisplayTimer_LargeTimeValues() {
        String result = gameTimer.displayTimer(90061);
        assertEquals("25:01:01", result);

        result = gameTimer.displayTimer(172800);
        assertEquals("48:00:00", result);
    }

    @Test
    public void testDisplayTimer_BoundaryValues() {
        String result = gameTimer.displayTimer(59);
        assertEquals("59", result);

        result = gameTimer.displayTimer(60);
        assertEquals("01:00", result);

        result = gameTimer.displayTimer(3599);
        assertEquals("59:59", result);

        result = gameTimer.displayTimer(3600);
        assertEquals("01:00:00", result);
    }

    @Test
    public void testDisplayTimer_ZeroTime() {
        String result = gameTimer.displayTimer(0);
        assertEquals("00", result);
    }
}