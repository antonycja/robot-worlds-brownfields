# Use Maven image for building the application
FROM maven:3.8.5-openjdk-17-slim AS build

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml and any other files needed for dependency resolution
COPY pom.xml /app/

# Copy the lib directory if there are external JAR dependencies
COPY libs /app/libs

# Copy the source code to the container
COPY src /app/src

# Package the application using Maven
RUN mvn clean package -DskipTests

# Debug: List contents of /app/target
RUN ls -l /app/target

# Use an official OpenJDK image as the runtime environment
FROM openjdk:17-jdk-slim

# Set the maintainer label
LABEL maintainer="Tech Team <tech-team@wethinkcode.co.za>"

# Set the working directory in the container
WORKDIR /app

# Assuming /app is the working directory in Docker
COPY src/main/java/robot_worlds_13/server/configuration/file.txt /app/configuration/file.txt

# Copy the JAR with dependencies from the libs directory to the runtime environment
COPY libs/robot-worlds-server-jar-with-dependencies.jar /app/robot_worlds_13.jar

# Set the entry point to run the JAR file

ENTRYPOINT ["java", "-jar", "robot_worlds_13.jar"]


# Expose the port the application runs on
EXPOSE 5001
