# Build stage
FROM gradle:8.4-jdk21 AS builder

WORKDIR /app

# Copy gradle wrapper and build files
COPY ./gradlew ./gradlew
COPY ./gradlew.bat ./gradlew.bat
COPY gradle ./gradle
COPY build.gradle ./build.gradle
COPY settings.gradle ./settings.gradle

# Copy source code
COPY src ./src

# Build the application
RUN ./gradlew build

# Runtime stage
FROM openjdk:21-slim

WORKDIR /app

# Copy the JAR from builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

