package ca.ucalgary.seng300.p4.condensation.condensation.Backend.GameTimer;

import ca.ucalgary.seng300.p4.condensation.condensation.Backend.Enums.PieceColor;

import java.util.ArrayList;
import java.util.HashMap;

public class GameTimer {
    private final HashMap<PieceColor, Integer> timerHashMap = new HashMap<>();
    private final ArrayList<TimerEndListener> timerEndListeners = new ArrayList<>();
    private final PieceColor[] playerColors;
    private PieceColor currentPlayerColor;

    public GameTimer(PieceColor[] colors, int timeInSeconds){
        this.playerColors = colors;
        this.currentPlayerColor = colors[0];
        for (PieceColor color: colors){
            timerHashMap.put(color, timeInSeconds);
        }
    }

    private PieceColor getOtherPlayerColor(PieceColor currentColor){
        for (PieceColor color: playerColors){
            if (color != currentColor){
                return color;
            }
        }
        return playerColors[0];
    }

    private void notifyTimerEnded(PieceColor loserColor){
        PieceColor winnerColor = getOtherPlayerColor(loserColor);
        for (TimerEndListener listener: timerEndListeners){
            listener.onTimerEnd(winnerColor);
        }
    }

    public void switchTimer(PieceColor playerColorToSwitchFrom){
        this.currentPlayerColor = getOtherPlayerColor(playerColorToSwitchFrom);
    }

    public void startTimer() throws InterruptedException {

    }

    public void addTimerEndListener(TimerEndListener listener){
        timerEndListeners.add(listener);
    }
}
