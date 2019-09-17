# game-of-three-player (CLIENT)

The project behaves as a client that consumes messages from REDIS broker and send a request with the player next move 
on its payload to the server, so first start game-of-three(server) and then the listeners would be able to connect to REDIS channels 
and process the messages. 


## Information:

- You must have JAVA 8 or more and Docker installed on your machine.
- The application uses gradle to manage dependencies and build.

## Configurable properties

- game.player.number [integer] - The player number, it must be 1 or 2.
- game.play.auto [boolean] - Specifies if the players added score has to be from input keybord or selected automatically.
- game.maxscore [integer] - The max score allowed to be selected randomly
- game.start [boolean] - Send a request to the server to start the game

## 1 - Dependencies setup

You can open or import the project into an IDE, the project was created with Intellij, using it, go on "file -> open"
and select the directory of this project, it will download the required dependencies and plugins easily and quickly.
 

## 2 - Build and enter into executable directory

```
$   cd {PROJECT_DIRECTORY}
$   ./gradlew build; && cd build/libs
$
```

## 3 - Initializing player 1

```
$   java -jar -Dgame.play.auto=false -Dgame.player.number=1 -Dgame.start=true game-of-three-player-0.0.1-SNAPSHOT.jar
```

## 4 - Initializing player 2 (Open on different terminal)

```
$   java -jar -Dgame.play.auto=false -Dgame.player.number=2 -Dgame.start=true game-of-three-player-0.0.1-SNAPSHOT.jar
```



