# Build your Own docker Backend

### Requirements

- Java 21 (JDK)
- Maven 3.9+
- Springboot backend with SQL or similar
- Docker

## Table of contents

1. [Preparing](#step-1-preparing)
2. [Updating Dependencies](#step-2-updating-dependencies) <br>
   <b>2.1</b> [Compiler Configuration](#step-21-compiler-configuration) <br>
   2.1 [Maven Guide](#maven) <br>
   2.1 [Gradle Guide](#gradle) <br>
   <b>2.2</b> [SQL Storing](#step-22-sql-initialisation-scripts)
3. [Configuring Springboot](#step-3-configuring-springboot-to-use-the-flyway-data-migration) <br>
   3.1 [Add Flyway Configs](#step-31-add-flyways-configs)<br>
   3.2 [Springboot ReConfiguration](#step-32-changing-some-things-on-springboots-configs)
4. [Building the Backend](#step-4-building-the-backend)<br>
   4.1 [Maven Compiling](#maven-compiling) <br>
   4.1 [Gradle Compiling](#gradle-compiling)
5. [Dockerizing](#step-5-dockerizing) <br>
    5.1 [Configuring the docker Builder](#step-51-configuring-the-docker-builder) <br>
    5.2 [Building the image](#step-52-building-the-image) <br>
6. [Deploy the Backend](#step-6-deploy-the-backend) <br>
    6.1 [Writing the docker-compose.yml](#step-61-writing-the-docker-composeyml) <br>
    6.2 [Deploy](#step-62-deploy)
7. [Finish](#step-7-finish)

___

## Step 1 Preparing

Add following files to the root of your Project

- .env
- compose.yaml
- Dockerfile

## Step 2 Updating Dependencies:

In this step we will Configure your Backend to run and creates its own Database

### Step 2.1 Compiler Configuration:

Locate and Open your pom.yml if you use as Compiler Maven otherwise if you are using Gradle do the same with your
build.gradle

### Maven Guide

<details>
  <summary>Maven Pom.xml Configuration </summary>

#### Maven:

Add under ```<dependencies>``` following lines:

```xml
        <!-- https://mvnrepository.com/artifact/org.flywaydb/flyway-maven-plugin -->
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-maven-plugin</artifactId>
    <version>11.17.0</version>
</dependency>
        <!-- https://mvnrepository.com/artifact/org.flywaydb/flyway-core -->
<dependency>
<groupId>org.flywaydb</groupId>
<artifactId>flyway-core</artifactId>
<version>11.17.0</version>
</dependency>
```

Now we added the default Flyway this is currently not capable of interacting with Ouer Database
to make this now work we need to configure it further
this step depends now a bit on your architecture and what Database type you use:

If you use a SQL DB also make sure that u use the ```mysql-connector-j``` connector and not the
```mysql-connector-java```

For Mariadb and MySql you can use: ```<artifactId>flyway-mysql</artifactId>```

if you uses PostgresSQL you need to use: ```<artifactId>flyway-database-postgresql</artifactId>```

if you use eany other Database find your self the right artifact [Here](https://mvnrepository.com/search?q=flyway)

Now adding this to your pom.xml also under the dependencies:

```xml

<dependency>
    <groupId>org.flywaydb</groupId>
    <!-- Your Artifact here -->
    <version>11.17.0</version>
</dependency>
```

</details>

### Gradle Guide

<details>
    <summary>Gradle build.gradle Configuration</summary>

#### Gradle:

Add under ```dependencies {}``` following lines:

```yaml
implementation("org.flywaydb:flyway-core:11.17.0")
runtimeOnly("org.flywaydb:flyway-gradle-plugin:11.17.0")
```

Now we added the default Flyway this is currently not capable of interacting with Ouer Database
to make this now work we need to configure it further
this step depends now a bit on your architecture and what Database type you use:

If you use a SQL DB also make sure that u use the ```mysql-connector-j``` connector and not the
```mysql-connector-java```

For Mariadb and MySql you can use: ```implementation("org.flywaydb:flyway-mysql:11.17.0")```

if you uses PostgresSQL you need to use: ```implementation("org.flywaydb:flyway-database-postgresql:11.17.0")```

if you use eany other Database find your self the right artifact [Here](https://mvnrepository.com/search?q=flyway)

Now adding this to your build.gradle also again under the dependencies

</details>

### Step 2.2 SQL initialisation scripts:

> <picture>
>   <source media="(prefers-color-scheme: light)" srcset="https://raw.githubusercontent.com/Mqxx/GitHub-Markdown/main/blockquotes/badge/light-theme/note.svg">
>   <img alt="Note" src="https://raw.githubusercontent.com/Mqxx/GitHub-Markdown/main/blockquotes/badge/dark-theme/note.svg">
> </picture><br>
> In this step I assume you use a Database of the SQL Family.

Now navigate to ```src/main/resources``` in there create a folder set ```db.migration```

if you created them you can put your SQL database initialization scripts in there important is that tey are named like
this:

V1__init.SQL
V2__AlterTable.SQL

So the important thing is that you do version them (```V1__```) etc. the name behind the __ can be set to whatever you
want but no White spaces.

## Step 3 Configuring Springboot to use the Flyway data Migration:

In this Step we will add the Flyway configs and reconfigure spring to work in a Containerized setup

### Step 3.1 Add flyways Configs:

For this you open again the resources folder (```src/main/resources```).
You should find next to your db.migration folders the ```application.properties```

Now you can open it and add following lines:

```properties
# Flyway
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true
spring.flyway.connect-retries-interval=120s
spring.flyway.locations=classpath:db/migration
spring.flyway.user=${SQL_USER:root}
spring.flyway.password=${SQL_PASSWORD:Wordle12345}
spring.flyway.schemas=${SQL_DB:WordleDB}
```

The Password and Schemas can be changed (Behind the ":" )

### Step 3.2 Changing some things on Springboots configs:

> <picture>
>   <source media="(prefers-color-scheme: light)" srcset="https://raw.githubusercontent.com/Mqxx/GitHub-Markdown/main/blockquotes/badge/light-theme/note.svg">
>   <img alt="Note" src="https://raw.githubusercontent.com/Mqxx/GitHub-Markdown/main/blockquotes/badge/dark-theme/note.svg">
> </picture><br>
> AN environment variable is defined like this ${MYVAR_SPECIFIFING:fallbackValue}
>
> To know is that the env Variabels are written in Upper case


> <picture>
>   <source media="(prefers-color-scheme: light)" srcset="https://raw.githubusercontent.com/Mqxx/GitHub-Markdown/main/blockquotes/badge/light-theme/info.svg">
>   <img alt="Note" src="https://raw.githubusercontent.com/Mqxx/GitHub-Markdown/main/blockquotes/badge/dark-theme/info.svg">
> </picture><br>
>
> You will need to hardcode your root credentials in to the fallback of the env vars becaus spring is when it trys to pars the var not in as state to load the vars from Docker


Also in the ```application.properties``` you need to change following things:

<details>
    <summary>Datasource Url</summary>

The first thing is the Connector (JDBC) the next part is the Database type (mysql),
next you will find your host probably "localhost" change this to a var like "SQL_HOST"

```properties
spring.datasource.url=jdbc:mysql://${SQL_HOST:localhost}:${SQL_PORT:3306}/${SQL_DB:yourDatabaseName}?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC&createDatabaseIfNotExist=true

spring.datasource.username=${SQL_USER:mySqlUser}
spring.datasource.password=${SQL_PASSWORD:mySqlPassword}
```

</details>

<details>
    <summary>Spring datasource Configurations</summary>

Change your springboot and hibernate settings to the following values / configuration:


```properties
# Database Configuration
# Change to 'update' if you want automatic schema management Disable because Flyway will do it
spring.jpa.hibernate.ddl-auto=validate
spring.sql.init.mode=never
spring.jpa.properties.hibernate.boot.allow_jdbc_metadata_access=false
spring.jpa.properties.hibernate.temp.use_jdbc_metadata_defaults=false
# Variable to your selected Database Example is designed for MySQL 9+
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
hibernate.dialect.storage_engine=innodb
spring.jpa.driverClassName=com.mysql.cj.jdbc.Driver
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

you may need to add this this line to the test/resources/application.properties
```properties
spring.flyway.enabled=false
```
</details>

# Step 4 Building the Backend:

> <picture>
>   <source media="(prefers-color-scheme: light)" srcset="https://raw.githubusercontent.com/Mqxx/GitHub-Markdown/main/blockquotes/badge/light-theme/note.svg">
>   <img alt="Note" src="https://raw.githubusercontent.com/Mqxx/GitHub-Markdown/main/blockquotes/badge/dark-theme/note.svg">
> </picture><br>
>
> Make sure that you have in your Application.java the following lines
> ```java
> @EnableJpaRepositories(basePackages =  {"net.ict_campus.Wordle.domain.player","net.ict_campus.Wordle.domain.word"}) //<-- Your reposetory location
> @EntityScan(basePackages = "net.ict_campus.Wordle.domain") //<-- Your package
> public class WordleApplication {} // <-- Main class
> ```
> If you have a nother folder structur you may need to define it like this:
> ```java 
> @EntityScan(basePackages = "net.ict_campus.*")
> ```

To compile your Application to a Jar please follow the guide for your Compiler.

### Maven Compiling

<details>
    <summary>Maven Guide</summary>

To compile it with maven run the following command in your cli:

```shell
mvn clean compile package -f pom.xml
```

</details>

### Gradle Compiling

<details>
    <summary>Gradle Guide</summary>
To compile it with gradle run the following command in your cli:

```shell
./gradlew clean build
```

</details>

# Step 5 Dockerizing

Locating your Jar file

under Maven, it is Located at target/Example.jar <br>
unser Gradle you will find it at build/libs/Example.jar

> <picture>
>   <source media="(prefers-color-scheme: light)" srcset="https://raw.githubusercontent.com/Mqxx/GitHub-Markdown/main/blockquotes/badge/light-theme/info.svg">
>   <img alt="Info" src="https://raw.githubusercontent.com/Mqxx/GitHub-Markdown/main/blockquotes/badge/dark-theme/info.svg">
> </picture><br>
>
> What is a [Dockerfile ?](https://docs.docker.com/build/concepts/dockerfile/)

## Step 5.1 Configuring the docker Builder:

After finding your Jar you can continue by opening the Dockerfile that we created in the first step

now you can add following lines:

```dockerfile
# Use a lightweight JDK base image
FROM eclipse-temurin:21-jre-alpine

# Set the working directory (for the image)
WORKDIR /app

#Copies the Jar file from your target dir to the docker image and renaming it
COPY target/Example.jar app.jar

# Expose application port (default 8080)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
```

## Step 5.2 Building the image:

Now its time to make this a universal container that can run eanywhere without the needs of extra librarys and
installation beside a Container run time

For this you will need to run the docker build command:

```shell
docker buildx build --platform linux/amd64,linux/arm64 \
  -t YOUR_DOCKER_USERNAME/YOUR_DOCKER_IMAGE_NAME:latest \
  -t YOUR_DOCKER_USERNAME/YOUR_DOCKER_IMAGE_NAME:1.0.1 \
  --push .
```

# Step 6 Deploy the Backend

Now to run our Backend we need to write the docker-compose.yml
A docker-compose tells docker how to deploy the images on to the Docker runtime

> <picture>
>   <source media="(prefers-color-scheme: light)" srcset="https://raw.githubusercontent.com/Mqxx/GitHub-Markdown/main/blockquotes/badge/light-theme/info.svg">
>   <img alt="Info" src="https://raw.githubusercontent.com/Mqxx/GitHub-Markdown/main/blockquotes/badge/dark-theme/info.svg">
> </picture><br>
>
> What is a [docker-compose.yml ?](https://docs.docker.com/compose/)

## Step 6.1 Writing the docker-compose.yml

Open the docker-compose.yml and paste this in:

<details>
    <summary>Code</summary>

```yaml
services:
  database:
    container_name: your-database
    image: mysql:9.5
    restart: unless-stopped
    environment:
      MYSQL_ROOT_PASSWORD: ${SQL_ROOT_PASSWORD}
      MYSQL_DATABASE: ${SQL_DB}
      MYSQL_USER: ${SQL_USER}
      MYSQL_PASSWORD: ${SQL_PASSWORD}
    ports:
      - "3308:3306"
    volumes:
      - ./mysql_data:/var/lib/mysql
    healthcheck:
      test: [ "CMD", "mysqladmin", "ping", "-h", "localhost", "-u", "${DB_USER}", "-p${DB_PASSWORD}" ]
      interval: 5s
      timeout: 5s
      retries: 5
    networks:
      wordleNet:
        ipv4_address: 172.38.0.2


  backend:
    container_name: your-application-backend
    image: YOUR_DOCKER_USERNAME/YOUR_DOCKER_IMAGE_NAME:latest
    restart: unless-stopped
    environment:
      SQL_ROOT_USER: ${SQL_ROOT_USER}
      SQL_ROOT_PASSWORD: ${SQL_ROOT_PASSWORD}
      SQL_USER: ${SQL_USER}
      SQL_PASSWORD: ${SQL_PASSWORD}
      SQL_DB: ${SQL_DB}
      SQL_HOST: database
      SQL_PORT: 3306
    depends_on:
      wordle-database:
        condition: service_healthy
    ports:
      - "8081:8080"
    networks:
      wordleNet:
        ipv4_address: 172.38.0.3

networks:
  wordleNet:
    driver: bridge
    ipam:
      config:
        - subnet: 172.38.0.0/16
```
</details>

Now there's only one thing left you need to change the image name and
your username to your setting.


## Step 6.2 Deploy

If you have changed them you are ready to Deploy:
```shell
docker compose up
```

# Step 7 Finish

Last but not least you can now open in your browser the url to your backend:
--> [localhost:8080](http://localhost:8080)

