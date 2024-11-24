package ca.ucalgary.seng300.p4.condensation.condensation.Networking.DTO;

import ca.ucalgary.seng300.p4.condensation.condensation.Backend.Enums.GameResult;
import ca.ucalgary.seng300.p4.condensation.condensation.Backend.Handlers.GameEndListener;

import java.util.UUID;

public class NetworkGameEndListener implements GameEndListener {
    @Override
    public void onGameEnd(GameResult gameResult, UUID winnerPlayer) {
        System.out.println();

    }
}
