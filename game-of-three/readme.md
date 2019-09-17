# game-of-three (SERVER)

The server stores all data of games, players and its moves. It helps to orchestrate the communication between players moves. 
It's also responsible to print all the game status and moves sent between both players.


## Information:

- You must have JAVA 8 or more and Docker installed on your machine.
- An application uses gradle to manage dependencies and build.
- The data is stored on a memory database, so it is not necessary to download any database.
- The project uses docker to download and start the redis into a container, it is used as the message broker. 
If you don't have docker follow the link: 

windows -> https://docs.docker.com/docker-for-windows/install/ 

linux -> https://docs.docker.com/install/linux/docker-ce/ubuntu/.

## 1 - Dependencies setup

You can open or import the project into an IDE, the project was created with Intellij, using it, go on "file -> open"
and select the directory of this project, it will download the required dependencies and plugins easily and quickly.

## 2 - Running Redis 

```
$   docker run --name redis-message-broker -d -p 6379:6379 redis

```

## 3 - Build and run the project in .jar

```
$   cd {PROJECT_DIRECTORY}
$   ./gradlew build; && cd build/libs
$   java -jar game-of-three-0.0.1-SNAPSHOT.jar
$
```

## Running the project directly (optional)

```
$   cd {PROJECT_DIRECTORY}
$   ./gradlew bootRun
```

