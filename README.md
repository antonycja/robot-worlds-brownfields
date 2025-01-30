# Robot Worlds 2.0: Comprehensive Documentation

## Overview
Robot Worlds 2.0 is an interactive Java-based server-client application that simulates a dynamic robot world environment. In this simulation, multiple clients can connect to a central server, each controlling their own robot in a shared virtual world filled with obstacles and opportunities for robot-to-robot interaction.

## Core Features

### Server Features
- Multi-threaded server architecture supporting concurrent client connections
- Real-time world state management and updates
- Obstacle generation and placement system
- Robot position and state tracking
- Collision detection and damage calculation
- World persistence with save/restore functionality

### Client Features
- Choice between GUI and terminal interfaces
- Real-time robot control and movement
- Combat system with shooting mechanics
- Robot status monitoring (health, position, ammunition)
- Auto-updating world view
- Command history and state tracking

### World Features
- Dynamic obstacle placement
- Multi-robot interaction
- Damage and repair systems
- Ammunition management
- Configurable world size

## System Requirements

### Hardware Requirements
- Minimum 4GB RAM (8GB recommended)
- 1GB free disk space
- Network connection for client-server communication

### Software Requirements
- Linux-based operating system (Ubuntu 20.04 or newer recommended)
- Java Development Kit (JDK) 17
- Maven 3.6 or higher
- Docker (optional, for containerized deployment)
- Terminal emulator with ANSI color support
- Network port access (default: 5050)

### Development Environment
- Any Java IDE (IntelliJ IDEA or Eclipse recommended)
- Git version control system
- Text editor for configuration files

## Build and Deployment Options

### 1. Standard Maven Build
```bash
# Compile the project
mvn compile

# Run tests
mvn test

# Create executable JAR
mvn package
```

### 2. Make Commands
The project includes a Makefile with various useful commands:
```bash
# Compile the project
make compile

# Run tests with your own server
make test_own

# Build and package
make package

# Clean build files
make clean

# Create a release
make release

# Development build
make development
```

### 3. Docker Deployment
The project can be containerized using Docker:
```bash
# Build Docker image
make docker-build

# Run Docker container
make docker-run

# Stop Docker container
make docker-stop

# Full release cycle
make docker-release
```

Docker container specifications:
- Base image: Ubuntu latest
- Java 17
- SQLite3
- Exposed port: 5050
- Includes necessary configuration files

## Detailed Installation Guide

### 1. System Preparation
First, ensure your system is up to date:
```bash
# Update package lists
sudo apt update

# Upgrade existing packages
sudo apt upgrade -y
```

### 2. Java Installation
Install the Java Development Kit:
```bash
# Install default JDK
sudo apt install default-jdk -y

# Verify installation
java -version
javac -version
```

### 3. Maven Installation
Install and configure Maven:
```bash
# Install Maven
sudo apt install maven -y

# Verify installation
mvn -version
```

### 4. Docker Installation
1. Build the Docker image:
```bash
docker build -t robot-worlds.
```

2. Run the container:
```bash
docker run -d -p 5050:5050 --name robot_worlds_container robot-worlds
```

3. View container logs:
```bash
docker logs robot_worlds_container
```

4. Stop the container:
```bash
docker stop robot_worlds_container
```

## Project Structure

### Main Components
- Server: `src/main/java/robot_worlds_13/server/`
- Client: `src/main/java/robot_worlds_13/client/`
- Configuration: `src/main/java/robot_worlds_13/server/configuration/`

### Dependencies
Key dependencies include:
- JUnit Jupiter 5.9.2 (testing)
- Mockito 3.6.28 (testing)
- JLine 3.26.1 (terminal handling)
- Gson 2.8.9 (JSON processing)
- Jackson Core 2.17.2 (JSON processing)
- SQLite JDBC 3.41.2.2 (database)
- Javalin 5.1.0 (web framework)
- EODSQL (database queries)

## Running the Application

### Starting the Server
1. From the project root directory, launch the server:
```bash
mvn exec:java -Dexec.mainClass="robot_worlds_13.server.Server"
```

### Starting the Client

#### Terminal Mode
1. Basic connection (uses default settings):
```bash
mvn exec:java -Dexec.mainClass="robot_worlds_13.client.Client"
```

