# game-of-three-player (CLIENT)

The project behaves as a client that consumes messages from REDIS broker and send a request with the player next move 
on its payload to the server, so first start game-of-three(server) and then the listeners would be able to connect to REDIS channels 
and process the messages. 


## Information:

- You must have JAVA 8 or more and Docker installed on your machine.
- The application uses gradle to manage dependencies and build.

## Configurable properties

- game.player.number [integer] - The player number, it must be 1 or 2. [default 1]
- game.play.auto [boolean] - Specifies if the players added score has to be from input keybord or selected automatically. [default true]
- game.maxscore [integer] - The max score allowed to be selected randomly on game start. [default 1000]
- game.keep.play [boolean] - Always a game finishes it calls the server to start a new one each 20s. [default true]

## 1 - Dependencies setup

You can open or import the project into an IDE, the project was created with Intellij, using it, go on "file -> open"
and select the directory of this project, it will download the required dependencies and plugins easily and quickly.
 

## 2 - Build and enter into executable directory

- The .jar files are generated in repo-game-of-three/bin or ../bin

```
$   ./gradlew build; cd ../bin
```

## 3 - Initializing player 1

```
$   java -jar -Dgame.player.number=1 game-of-three-player-0.0.1-SNAPSHOT.jar
```

## 4 - Initializing player 2 (Open on different terminal)

```
$   java -jar -Dgame.player.number=2 game-of-three-player-0.0.1-SNAPSHOT.jar
```



