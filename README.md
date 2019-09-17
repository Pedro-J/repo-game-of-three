# Implementation Details

This implementation of the game of three consists of two players exchanging messages each other. The first player generates a random score and 
start the game by sending an HTTP request to the server, the second also need to send a start request in order to start the game, 
when this happens the server sends a message to a REDIS channel where the second player process this message adding one of three 
values [1, 0, -1], divide the result by 3 and send a new request to the server with these data, so the server passes it 
to the first perform the same processing and the players keep exchanging these messages util the score gets 1, so the game finishes,
and the player who did the last division win the game. 


# 1 - game-of-three (SERVER)

The server stores all data of games, players and its moves. It helps to orchestrate the communication between players moves. 
It's also responsible to print all the game status and moves sent between both players.


# 2 - game-of-three-player (CLIENT)

The project behaves as a client that consumes messages from REDIS broker and send a request with the player next move 
on its payload to the server, so first start game-of-three(server) and then the listeners would be able to connect to REDIS channels 
and process the messages. 
