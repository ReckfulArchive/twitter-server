# Reckful's Twitter Archive

The backend server for storing and querying the tweets of or related to Byron (Reckful) Bernstein.

The API is available at
[twitter-api.reckful-archive.org](https://twitter-api.reckful-archive.org/)

The archived Twitter timelines and media files are available at
[files.reckful-archive.org/twitter](https://files.reckful-archive.org/twitter/)

# Contributing

To work with the project locally, you will need Java 19. If you don't have it installed,
the easiest way is to use [sdkman](https://sdkman.io/).

## Run from IDE

To run the project locally, you first have to navigate to `server/src/main/resources/application-local.yml` and
change the paths to data files that you can find in the [data](data) directory.

After that, you can run the main function located
in `server/src/main/kotlin/org/reckful/archive/twitter/server/Application.kt`
with
the `local` [Spring profile](https://docs.spring.io/spring-boot/docs/3.0.6/reference/html/features.html#features.profiles),
which can be set in IntelliJ's Run Configuration.

The API will be available locally at [localhost:8080](http://localhost:8080/swagger-ui/index.html)

## Build and run

Running `./gradlew build` should produce a runnable jar under `server/build/libs/server-1.0-SNAPSHOT.jar`.

The jar can be started via `java -jar server-1.0-SNAPSHOT.jar`:

```bash
java -Dspring.profiles.active=local -jar server-1.0-SNAPSHOT.jar
```

If you need to, correct the settings in `server/src/main/resources/application-local.yml`.

The API will be available locally at [localhost:8080](http://localhost:8080/swagger-ui/index.html)

## Docker

You can use the [Dockerfile](Dockerfile) to build and run the project:

```bash
docker build -t reckful-archive/twitter-server:latest .

docker run -e "JAVA_OPTS=-Dspring.profiles.active=docker" -p 8080:8080 reckful-archive/twitter-server:latest
```

The API will be available locally at [localhost:8080](http://localhost:8080/swagger-ui/index.html)
