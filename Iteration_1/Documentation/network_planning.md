# Networking Team Project Overview

## 1. NetworkManager

**Purpose**: Acts as the central hub for all network communication. This class will handle connections, message routing, and managing different network protocols if necessary. This class needs to be a singleton.

### Responsibilities of Class:
- **Establishing Connections**: Manages the initial setup for connecting players to the server.
- **Routing Messages**: Directs messages to the correct service (matchmaking, chat) based on the type of request.
- **Handling Protocols**: Manages protocols for real-time communication (WebSockets for chat) and handles any other required formats.

### Core Methods:
- `connect(userID)`: Establishes a connection for a given player.
- `disconnect(userID)`: Closes the connection.
- `sendMessage(target, message)`: Sends data to a specific target, whether it's matchmaking, chat, or game sessions. This is very important. Im still not sure if we want to abstract this out into more methods. I left it as one for now.
- `receiveMessage()`: Listens for incoming messages and routes them. This is also quite important. We are going to have to apply some sort of design pattern where for correct routing of packet data.


### Interactions:
- Interfaces with `MatchmakingService`, `ChatManager`, and `GameSessionManager` to pass along relevant messages.
- Centralizes all communications so other services don’t have to manage connections directly.

-  This class will handle, as its name suggests, most of the network requests we need to use. This allows our app to be as modular as possible. Most if not all basic to moderate backend services are set up this way.

---

## 2. MatchmakingService

**Purpose**: Manages player queueing and pairing based on skill level, player preferences, or other criteria.

### Responsibilities:
- **Queue Management**: Maintains a list of players waiting for a match and ensures they’re paired based on predefined criteria. We are going to use the queue data strcuture here. Shout out to my 331 lads.
- **Match Creation**: Pairs players and initiates a game session by notifying `GameSessionManager` when a match is found.

### Core Methods:
- `addToQueue(player)`: Adds a player to the matchmaking queue.
- `removeFromQueue(player)`: Removes a player from the queue if they cancel.
- `findMatch(player)`: Finds and pairs players, returning a match or keeping them in the queue if no match is available.
- `startMatch(player1, player2)`: Triggers the creation of a new game session between two matched players.

### Interactions:
- Communicates with `NetworkManager` to update players on their matchmaking status.
- Initiates a new game session with `GameSessionManager` once a match is found.
- Sends notifications to the players when they are matched. (Though I really dont think we need to do this...)

---

## 3. GameSessionManager

**Purpose**: Manages the state and progress of each game session, including player interactions, in-game events, and session termination. This class is going to be the hardest to implement here. I honestly dont know how we are going to keep track of game state. Considering that we arent acutally using websockets... I need big help on this lol.

### Responsibilities:
- **Session Initialization**: Sets up a game session once matchmaking finds a match, allocating the necessary resources (whatever those are lol) for the players.
- **In-Game State Management**: Tracks moves, scores, and events in the game, ensuring synchronization between players. Absolutely no idea how we are going to do this...
- **Session Termination**: Ends the session once the game concludes, storing any results that `LeaderboardService` may use. This should be fine

### Core Methods:
- `createSession(player1, player2)`: Initializes a new game session between two players.
- `updateGameState(action)`: Updates the state of the game (e.g., when a player makes a move).
- `endSession()`: Closes the game session, triggering leaderboard updates.

### Interactions:
- Works closely with `NetworkManager` to communicate real-time game updates to each player. This is honestly a guess here. It porbably will work with the network manager but Im not sure why... I dont plan on using any socket logic in the NetworkManager im not going to lie. 
- Notifies `LeaderboardService` of game results so that player rankings can be updated.

---

## 4. ChatManager

**Purpose**: Manages in-game chat between players, allowing them to communicate during a match. A big thing we need to consider with the chat manager class is how we store all our messages. Do we want to map them to an ID? or do we not care... The former is much harder than the ladder, espically if we consider there could be thousands of messages. We may want to implement a ringbuffer to only keep an X number of messages. Im not sure. Though, its really not that serious.

### Responsibilities:
- **Real-Time Messaging**: Handles sending and receiving chat messages during active game sessions. Granted, this would require us to acutally use a websocket to acutally work...
- **Message Queuing and Delivery**: Ensures that messages are sent to the correct recipients in the correct order.
- **Filtering and Moderation (optional)**: Filters inappropriate language or spam to keep the chat environment friendly. This is just a fun idea. I dont think we should do this. I only added it for fun.

### Core Methods:
- `sendMessage(fromPlayer, message)`: Sends a message from one player to their opponent.
- `receiveMessage()`: Receives and displays incoming messages.
- `moderateMessage(message)`: (Optional) Filters or flags inappropriate language.

### Interactions:
- Communicates with `NetworkManager` to send and receive messages in real time.
- Provides chat functionality only during active game sessions managed by `GameSessionManager`.

---

## 5. LeaderboardService

**Purpose**: Manages player rankings and leaderboards based on game performance. Lets for now, only focus on SBMM. No need to have SBMM and regular matchmaking.

### Responsibilities:
- **Ranking Calculations**: Updates player rankings based on the outcome of each match.
- **Leaderboard Display**: Provides a list of top players and their rankings for display in the game lobby or profile sections.
- **Data Persistence**: Interacts with `StubDatabase` to store and retrieve ranking data.

### Core Methods:
- `updateRankings(playerID, stats)`: Updates player rankings after a game.
- `getTopPlayers(count)`: Retrieves the top players based on ranking. Super easy by the way. Literally one line of code.
- `fetchPlayerRank(playerID)`: Gets a single player’s rank for display on their profile.

### Interactions:
- Works with `StubDatabase` to store leaderboard data.
- Fetches data for display in user profiles or the main leaderboard interface.

---

## 6. StubDatabase

**Purpose**: Simulates database interactions. Since you’re not using a live database, this will act as the placeholder for user profiles, game history, leaderboard data, etc.

### Responsibilities:
- **Mock Data Management**: Stores mock data for players, game stats, and rankings.
- **Stub Functions for API Simulation**: Provides functions that mimic what would happen in a real database (e.g., storing and retrieving data).

### Core Methods:
- `getPlayerData(playerID)`: Retrieves player profile data.
- `updateLeaderboard(playerID, stats)`: Updates leaderboard stats for a player.
- `fetchGameStats(gameID)`: Retrieves mock stats for a specific game session.

### Interactions:
- Serves as the backend for `LeaderboardService` and `GameSessionManager`.
- Enables the team to simulate data storage and retrieval, even without a real database.

---

## 7. Helper Classes

Purpose: We are going to need multiple classes to help us along the way. Unfortunately, Java doesn’t have structs, so we must resort to using classes. We will have multiple classes (I still don’t know how many) that will look something like this:

```java

/*
This class will store the required player data that we can have for each player. This will be stored in our Database. This class could also be nested within the StubDataBase class. Still not sure if we want to do that.
*/

public class PlayerData {
    // Fields for player data. Ideally this would be a struct in almost any other language :D
    private String playerID;
    private float score;
    private int leaderBoardPosition;
    //Add more fields for whatever we need^^^^
    



    // Add Constructor
}

```
Our StubDataBase class could then use it like this:

```java
public class StubDataBase {

    // Hashmap maping a UUID (unique user ID) to each playerData object
    private Map<String, PlayerData> playerData;

    // TODO: Constructor


    // Methods 
    // public PlayerData getPlayerData(String playerID) { return null; }
    // public void updateLeaderboard(String playerID, int stats) { }
    // public GameStats fetchGameStats(String gameID) { return null; }
}
```

Hopefully you get the point!


---

## 8. *Tentative* System Workflow

Like the title suggests, this workflow will be tentative, this is just how I envision it in my mind. If it will acutally be like this, I have no idea...

### Player Queueing:
- A player joins the matchmaking queue. `MatchmakingService` adds them to the queue and searches for a compatible match.
- When a match is found, `MatchmakingService` notifies `GameSessionManager` to initiate a game session.

### In-Game Interaction:
- During the game, players communicate throgh `ChatManager`, which uses `NetworkManager` to handle real-time messaging.
- Any moves or actions within the game are sent to `GameSessionManager`, which keeps track of the game state.

