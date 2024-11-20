package ca.ucalgary.seng300.p4.condensation.condensation.Backend.Handlers;

import java.util.UUID;

public interface TurnEndListener {
    void onTurnEnd(UUID playersTurnID);
}
