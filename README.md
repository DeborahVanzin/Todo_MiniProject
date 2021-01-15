# ToDo Server MiniProject

Lorenzo Antelmi & Deborah Vanzin

## Overview

ToDo Server MiniProject is a simple implementation of a server, that is able to handle and manage the ToDos of multiple users. It also suports registration, login and logout of users.

## Basic features

- Ability to serve multiple clients in parallel (like a web server)
- Implements all of the API commands
- The server listens on port 50002

## Additional extra features
- ToDo entries support due dates
- complete data validation (password requirements, validity of all requests etc.)
- tokens are standard 128-bit UUIDs
- passwords are stored hashed with SHA-256 algorithm
- server stores users and their ToDo entries in a file, so that database persists in-between server restarts
- logout is performed with token

# ToDo Client MiniProject

There is a simple client application written in JavaFX, that simply connects to a server and allows users to send a command to it. The raw output of the command is then displayed.
