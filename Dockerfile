# Use OpenJDK with Gradle pre-installed
FROM gradle:8.4-jdk21 AS build

# Copy source code
COPY --chown=gradle:gradle . /home/gradle/project
WORKDIR /home/gradle/project

# Build the JAR
RUN gradle build --no-daemon

# ---------------------------
# Runtime image
# ---------------------------
FROM openjdk:21-jdk-slim

WORKDIR /app

# Copy the JAR from the build stage
COPY --from=build /home/gradle/project/build/libs/*.jar app.jar

# Run the app
CMD ["java", "-jar", "app.jar"]
