package ca.ucalgary.seng300.p4.condensation.condensation.Backend.Handlers;
import ca.ucalgary.seng300.p4.condensation.condensation.Backend.Enums.PieceColor;

public interface TurnEndListener {
    void onTurnEnd(PieceColor currentPlayerColor);
}
