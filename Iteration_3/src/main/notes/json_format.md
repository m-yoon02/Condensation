# JSON Format in Server-Client communication

## Frontend to Backend
```json
{
    "SessionID": "UUID Representing Session",
    "PlayerID": "UUID of the player making the move",
    "Positions": [
        [0, 5],
        //If the game is checkers:
        [1, 6]
    ]
    //Positions[0] represents the new position that the player wants to move the piece to
    //IF THE GAME IS CHECKERS, Positions[1] will represent the old position of the piece
}
```

## Backend to Frontend
One JSON string is sent to frontend to notify state, **make sure to differentiate by checking what UpdateType is** since updates differ by UpdateType

```json
{
    "UpdateType": "GameEnd", //(OR StateChange)
    "Data": {
        "GameState": "Win", // (OR Lose, Tie)
    }
}
```

```json
{
    "UpdateType": "StateChange",
    "Data": {
        "Changes": [
            {
                "Type": "Add", //Can be Add, Move or Remove (Any maybe Promotion for Checkers)
                "Color": "RED", //RED, BLACK, WHITE, BLUE, ORANGE (IFF Type is Add)
                "Positions": [
                    [0, 5],
                    //If the type is Move:
                    [1, 6]
                    //Positions[0] represents the new position that the player wants to move the piece to
                    //IF TYPE IS MOVE, Positions[1] will represent the old position of the piece
                ]
            },
            ... //There can be more than one of these tables
        ]
    }
}
```