package ca.ucalgary.seng300.p4.condensation.condensation.Backend.GameTimer;

import ca.ucalgary.seng300.p4.condensation.condensation.Backend.Enums.PieceColor;

import java.util.ArrayList;
import java.util.HashMap;

public class GameTimer {
    //Map PieceColor (To represent the players) to the time left
    private final HashMap<PieceColor, Integer> timerHashMap = new HashMap<>();
    private final ArrayList<TimerEndListener> timerEndListeners = new ArrayList<>();
    private final PieceColor[] playerColors;
    //currentPlayerColor is the color of the player whose timer is currently being counted down
    private PieceColor currentPlayerColor;

    public GameTimer(PieceColor[] colors, int timeInSeconds){
        this.playerColors = colors;
        this.currentPlayerColor = colors[0];
        for (PieceColor color: colors){
            timerHashMap.put(color, timeInSeconds);
        }
    }

    /**
     * Gets the PieceColor that is not thisColor, so the other player's color
     * @param thisColor the color to NOT get
     * @return the other player color that is not thisColor
     */
    private PieceColor getOtherPlayerColor(PieceColor thisColor){
        for (PieceColor color: playerColors){
            if (color != thisColor){
                return color;
            }
        }
        return playerColors[0];
    }

    /**
     * Notifies all listeners that one of the player's timers has run out, passes the winning color
     * @param loserColor the color of the player whose timer has run out
     */
    private void notifyTimerEnded(PieceColor loserColor){
        PieceColor winnerColor = getOtherPlayerColor(loserColor);
        for (TimerEndListener listener: timerEndListeners){
            listener.onTimerEnd(winnerColor);
        }
    }

    /**
     * Sets the current timer to the other player from playerColorToSwitchFrom
     * @param playerColorToSwitchFrom the color of the player to switch from
     */
    public void switchTimer(PieceColor playerColorToSwitchFrom){
        this.currentPlayerColor = getOtherPlayerColor(playerColorToSwitchFrom);
    }

    public void startTimer(){

    }

    public void addTimerEndListener(TimerEndListener listener){
        timerEndListeners.add(listener);
    }
}
