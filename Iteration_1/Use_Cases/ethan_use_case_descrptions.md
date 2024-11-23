# Network Use Case Descriptions (Ethan)
note: put into pdf when all use cases are done

## Use Case: Initialise Connection

Iteration: 1

Primary Actor: Network

Goal in Context: Establish the connection between the player and the server, which allows for access to matchmaking and chat services

Preconditions: The application has been launched and the network manager is ready to accept connections

Trigger: The player starts the connection by logging in to the application

Scenario:
1. Player requests a connection to the network by trying to log in
2. NetworkManager verifies the players log in information
3. If successful NetworkManager creates a connection for the player 
4. Player is notified that the connection was successful, and now has access to online services

Post Conditions:
- There is a stable connection between the network and the player

Exceptions: 
- UserID or password is incorrect
- NetworkManager fails to establish a connection

Priority: Essential

When Available: 1st Iteration

Frequency of Use: Very high

Channel to Actor: User interface, network connection

## Use Case: Maintain Connection

Iteration: 1

Primary Actor: Network

Goal in Context: Ensure the connection between the player and the server remains active and stable for access to online services

Preconditions:
- The player has initially connected to the network successfully
- NetworkManager is actively monitoring the connection

Trigger: The player is actively using the online services within the application

Scenario:
1. Player successfully initiates a connection
2. NetworkManager periodically checks the status of the connection
3. If the connection is stable, NetworkManager continues communication between the player and the network
4. If the connection is unstable NetworkManager attempts to reestablish a stable connection
5. Player will receive a notification if their connection experiences any issues

Post Conditions: 
- The connection remains active and stable


Exceptions:
- Connection is completely lost
- Reconnection fails after several attempts to reconnect
	
Priority: Essential

When Available: 1st iteration

Frequency of Use: Very high

Channel to Actor: User interface, network connection, connection notifications

## Use Case: Manage Network

Iteration: 1

Primary Actor: Network

Goal in Context: To Ensure that all connections are being managed. Which includes initialization, maintenance, and termination of player connections to maintain a stable connection to the network

Preconditions:
- The servers are active, and NetworkManager is functioning
- Online services (Matchmaking, chat, game sessions) are enabled, and accessible to players

Trigger: The player initiates any kind of network activity

Scenario: 
1. The NetworkManager is active and listening for network requests from players
2. A request is received and NetworkManager checks for network availability
3. NetworkManager fulfils the players request
4. NetworkManager continues to monitor for requests and routing to different services

Post Conditions:
- All active connections are monitored, maintained, and closed as needed
- Network resources are efficiently allocated and released
- Players experience minimal interruptions in their connection to network services

Exceptions:
- NetworkManager fails to establish a connection insufficient resources
- NetworkManager encounters unexpected latency during an active session
- Disconnection occurs due to a network error

Priority: Essential

When Available: 1st iteration

Frequency of Use: Very high

Channel to Actor: Network connection, server logs, graphical interface

## Use Case: Session Timeout/Inactive

Iteration: 2
	
Primary Actor: Network

Goal in Context: End inactive sessions to free up network resources

Preconditions: 
- The player is connected to the network and their session has been idle for 10 minutes 
- NetworkManager is functioning

Trigger: The current players session has been idle (no activity) for 10 minutes

Scenario:
1. A players session has been idle for 10 minutes
2. An inactivity warning is displayed to the player
3. If the player remains inactive NetworkManager terminates their session
4. A notification that their session has been terminated is displayed to the player

Post Conditions:
- Player status is updated to offline

Exceptions:
- Player resumes activity during the warning period, cancelling the timeout.
- NetworkManager fails to terminate the session due to a network error.

Priority: Medium

When Available: 2nd iteration

Frequency of Use: Low to medium

Channel to actor: Graphical interface and server logs