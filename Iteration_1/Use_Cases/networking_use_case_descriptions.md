# Use Cases Descriptions for Network

## **Use Case**: Establish connection

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

**When Available**: Any time a player starts the game

**Frequency of use**: Every time a player initiates a connection or re-connects

**Channel to Actor**: GUI 

**Secondary Actor**: Network (mostly `NetworkManager`)

**Channels to Secondary Actor**: Stub? (realistically an API call)

**Open Issues**: 
- Say a player keeps having to reconnect, should there be a timer to stage intervals?


## **Use Case**: Send Message

**Actor**: Player

**Goal in Context**:

**Preconditions**:

**Trigger**:
**Scenario**:

**Post Conditions**:


**Exceptions**:


**Priority**:

**When Available**:

**Frequency of use**:
**Channel to Actor**:

**Secondary Actor**:

**Channels to Secondary Actor**:

**Open Issues**:


## **Use Case**: Queue Entry

**Actor**: Player

**Goal in Context**:

**Preconditions**:

**Trigger**:
**Scenario**:

**Post Conditions**:


**Exceptions**:


**Priority**:

**When Available**:

**Frequency of use**:
**Channel to Actor**:

**Secondary Actor**:

**Channels to Secondary Actor**:

**Open Issues**:




## **Use Case**: Status Update - (up in the air about this)

**Actor**: Player

**Goal in Context**:

**Preconditions**:

**Trigger**:
**Scenario**:

**Post Conditions**:


**Exceptions**:


**Priority**:

**When Available**:

**Frequency of use**:
**Channel to Actor**:

**Secondary Actor**:

**Channels to Secondary Actor**:

**Open Issues**:

