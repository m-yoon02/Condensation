**Review 2 Suggested Changes**

**Class Diagram:** 

- Expand on associations between classes to make the diagram look more cohesive
- Include missing entities
  - Show how the Leaderboard class interacts with other components
    - gameSessionManager, Player
- Connect network, frontend, and backend to show the structure of the entire project

*Backend:* 

- checkersHandler should be associated with checkersPiece, same for connect four and tic tac toe

*Frontend:*

- birthday not used
  - take age as an input to verify age instead of birthday
- fix arrows that do not reach or are too long
- classes need arrows instead of direct lines as connections, clarify relationships between classes
- all signUp fields are not used in the Profile class
- unclear relationship between the Checkers and TicTacToe and ConnectFour classes (line that connects the three)
- afterMath is could be dependent on each of the games for the result

*Networking:*

- connection is independent 
- make naming convention consistent throughout all attributes
- IGameHandler extends MoveData relationship not shown 
- fix representation of parent-child relationship between LeaderBoardService and StubDatabase

**Use Case Diagrams:**

*Backend:* 

- order should have been “start game” -> “resign game” and “choose a side” -> “place a piece” 
  - I don’t think their suggested format for the use case diagram is correct

*Frontend:*

- Add use case “update player profile”
- Missing some extends and includes relationships

*Networking:*

- Make flow of relationship easier to understand

*Generalized:*

- Change relationship to be extends instead of includes if players do not always use text chat

**Use Case Descriptions:**

- Use consistent wording throughout

*Backend:* 

- Make primary actors match the use case diagram
- Make the beginning of the scenario logically follow the preconditions or does not exclude certain cases
- Clarify that the Update Player Timer use case occurs once and that the timer is continuously running, just switching between the player’s times according to whose turn it is
- Clarify whose turn it is when the game starts

*Frontend:*** 

- Make sure that the scenario does not include anything that was already mentioned in the pre-conditions or trigger

*Networking:* 

- No major changes

**Proposed Features:**

- Friendship system
- Reconnection support
- Pause game



