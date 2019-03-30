# Spring Boot REST example

![Docker Cloud Build Status](https://img.shields.io/docker/cloud/build/petewoods/spring-rest-example.svg)

A simple REST API implemented using a stateless Spring Boot app on-top of Redis and MariaDB,
using Google OIDC for authentication.

## Requirements

- JDK 11+
- Docker

## Building

### Under development

```
docker stack deploy --prune -c docker-compose.yml -c docker-compose-development.yml spring-rest-example
```

Then run the spring boot app through your IDE, or with the command:
```
./mvnw spring-boot:run
```

## Packaging
To build the Docker image:
```
docker build -t petewoods/spring-rest-example .
```

Then it can be deployed as follows (the brackets use a BASH sub-shell):
```
(. production.env && docker stack deploy --prune -c docker-compose.yml -c docker-compose-production.yml spring-rest-example)
```
