## Extra Backend Planning Notes

### GameHandler CHANGES
- Is now required to instantiate and store both PlayerMemos (Player 1 and Player 2) in a HashMap<UUID, PlayerMemo>
    - Will insantiate PlayerMemos from UUID passed somehow
    - Randomly assign PieceColors to each PlayerMemo depending on the game
    - These are used to identify player colours and UUIDs
- makeMove(moveData: T) -> makeMove(moveDataJSON: String)
    - Instead of networking passing us already parsed data, we will PARSE it ourselves from the JSON string