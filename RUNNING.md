## How to run 

There are multiple ways to run our program. The command provided in the assignment is valid:
 `java -classpath "gameworldapi.jar;impl.jar;client.jar" client.main.package.ClientMainClass impl.root.package.ImplRootClass`
 However, as it is quite verbose, we have added a shorter option as well:
 `java -jar client.jar impl`

Below are a few examples of valid commands:
```
java -classpath "gameworldapi.jar;robotgame.jar;simplegameapp.jar" simplegameapp.MyCanvasWindow robotgame.Type
java -classpath "gameworldapi.jar;mygame.jar;blockr.jar" blockr.MyCanvasWindow mygame.Type
java -jar blockr.jar robotgame
java -jar simplegameapp.jar mygame