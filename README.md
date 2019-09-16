# game-of-three

Game where to player .....


## Information:

** Warning **: 

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

## 2 - Running the project

```
$   cd {PROJECT_DIRECTORY}
$   ./gradlew bootRun
```

