# Spring Boot REST example

## Requirements

- JDK 8+
- Docker

## Building

### Under development

```
docker-compose -f docker-compose.yml -f docker-compose-development.yml up
```

Then run the spring boot app through your IDE, or with the command:
```
./mvnw spring-boot:run
```

## Packaging
To build the Docker image:
```
./mvnw package
```

Then it can be deployed as follows:
```
docker-compose -f docker-compose.yml -f docker-compose-production.yml up
```

## Licensing

This project is freely redistributable under a "new-style BSD"
licence.  See the file COPYING for more details.  In short, you may
modify and redistribute the SDK and example plugins within any
commercial or non-commercial, proprietary or open-source plugin or
application under almost any conditions, with no obligation to provide
source code, provided you retain the original copyright note.
