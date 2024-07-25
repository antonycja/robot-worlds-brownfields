# Use an official OpenJDK image as a parent image
FROM openjdk:17

# Set the maintainer label
LABEL maintainer="Tech Team <tech-team@wethinkcode.co.za>"

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml and any other files needed for dependency resolution
COPY /home/lindani/cpt13_brownfields_2024/pom.xml /app/

# Copy the source code to the container
COPY src /app/src

# Copy the lib directory if there are external JAR dependencies
COPY libs /app/libs

# Install Maven to build the project
RUN  apt update && apt install -y maven

# Package the application using Maven
RUN mvn clean package -DskipTests

# Set the entry point to run the JAR file
ENTRYPOINT ["java", "-jar", "target/cpt13_brownfields_2024-1.0-SNAPSHOT.jar"]

# Expose the port the application runs on
EXPOSE 8080
