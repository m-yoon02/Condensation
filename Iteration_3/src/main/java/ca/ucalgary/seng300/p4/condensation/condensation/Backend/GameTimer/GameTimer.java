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
        this.currentPlayerID = getOtherPlayerID(playerIDToSwitchFrom);
    }

    public void startTimer(){

    }

    public void addTimerEndListener(TimerEndListener listener){
        timerEndListeners.add(listener);
    }
}
