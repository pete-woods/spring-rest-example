# Spring Boot REST example

A simple REST API implemented using Spring Boot on-top of Redis and MariaDB, using Google OIDC for
authentication.

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
