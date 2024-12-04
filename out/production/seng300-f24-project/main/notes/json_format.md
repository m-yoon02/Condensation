# JSON Format in Server-Client communication

## Frontend to Backend

**THIS IS WHAT FRONTEND SENDS TO BACKEND**

```json
{
    "SessionID": "UUID Representing Session",
    "PlayerID": "UUID of the player making the move",
    "NewPosition": [0, 5],
    // If the game is Checkers:
    "OldPosition": [1, 6]
    //NewPosition represents the new position that the player wants to move the piece to
    //IF THE GAME IS CHECKERS, OldPosition will represent the old position of the piece
}
```

## Backend to Frontend

**THIS IS WHAT BACKEND SENDS TO FRONTEND**
One JSON string is sent to frontend to notify state, **make sure to differentiate by checking what UpdateType is** since updates differ by UpdateType

```json
{
    "UpdateType": "GAME_END", //(OR StateChange)
    "GameState": "WIN", // (or TIE)
    // IF GameState == WIN:
    "PlayerWon": "UUID of the player who won",
    "PlayerLost": "UUID of the player who lost"
    // if GameState == TIE, above fields will NOT exist
}
```

```json
{
    "UpdateType": "TURN_END",
    "SessionID": "UUID of the Session",
    "PlayerTurn": "UUID of the next player's turn",
    "Changes": [
        ["EMPTY", "RED", "EMPTY"],
        ["BLUE", "RED", "EMPTY"],
        ["EMPTY", "BLUE", "EMPTY"],
    ] // A 2D array representing the current state of the board on the Back End
    // RED, BLUE are not the only colours, colours differ per game, SEE BELOW
}
```

## COLORS PER GAME

### Connect Four

- **First player:** BLUE
- **Second player:** ORANGE

### Checkers

- **First player:** RED
- **Second player:** BLACK

### Tic Tac Toe

- **First player:** RED
- **Second player:** BLUE
