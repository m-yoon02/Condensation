# Network Use Case Descriptions (Ethan)
note: put into pdf when all use cases are done

Use Case: Initialise Connection

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

Use Case: Maintain Connection

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
