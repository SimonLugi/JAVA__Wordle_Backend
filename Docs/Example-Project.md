

## Quick start: build the Wordle backend with Docker

## Build the application JAR
Prerequisite:

```shell
mvn clean compile package -f pom.xml
```

The build creates `target/Wordle-0.0.1-SNAPSHOT.jar`.

Run it locally:

```shell
.\start.ps1
```


### Run the backend in Docker
First, ensure the JAR exists (see the build step above). Then build the image (PowerShell line breaks shown with `^`):

```shell
docker buildx build --platform linux/amd64,linux/arm64 ^
  -t YOUR_DOCKER_USERNAME/wordle-backend:latest ^
  -t YOUR_DOCKER_USERNAME/wordle-backend:1.0.1 ^
  --push .
```


### Configuration
The backend reads database settings from environment variables (with sensible defaults):
- `SQL_HOST` – database host (default `localhost`)
- `SQL_PORT` – database port (default `3306` for MySQL)
- `SQL_DB` – database name (e.g., `WordleDB`)
- `SQL_USER` – database user (e.g., `Wordle`)
- `SQL_PASSWORD` – database password (e.g., `Wordle12345`)

Note: The application is configured for MySQL. When using the Docker Compose file in this repository, the defaults above should work out of the box.

### Running the Application
Now you can go to the docker directory in there you will find the .env there you can set your db Credentials

Then you can open the docker-compose.yml and change at line 27 the Username to your Docker hub username
it should look something like this:
```yaml
    image: themodcrafttmc/wordle-backend:latest
```

After this you can go ahead and run the commands:
```shell
cd docker

docker compose up
```

### Endpoints and API docs
If Springdoc OpenAPI is enabled, Swagger UI is available at:
- `http://localhost:8080/swagger-ui.html` or
- `http://localhost:8080/swagger-ui/index.html`


### Troubleshooting
- Port 8080 already in use: stop the other application or start this one on a different port using `-Dserver.port=8081`.
- Can’t connect to the database: ensure MySQL is running (`docker compose ps`), credentials match, and the port mapping (3307 on host -> 3306 in container) is correct.
- Windows PowerShell line breaks: use `^` as shown above, or place the command on a single line.


### Project files of interest
- `docker-compose.yaml` – runs MySQL with the correct schema and credentials (for development).
- `Dockerfile` – minimal runtime image for the backend.
- `src/main/resources/application.properties` – Spring Boot and database configuration.