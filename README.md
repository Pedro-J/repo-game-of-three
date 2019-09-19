## Information:

- You must have JAVA 8 or more and Docker installed on your machine.
- An application uses gradle to manage dependencies and build.
- The data is stored on a memory database, so it is not necessary to download any database.
- The project uses docker to download and start the redis into a container, it is used as the message broker.
- The application's tomcat runs on port 8080 and redis on 6379, those ports must be available.
 
If you don't have docker follow the link: 

windows -> https://docs.docker.com/docker-for-windows/install/ 

linux -> https://docs.docker.com/install/linux/docker-ce/ubuntu/.

# Implementation Details

This implementation of the game of three consists of two players exchanging messages each other. The first player generates a random score and 
start the game by sending an HTTP request to the server, the second also need to send a start request in order to start the game, 
when this happens the server sends a message to a REDIS channel where the second player process this message adding one of three 
values [1, 0, -1], divide the result by 3 and send a new request to the server with these data, so the server passes it 
to the first perform the same processing and the players keep exchanging these messages util the score gets 1, so the game finishes,
and the player who did the last division win the game. But whether a player finishes with 2 which is not divisible by three, the implementation 
consider a game with no winner.


# game-of-three (SERVER)

The server stores all data of games, players and its moves. It helps to orchestrate the communication between players moves. 
It's also responsible to print all the game status and moves sent between both players.


# game-of-three-player (CLIENT)

The project behaves as a client that consumes messages from REDIS broker and send a request with the player next move 
on its payload to the server, so first start game-of-three(server) and then the listeners would be able to connect to REDIS channels 
and process the messages.

# 1 - How to run the application

You can run the two .jar on the /bin directory using the following commands, but you can also build the jars on your own IDE
and run them. To do this, go to the /game-of-three and game-of-three-player directory and follow the instructions.

# 2 - Runnning the server

- This application uses tomcat that runs on port 8080 and redis that runs on 6379, those ports must be available.

```

$   docker run --name redis-message-broker -d -p 6379:6379 redis
$   cd {PROJECT_DIRECTORY}/bin
$   java -jar game-of-three-0.0.1-SNAPSHOT.jar

```

# 3 Running the clients

You must be on {PROJECT_DIRECTORY}/bin directory and must execute each player in a different terminals.

```
$   cd {PROJECT_DIRECTORY}/bin
```

# 3.1 - Running the first client (player1)

```
$   java -jar -Dgame.player.number=1 game-of-three-player-0.0.1-SNAPSHOT.jar
```

# 3.1 - Running the second client (player2) 

```
$   java -jar -Dgame.player.number=2 game-of-three-player-0.0.1-SNAPSHOT.jar

```

## Configurable properties

- game.player.number [integer] - The player number, it must be 1 or 2. [default 1]
- game.play.auto [boolean] - Specifies if the players added score has to be from input keybord or selected automatically. [default true]
- game.maxscore [integer] - The max score allowed to be selected randomly on game start (min: 100 | max: maxScore). [default 1000]
- game.keep.play [boolean] - Always a game finishes it calls the server to start a new one after 20s. [default true]
