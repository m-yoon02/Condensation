package ca.ucalgary.seng300.p4.condensation.condensation.Networking.DTO;

import ca.ucalgary.seng300.p4.condensation.condensation.Networking.Enums.GameTypes;

import java.util.UUID;

public record QueuedPlayerMemo (UUID PlayerID, GameTypes gameType, int Elo) {}
