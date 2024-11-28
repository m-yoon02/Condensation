# JSON Format in Server-Client communication

## Frontend to Backend

```json
{
    "SessionID": "UUID Representing Session",
    "PlayerID": "UUID of the player making the move",
    "Positions": {
        "NewPosition": [0, 5],
        // If the game is Checkers: 
        "OldPosition": [1, 6],
    }
    //NewPosition represents the new position that the player wants to move the piece to
    //IF THE GAME IS CHECKERS, OldPosition will represent the old position of the piece
}
```

## Backend to Frontend

One JSON string is sent to frontend to notify state, **make sure to differentiate by checking what UpdateType is** since updates differ by UpdateType

```json
{
    "UpdateType": "GAME_END", //(OR StateChange)
    "GameState": "WIN" // (OR LOSE, TIE)
}
```

```json
{
    "UpdateType": "TURN_END",
    "SessionID": "UUID of the Session",
    "PlayerTurn": "UUID of the next player's turn",
    "Changes": [
        {
            "Type": "ADD", //Can be ADD, MOVE or REMOVE
            "Color": "RED", // IF Type == ADD, Color sent will depend on the game type
            "Positions": {
                "NewPosition": [0, 5],
                //If the type is Move:
                "OldPosition": [1, 6]
                //NewPosition represents the new position that the player wants to move the piece to
                //IF TYPE IS MOVE, OldPosition will represent the old position of the piece
            }
        },
        ... //There can be more than one of these tables
    ]
}
```

## COLORS PER GAME

### Connect Four

- BLUE
- ORANGE

### Checkers

- RED
- BLACK

### Tic Tac Toe

- WHITE
- BLACK
