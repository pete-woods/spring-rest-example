# Spring Boot REST example

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
docker build -t surevine/spring-rest-example .
```

Then it can be deployed as follows (the brackets use a BASH sub-shell):
```
(. production.env && docker stack deploy --prune -c docker-compose.yml -c docker-compose-production.yml spring-rest-example)
```

## Licensing

This project is freely redistributable under a "new-style BSD"
licence.  See the file COPYING for more details.  In short, you may
modify and redistribute the SDK and example plugins within any
commercial or non-commercial, proprietary or open-source plugin or
application under almost any conditions, with no obligation to provide
source code, provided you retain the original copyright note.
