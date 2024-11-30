package ca.ucalgary.seng300.p4.condensation.Backend.Handlers;
import ca.ucalgary.seng300.p4.condensation.Backend.Enums.GameResult;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.UUID;

public interface GameEndListener {
    void onGameEnd(GameResult gameResult, UUID winnerPlayer) throws IOException, ParseException;
}