### End of Game:
- When the game concludes, `GameSessionManager` sends results to `LeaderboardService`.
- `LeaderboardService` updates the rankings in `StubDatabase`, allowing players to see their updated ranks on the leaderboard.

### Data Flow and Communication:
- `NetworkManager` acts as a traffic controller, routing messages between the different services.
- For non-real-time interactions, methods in `StubDatabase` provide placeholder responses that simulate a backend API. Remember, StubDatabase is primaraly going to be used for leaderboard and non-real-time interactions. 





# Notes

- Hannah:

Notes on Network Planning

**Sorry if any of this seems obvious, I got no experience or clue in Networking besides the videos you sent**

`Singleton Patterns`

Problems with singleton patterns that could arise (how should we deal with them?):
**I do agree we should use a singleton pattern because its a network and there's going to be multiple objects trying access the same network data but I'm just jotting some problems that usually arise with singleton patterns. We should rule them out if it doesn't apply or try to center our tests around these issues -> as programmers we try to make our application fail anyways so LOL**
- writing tests can be challenging because all objects in the application widely depend on this one object.
- creating global variables are a problem because the singleton object is global to the entire application
- could run into a race condition -> changing things in the singleton from different places at the same time can cause things to be overwritten or not read properly because they are all trying to access the same information at the same time.
	- CONNECTING TO THE SAME WEBSITE/NETWORK FROM TWO DIFFERENT DEVICES? -> I'm assuming we need to make it so that a certain player ID can only be 	opened one device and not multiple so this doesn't happen
	- MESSAGES BEING MODIFIED? -> ChatManager thing..

`ChatManager & NetworkManager`

Do we want to implement a chat logger for global chat or does the websocket(oh wait we aren't using this thin lol nvm) just log it automatically? -> if it doesn't, we can use a singleton pattern here.
I found this video all about singleton patterns and the example he goes through is a logger for files, messages might be somewhat similar because we aren't actually creating a database?
https://youtu.be/sJ-c3BA-Ypo

Players are being brought to a server that is shared for the setup? Or will they have their local setup and it's transferring data from client to the server and back? Sorry I little confused LOL, I'm assuming its the last bit because that's how the API stuff worked but just clarifying..

Core Methods we might want to consider for ChatManager which then would have to interact with NetworkManager:
- deleteMessage() : are we gonna have it so that the delete will delete it from the other observers (objects) as well? or just from your side? ik some apps have it that way with I hate but
- editMessage() : maybe not.. cause that's not how video games usually work but we could if u want
- updateMessage() : goes with editMessage, idk if you want to make it a separate method or not

I guess we could map messages to an ID and if we do we should keep a limit to how many messages there are. Kind of like a logger in global chat where it cuts off at a certain # and starts deleting the earliest messages. Might wanna do that so running time is good, no lag or smthing. (Since we aren't actually implementing a database, are we just gonna make a stub or smthing for this...

`MatchmakingService`
Ok :thumbsup: 

we need a method that checks skill level (idk how SBBM works specifically but does it do this automatically?), if it's just queue, its gonna pair the first two in, and so on.
- matchRating() or we can implement this logic in findMatch() : selects the first player in the queue and goes through the queue to find the a player who has a rating within a certain range of this player's rating. If we're doing ranks like said before (diamond, platinum, gold, silver, etc) maybe within 1 rank difference (so if player 1 is gold, the possible pairs would be silver or platinum). If we do this rank thing, we probably need a rank rating (RR idk if u guys are familiar with how riot does it) from 0-100 - so if player 1 wins, they get a certain amount of RR from that game with adds to their original RR. Once the RR exceeds 100, they rank up and start from 0 again. 

- we should def communicate with everyone else on this and see what they wanna do because we can also not do ranks and just have a rank rating that just goes up like in chess. a higher rating number means more skilled and then just make a certain range from that for matchMaking


`LeaderBoardService`

Are we using like a map or is it gonna be like an array, list?
Maybe we should use an arraylist or map
 
`Some Questions I have`
- Are we going to implement a friend's list? or nah
- if we do, are we going to implement private matches?