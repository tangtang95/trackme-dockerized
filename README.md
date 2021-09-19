# Docker version of TrackMe project

This repository contains a docker version of an old university project, [TrackMe](https://github.com/tangtang95/TrackMe),
 about microservices with spring framework. Using a framework composed by service registry, API gateway, and a bunch of microservices.
## Requirements

- **Docker**
- **Docker Compose v3**

## Instructions

- Export your external server hostname into the environment variable name **SERVER_EXT_ADDRESS**
- Make sure to forward the port **8443** of the API gateway if necessary
- Make sure in-bound request to port **8443** into your server is allowed
- Launch the application with **docker-compose up**. Optionally, it is possible to add option **--SCALE servicename=n_instances** to launch multiple containers for that specific microservice