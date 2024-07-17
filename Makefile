#!/bin/sh

mvn -q exec:java -Dexec.mainClass="za.co.robotworlds.client.Play"

mvn -q exec:java -Dexec.mainClass="za.co.robotworlds.server.socket.SocketServer"
