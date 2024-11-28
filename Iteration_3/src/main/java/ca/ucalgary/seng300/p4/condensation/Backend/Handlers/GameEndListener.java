package ca.ucalgary.seng300.p4.condensation.Backend.Handlers;
import ca.ucalgary.seng300.p4.condensation.Backend.Enums.GameResult;
import ca.ucalgary.seng300.p4.condensation.condensation.Backend.Enums.*;

import java.util.UUID;

public interface GameEndListener {
    void onGameEnd(GameResult gameResult, UUID winnerPlayer);
}