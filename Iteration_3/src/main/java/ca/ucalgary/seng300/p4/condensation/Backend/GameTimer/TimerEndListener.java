package ca.ucalgary.seng300.p4.condensation.Backend.GameTimer;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.UUID;

public interface TimerEndListener {
    void onTimerEnd(UUID winnerPlayerID) throws IOException, ParseException;
}