2. Connection with specific server details:
```bash
mvn exec:java -Dexec.mainClass="robot_worlds_13.client.Client" -Dexec.args="[ip_address] [port]"
```

#### GUI Mode
1. Open the project in your IDE
2. Locate `Client.java` in the project explorer
3. Right-click and select "Run" or "Debug"
4. Enter server details in the connection dialogue

## Command Reference

### Client Commands
| Command | Syntax | Description | Example |
|---------|--------|-------------|----------|
| Forward | `forward <steps>` | Move robot forward by specified steps | `forward 5` |
| Back | `back <steps>` | Move robot backward by specified steps | `back 3` |
| Left | `left` | Rotate robot 90 degrees left | `left` |
| Right | `right` | Rotate robot 90 degrees right | `right` |
| Look | `look` | Display surrounding area (5x5 grid) | `look` |
| State | `state` | Show robot's current status | `state` |
| Fire | `fire` | Shoot in current direction | `fire` |
| Repair | `repair` | Repair robot's shields (costs 1 turn) | `repair` |
| Reload | `reload` | Reload ammunition (costs 1 turn) | `reload` |
| Off | `off` | Disconnect from server | `off` |

### Server Commands
| Command | Syntax | Description | Example |
|---------|--------|-------------|----------|
| Robots | `robots` | Display list of active robots | `robots` |
| Dump | `dump` | Show complete world state | `dump` |
| Save | `save <name>` | Save current world state | `save world1` |
| Restore | `restore <name>` | Load saved world state | `restore world1` |
| Delete | `delete <name>` | Remove saved world | `delete world1` |
| Quit | `quit` | Shutdown server | `quit` |

## Game Mechanics

### Robot Properties
- Health: Maximum 100 points
- Shields: Maximum 50 points, regenerates with repair command
- Ammunition: 10 shots per reload
- Movement: 1 step per movement command
- Vision: 5x5 grid centred on robot

### Combat System
- Shots travel in straight lines
- Direct hits cause 20 points of damage
- Shields absorb 50% of damage
- Robots are destroyed at 0 health
- Missed shots continue until hitting an obstacle or world boundary

### World Interaction
- Obstacles block movement and shots
- Robots cannot move through other robots
- World boundaries are solid
- Each action (move, shoot, repair) counts as one turn

## Server Configuration

### Default Settings
- Port: 5050
- Database: SQLite
- Configuration file location: `/app/src/main/java/robot_worlds_13/server/configuration/file.txt`

### Custom Configuration
1. Modify the configuration file
2. Rebuild the application/container
3. Restart the server

## Error Handling

### Common Client Issues
1. Connection refused
   - Verify the server is running
   - Check the IP address and port
   - Ensure network connectivity

2. Client crashes
   - Check Java version compatibility
   - Verify Maven dependencies
   - Review client logs in `logs/client.log`

### Common Server Issues
1. Port binding failed
   - Ensure the port is not in use
   - Check port permissions
   - Try alternative port

2. Memory issues
   - Increase JVM heap size
   - Monitor server resources
   - Check for memory leaks

## Contributing Guidelines

1. Fork Repository
   - Create a personal fork from [robot-worlds-brownfields](https://github.com/antonycja/robot-worlds-brownfields)
   - Keep fork synchronized with upstream

2. Branch Creation
   - Create a feature branch from the main
   - Use descriptive branch names (e.g., `feature/new-combat-system`)

3. Code Standards
   - Follow Java coding conventions
   - Maintain consistent indentation
   - Add appropriate documentation
   - Include unit tests

4. Submit Changes
   - Create a detailed pull request
   - Reference related issues
   - Await code review

## Development Team

### Core Developers
- Summer Ngcobo
- Sindiswa Zama
- Nkosikhona Mlaba
- Ayanda Mzimela
- Thobile Mvuni

### Advanced Features
- Antony Maposa
- Lindani Jonas

## Additional Resources
- [GitHub Repository](https://github.com/antonycja/robot-worlds-brownfields)
- [Issue Tracker](https://github.com/antonycja/robot-worlds-brownfields/issues)

## License
Robot Worlds 2.0 is released under the MIT License. See the LICENSE file in the repository for full details.

---
*Last updated: January 30, 2025*
