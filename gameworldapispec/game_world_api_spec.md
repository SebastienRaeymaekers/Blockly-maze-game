# Game world API Specifications

This short document will explain how to properly compose a valid implementation of both a Game World and a Client using this API.

## Valid Game World API implementation

For a world to be valid, it must implement both the `GameWorldType` and `GameWorld` interfaces provided in the API. A valid Game World thus has at least 2 classes, each implementing one of these interfaces respectively. 

### The GameWorldType interface
This interface consists of the following methods:
* `public ArrayList<String> getActions()`  Return an ArrayList of Actions (represented as Strings) which are supported by this GameWorld. Note that it is possible to receive execute commands for actions which are not supported by this GameWorld.
* `public ArrayList<String> getPredicates()`  Return an ArrayList of Predicates (represented as Strings) which are supported by this GameWorld.
* `public <T extend GameWorld> T createWorld(int[] bounds)` Create a new GameWorld. The `bounds` parameter determines the region this GameWorld controls. 

### The GameWorld interface
This interface consists of the following methods:
* `ExecuteResult execute(String action)` Execute a given action. This action might not be supported by this GameWorld, in which case this function should return "ExecuteResult.Failure". Otherwise, return an enum value containing "ExecuteResult.Success", "ExecuteResult.Failure", or "ExecuteResult.Completed"  depending on the result of the action.
* `boolean eval(String predicate)` Evaluate a given predicate. If this predicate is not supported by this GameWorld, it should return False. Otherwise, return the outcome of the evaluation.   
* `GameWorld getSnapShot()` Return a new GameWorld copy, without any reference to the current GameWorld. 
* `void setSnapShot(GameWorld world)` Replace the current world with the provided GameWorld.
* `void paint(Graphics g)` Paint the current GameWorld. This function should not paint outside of the `bounds` parameter given to this GameWorld upon creation.
* `void loadGame()` Load in a new game from a file. This function can use the `FileToDataReader.loadGameData()` provided in the API to quickly get a HashMap containing keys and values from a given file. This helps to give the same format to all world files over multiple GameWorlds as well.
* `boolean isGameCompleted()` Return True if the game is considered completed in this GameWorld, otherwise return False. 
* `int getMaxBlocks()` Return the amount of blocks that can be used for this world. (Note: in the second iteration this doesn't make as much sense as there are other clients without blocks as well. However, the assignment is worded as if a game is responsible for determining how many blocks can be used, which is why we have left this in the API.)


## Valid Client implementation
A valid Client can do the following:
* Create any amount of GameWorlds
* Obtain a list of Actions and Predicates supported by these GameWorlds by calling the `getActions()` and `getPredicates()` methods respectively.
* Call the `execute()` and `evaluate()` functions using any of the obtained Actions and Predicates. If called using an unsupported String, these will return "Failure" and False.
* Call the `paint()` function of a world to update its graphical representation.
* Call the `getSnapShot()` and `setSnapShot()` functions to save a state and later revert to it.

In short, there are very few responsibilities for a Client. The main responsibility lies within making a valid GameWorld.

