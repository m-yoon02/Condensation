package ca.ucalgary.seng300.p4.condensation.condensation.Backend.GameTimer;

import ca.ucalgary.seng300.p4.condensation.condensation.Backend.Enums.PieceColor;

public interface TimerEndListener {
    void onTimerEnd(PieceColor playerColor);
}
