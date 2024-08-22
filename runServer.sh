#!/bin/bash

# Build the project and skip tests
#mvn clean package -DskipTests

#Clear the terminal
clear

# Run the generated JAR file
java -jar lib//robot-worlds-server-jar-with-dependencies.jar
