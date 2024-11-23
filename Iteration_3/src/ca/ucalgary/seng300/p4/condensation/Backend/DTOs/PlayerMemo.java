package ca.ucalgary.seng300.p4.condensation.condensation.Backend.DTOs;

import ca.ucalgary.seng300.p4.condensation.condensation.Backend.Enums.PieceColor;

import java.util.UUID;

public record PlayerMemo(UUID playerID, PieceColor pieceColor) {
}
