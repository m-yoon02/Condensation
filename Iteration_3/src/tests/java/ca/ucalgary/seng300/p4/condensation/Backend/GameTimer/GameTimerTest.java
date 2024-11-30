package ca.ucalgary.seng300.p4.condensation.Backend.GameTimer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import java.util.UUID;

import static org.junit.Assert.*;

public class GameTimerTest {

    private UUID p1ID;
    private UUID p2ID;
    private int timeInSeconds;

    private GameTimer gameTimer;


    @Before
    public void setUp() throws Exception {
        p1ID = UUID.randomUUID();
        p2ID = UUID.randomUUID();

        Random rand = new Random();

        timeInSeconds = rand.nextInt(100);

        gameTimer = new GameTimer(p1ID, p2ID, timeInSeconds);
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
    public void startTimer() {
        gameTimer.startTimer();
        assertTrue("Timer should be active", gameTimer.isActive());
    }

    @Test
    public void stopTimer() {
        gameTimer.stopTimer();
        assertFalse("Timer should not be active", gameTimer.isActive());
    }

}