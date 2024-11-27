package ca.ucalgary.seng300.p4.condensation.condensation.Backend.GameTimer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class GameTimer {
    //Map UUID (To represent the players) to the time left
    private final HashMap<UUID, Integer> timerHashMap = new HashMap<>();
    private final ArrayList<TimerEndListener> timerEndListeners = new ArrayList<>();
    //currentPlayerID is the ID of the player whose timer is currently being counted down
    private UUID currentPlayerID;
    private boolean active = false;
    private int currentPlayerTimer;

    public GameTimer(UUID player1ID, UUID player2ID, int timeInSeconds){
        currentPlayerID = player1ID;
        timerHashMap.put(player1ID, timeInSeconds);
        timerHashMap.put(player2ID, timeInSeconds);
    }

    /**
     * Gets the UUID that is not this playerID, so the other player's ID
     * @param playerID the UUId to NOT get
     * @return the other player UUID that is not UUID
     */
    private UUID getOtherPlayerID(UUID playerID){
        for (UUID id: timerHashMap.keySet()){
            if (!id.equals(playerID)){
                return id;
            }
        }
        return null;
    }

    /**
     * Notifies all listeners that one of the player's timers has run out, passes the winning player ID
     * @param loserPlayerID the ID of the player whose timer has run out
     */
    private void notifyTimerEnded(UUID loserPlayerID){
        UUID winnerPlayerID = getOtherPlayerID(loserPlayerID);
        for (TimerEndListener listener: timerEndListeners){
            listener.onTimerEnd(winnerPlayerID);
        }
    }

    /**
     * Sets the current timer to the other player from playerIDToSwitchFrom
     * @param playerIDToSwitchFrom the id of the player to switch from
     */
    public void switchTimer(UUID playerIDToSwitchFrom){
        if (timerHashMap.containsKey(playerIDToSwitchFrom)) {
            this.currentPlayerID = getOtherPlayerID(playerIDToSwitchFrom);
        }
    }

    /**
     * Overloaded switchTimer method that alternates between players without needing to specify
     * current player
     */
    public void switchTimer(){
        this.currentPlayerID = getOtherPlayerID(currentPlayerID);
    }

    /**
     * Sets the current timer to the player ID of playerIDTOSet
     * @param playerIDToSet the player ID to set to
     */
    public void setTimerTo(UUID playerIDToSet) {
        if (timerHashMap.containsKey(playerIDToSet)) {
            this.currentPlayerID = playerIDToSet;
        }
    }

    /**
     * Starts the game timer countdown
     * @exception Throws InterruptedException if the thread is interupted while sleeping
     */
    public void startTimer() throws InterruptedException {
        UUID backupCurrentPlayer;
        active = true;
        while (active) {
            backupCurrentPlayer = currentPlayerID;
            currentPlayerTimer = getPlayerTimer(backupCurrentPlayer);
            currentPlayerTimer--;
            timerHashMap.put(backupCurrentPlayer, currentPlayerTimer);
            if (currentPlayerTimer <= 0){
                stopTimer();
                notifyTimerEnded(backupCurrentPlayer);
            }
            Thread.sleep(1000);
        }
    }

    /**
     * Stops the game timer countdown (when the game ends or something)
     */
    public void stopTimer(){
        active = false;
    }

    /**
     * Returns a specified player timer. Useful for frontend representation
     * @param playerID the specified player
     * @return the remaining time for the player
     */
    public int getPlayerTimer(UUID playerID){
        return timerHashMap.get(playerID);
    }

    /**
     * Returns a formatted string that represents the remaining time.
     * Format: hh:mm:ss -> mm:ss -> ss
     * @param playerTime remaining player time
     * @return formatted string
     */
    public String displayTimer(int playerTime){
        // Define strings to displayed later
        String displayHours;
        String displayMinutes;
        String displaySeconds;

        // Calculate hours, minutes and seconds based on playerTime
        int hours = (playerTime / 3600);
        int minutes = ((playerTime % 3600) / 60);
        int seconds = ((playerTime % 3600) % 60);

        // Formats to "0h"/"0m"/"0s" in case there's less than 10 units remaining

        if (hours < 10){
            displayHours = "0" + String.valueOf(hours);
        } else {
            displayHours = String.valueOf(hours);
        }

        if (minutes < 10){
            displayMinutes = "0" + String.valueOf(minutes);
        } else {
            displayMinutes = String.valueOf(minutes);
        }

        if (seconds < 10){
            displaySeconds = "0" + String.valueOf(seconds);
        } else {
            displaySeconds = String.valueOf(seconds);
        }

        // If less than one hour remains, use mm:ss format;
        // If less than one minute remains, use ss format;
        if (displayHours.equals("00")){
            if (displayMinutes.equals("00")){
                return String.format("%s", displaySeconds);
            }
            return String.format("%s:%s", displayMinutes, displaySeconds);
        }
        // Otherwise use common hh:mm:ss format
        return String.format("%s:%s:%s", displayHours, displayMinutes, displaySeconds);
    }

    public void addTimerEndListener(TimerEndListener listener){
        timerEndListeners.add(listener);
    }
}
