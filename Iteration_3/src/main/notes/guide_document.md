# Backend P3 Guide Document

- [Backend P3 Guide Document](#backend-p3-guide-document)
  - [Introduction](#introduction)
  - [Util Classes](#util-classes)
    - [Vector2](#vector2)
    - [PlayerMemo](#playermemo)
  - [Game Handlers](#game-handlers)
  - [Game Board](#game-board)
  - [Game Timer](#game-timer)

## Introduction
The long awaited guide document you guys have been waiting for 😭. Apologies for the long wait and I hope coding without this hasn't been too much of a hassle, but let's get started.

## Util Classes
These classes are meant to meant to help simply represent data so it is easier for you to work with
### Vector2
- A class representing a 2D position
- **This is used to represent all 2D position data**
- It has two fields:
  - ```java
    // x coordinate of the position
    Vector2.x()
    ```
  - ```java
    // y coordinate of the position
    Vector2.y()
    ```
- It also contains two helper methods:
  - ```java
    // adds two positions together
    Vector2.add(Vector2 other)
    ```
  - ```java
    // subtracts this position by the other position
    Vector2.sub(Vector2 other)
    ```

### PlayerMemo
- A simple DTO (or Record) that has two fields:
  - ```java
    // The UUID representing the player's id
    UUID playerID
    ```
  - ```java
    // The color that the player chose
    PieceColor pieceColor
    ```

## Game Handlers
All game handlers will extend from the abstract GameHandler class.
- **I advise you go take a look over the abstract GameHandler class so you know what utilities are available to you**

GameHandlers have a GameBoard object which you will use to represent the current state of the game

They also contain **PlayerMemos** of the players currently playing, there are multiple methods on how you can get these PlayerMemos from GameHandler

Specific game handlers (CheckersHandler, TicTacToeHandler, Connect4Handler) will have extra methods that you **can** modify at will.

You can also add extra methods to specific game handlers if needed.

Important methods:
```java
// This is the main driver of your game logic.
// Assume that this method is called whenever a player makes a move
// You are to use the move data and translate it to the GameBoard
// You are also required to check for game winning scenarios here
// This will also notify when the game has ended, and which player's turn it is after the move
@Override
public void makeMove(String moveDataJSON) {
    MoveData moveData = MoveData.fromJSON(moveDataJSON);
}
```

```java
/**
 * Notifies all turn end listeners that a turn has ended and which player's turn it is currently
 * @param currentPlayerTurnID the ID of the next player's turn
 */
protected void notifyTurnEnd(UUID currentPlayerTurnID)
```

```java
/**
 * Notifies all game end listeners that the game has ended
 * @param gameResult the game result
 * @param winnerID the ID of the winner player if the game has ended on a win
 */
protected void notifyGameEnd(GameResult gameResult, UUID winnerID)
```

## Game Board
The class that will represent the actual board and its pieces from our games

I will be personally implementing this class so you do not have to worry about implementing functionality here

But, **I heavily advise you to go look at the file and read the documentation so you have an understanding of what you can do with the class**

## Game Timer
This class keeps track of each player's remaining time throughout the game

It uses a HashMap to link a player's ID to the amount of time they have left
```java
private final HashMap<UUID, Integer> timerHashMap
```

There are two important states that you must keep track of

```java
//currentPlayerID is the ID of the player whose timer is currently being counted down
private UUID currentPlayerID;
```
Every second you are required to :
- Check if timerHashMap.get(currentPlayerID) is bigger than zero (If it is not then that player has lost, stop the timer and refer to **notifyTimerEnded**)
- If it is bigger than zero, you will decrement the time left linked to the player's ID by one

```java
private boolean active;
```
This represents if the GameTimer is currently active, make sure you stop the timer whenever this becomes false. (This would mean the game has ended)

An important method to regard :
```java
/**
 * Notifies all listeners that one of the player's timers has run out, passes the winning player ID
 * @param loserPlayerID the ID of the player whose timer has run out
 */
private void notifyTimerEnded(UUID loserPlayerID)
```
You are required to call this if whenever one of the player's has used up all their time. Simply pass in the ID of the player who has no more time left