**Comparator Method**

Currently, the team uses rank in an enum to denote the different status of players. 
However, the leaderboard handler does not have a way to rank these players other than to use rank. 
The team can use other statistics such as moves or games played to order these players. The comparator can take these 
inputs and order these players accordingly creating a complex way to show how you stack up against other players 
instead of just using a set enum constant. 

**Reasons to implement**:
1. **Complex user interaction**: Comparator allows the leaderboard to organize players based on different criteria,
and even based on various factors combined. This allows for richer and personalized view of their statistics
and possible ways to improve gameplay.

2. **Diverse audience:** Allows for better strategies that the user can aim for such as speed runs or using as little movements as possible.
This opens up for greater possibilities and allows the application to cater to various audience groups that 
may be interested in.

3. **Play testing**: Data can enable users and developers to see how to add challenges to the game and weaknesses that may lie within the
game. Developers can then use this data to add updates to the game logic and/or balance certain player behaviour
to ensure game is challenging and entertaining. 

**Debug/Console Log**

A console log is beneficial for debugging since it can identify problems within the application. Each user inputs can 
have a message that is written to a log file and can be used by devs to improve the system. Additionally, it is useful to 
identify program crashes that may result from the GUI not initializing components properly. In principle these logs are 
written to the system output in the IDE, however can be saved to a file that is overwritten each time program initializes.
