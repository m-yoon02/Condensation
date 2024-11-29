package ca.ucalgary.seng300.p4.condensation.Backend.GameTimer;

import java.util.UUID;

public interface TimerEndListener {
    void onTimerEnd(UUID winnerPlayerID);
}
