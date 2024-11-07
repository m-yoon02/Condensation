package ca.ucalgary.seng300.p4.condensation.condensation.Backend.Handlers;
import ca.ucalgary.seng300.p4.condensation.condensation.Backend.Enums.*;

public interface GameEndListener {
    void onGameEnd(GameResult gameResult, PieceColor winnerColor);
}
