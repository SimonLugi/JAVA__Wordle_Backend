# Use a lightweight JDK base image
FROM eclipse-temurin:21-jre-alpine

# Set the working directory
WORKDIR /app

# Copy the jar file
COPY target/Wordle-0.0.1-SNAPSHOT.jar app.jar

# Expose application port (default 8080)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
