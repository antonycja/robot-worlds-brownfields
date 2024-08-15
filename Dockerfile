# Use Ubuntu as the base image
FROM ubuntu:latest AS build

# Set the working directory in the container
WORKDIR /app

# Install required dependencies: OpenJDK, Maven, and SQLite
RUN apt-get update && apt-get upgrade -y && \
    apt-get install -y openjdk-17-jdk maven sqlite3 && \
    apt-get clean

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

# Use Ubuntu as the runtime environment
FROM ubuntu:latest

# Set the maintainer label
LABEL maintainer="Tech Team <tech-team@wethinkcode.co.za>"

# Install OpenJDK and SQLite in the runtime environment
RUN apt-get update && apt-get install -y openjdk-17-jdk sqlite3 && apt-get clean

# Set the working directory in the container
WORKDIR /app

# Copy configuration file to the runtime environment
COPY src/main/java/robot_worlds_13/server/configuration/file.txt /app/src/main/java/robot_worlds_13/server/configuration/file.txt


# Copy the JAR with dependencies from the build stage
COPY --from=build /app/libs/robot-worlds-server-jar-with-dependencies.jar /app/robot_worlds_13.jar

# Set the entry point to run the JAR file
ENTRYPOINT ["java", "-jar", "robot_worlds_13.jar"]

# Expose the port the application runs on
EXPOSE 5050
