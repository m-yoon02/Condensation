package ca.ucalgary.seng300.p4.condensation.Networking.DTO;

import ca.ucalgary.seng300.p4.condensation.Networking.Enums.GameTypes;

import java.util.UUID;


/**
 * Data Transfer Object (DTO) representing a queued player's information.
 * This record is used to store and transfer player details for matchmaking.
 * @param PlayerID The unique identifier of the player.
 * @param gameType The type of game the player wants to play.
 * @param Elo      The player's Elo rating for matchmaking purposes.
 */
public record QueuedPlayerMemo (UUID PlayerID, GameTypes gameType, int Elo) {}
