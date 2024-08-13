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

# Copy the built jar file from the build stage
COPY --from=build /app/target/robot_worlds_13-1.0.jar /app/robot_worlds_13-1.0.jar

# Set the entry point to run the JAR file
ENTRYPOINT ["java", "-jar", "robot_worlds_dbn_13-1.0.jar"]

# Expose the port the application runs on
EXPOSE 5001
