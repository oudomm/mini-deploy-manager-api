# Use OpenJDK 17 as the base image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the built jar file into the container
COPY build/libs/*.jar app.jar

# Command to run the application
CMD ["java", "-jar", "app.jar"]
