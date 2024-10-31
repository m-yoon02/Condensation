# Use Cases Descriptions for Network

## **Use Case**: Establish connection

**Iteration**: 1

**Primary Actor**: Player

**Goal in Context**: To establish a secure and stable connection with the network to access game services

**Preconditions**: 
- Player has launched the game
- Network Services are online and available

**Trigger**: Player initiates a connection attempt when starting the game

**Scenario**:
1. Player launches the game and reaches the main menu
2. Player selects an option that requires an online connection (all three of our games need it)
3. `NetworkManager` receives the player's request to connect
4. `NetworkManager` verifies player credentials and initiates a secure session (token? authentication method?)
5. `NetworkManager` checks the network quality and stable connection
6. Player is now connected to the network

**Post Conditions**:
- Player is successfully connected and ready to access online features (leaderboard, SBMM, chat)

**Exceptions**:
- Network is down and unavailable
- Connection timeout: the network takes too long to respond
- Poor connection quality

**Priority**: High

**When Available**: 2nd iteration

**Frequency of use**: Every time a player initiates a connection or re-connects

**Channel to Actor**: GUI 

**Secondary Actor**: Network (mostly `NetworkManager`)

**Channels to Secondary Actor**: Stub? (realistically an API call)

**Open Issues**: 
- Say a player keeps having to reconnect, should there be a timer to stage intervals?


## **Use Case**: Send Message

**Iteration**: 1

**Actor**: Player

**Goal in Context**: To allow a player to send a message to their opponent during a game session

**Preconditions**:
- both players are connected to the same game session
- chat functionality is enabled for the game session

**Trigger**: Player initiates a message by typing into the in-game chat interface

**Scenario**:
1. Player opens the in-game chat interface
2. Player types a message in a text input box
3. Player sends their message by clicking "Send" button or pressing "Enter" on their keyboard
4. `Network Manager` receives the message and forwards it to `ChatManager`
5. `ChatManager` routes the message to the opponent
6. The message appears in the opponent's chat interface

**Post Conditions**:
- The message is received and displayed in the opponent's chat interface/window
- both players can see the updated chat log with the new message


**Exceptions**:
- A player disconnecting while in the process of sending their message
- network failure
- message blocking (inappropriate language)


**Priority**: High

**When Available**: 2nd iteration

**Frequency of use**: It varies as it depends on a player's communication preferences

**Channel to Actor**: In-game chat interface/window

**Secondary Actor**: Network - `NetworkManager` and `ChatManager`

**Channels to Secondary Actor**: N/A

**Open Issues**:
- Filtering chat which can be a long and heavy process to do


## **Use Case**: Queue Entry

**Iteration**: 1

**Actor**: Player

**Goal in Context**: To allow a player to enter a matchmaking queue and wait for a compatible opponent to begin a game session

**Preconditions**:
- Both players are connected to the network
- Matchmaking services are active and available

**Trigger**: Player selects an option to queue up matchmaking

**Scenario**:
1. Player navigates to the three different games they want to play in the GUI
2. Player chooses a game by selecting one
3. Player chooses to join matchmaking queue by clicking "find game" button in the GUI
4. `NetworkManager` sends a request to `MatchmakingService` to add the player to the queue
5. `MatchmakingService` places the player in a queue and begins searching for the right opponent based on SBMM criteria
6. Player receives a notification/status update to notify that they have found an opponent
7. The player is transitioned to the game session with their matched opponent

**Post Conditions**:
- Player is currently in an active match with their opponent or still remains in the queue if no match is available


**Exceptions**:
- Network failure
- Timeout connection - if a player can't find a match within a certain amount of time
- One of the player disconnects or leaves the matchmaking queue
- Too many players in the matchmaking queue - a capacity limit


**Priority**: High

**When Available**: 2nd iteration

**Frequency of use**: Frequent as our games are all based on multiplayer

**Channel to Actor**: GUI interface

**Secondary Actor**: Network - `MatchmakingService`

**Channels to Secondary Actor**: Stub (API if we're being serious) calls and notifies from `NetworkManager` to `MatchmakingService`

**Open Issues**:
- We should define a time limit when waiting in queue before a timeout





## **Use Case**: Status Update 

**Actor**: Player

**Goal in Context**: To provide a player with updates regarding their current status within the game. For example, connection quality and matchmaking queue position

**Preconditions**:
- player is connected to network and using the network features - SBMM, game chat, etc...
- `NetworkManager` or `MatchmakingService` is actively tracking player status

**Trigger**: A change in a player's status or an update from the network that needs to be communicated to the player

**Scenario**:
1. Player initiates an action that requires a status update - entering matchmaking queue or sending a message
2. `NetworkManager` detects a change in the payer's current activity - moving up a spot in matchmaking queue
3. `NetworkManager` sends a status update to the player's GUI/interface regarding their current activity - estimated wait time, current queue position, network status etc...
4. Player receives and views the update on their screen
5. Player may adjust their actions - exit queue if it's taking too long, latency issues / connection status

**Post Conditions**:
- The player is aware of their current status and makes a decision or not based on the updated information


**Exceptions**:
- Network failure
- A player may disconnect before the status update is received 
- Network latency might cause delay to status updates - potentially giving outdated or slow feedback


**Priority**: Medium - it gives a nice experience for the players to know what their status is, but not as important as gameplay functionality

**When Available**: 3rd iteration

**Frequency of use**: It varies as it'll be based on network events or player actions

**Channel to Actor**: GUI - notification or a status window

**Secondary Actor**: Network - `NetworkManager` and `MatchmakingService`

**Channels to Secondary Actor**: Stub (API) calls for status changes

**Open Issues**:
- We have to determine which relevant statuses to update the player on
- the frequency and method of update delivery 

