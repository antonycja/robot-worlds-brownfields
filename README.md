 ### Robot Worlds 2.0

![maxresdefault.jpg](robot_worlds_dbn_13%2Fmaxresdefault.jpg)
This is a Java-based server-client application for a robot world simulation. The server manages a world with obstacles and robots, and clients can connect to the server to launch and control their own robots.

<br>


### Features:
- Server that manages a world with obstacles and robots
- Clients can connect to the server to launch and control their own robots
- Robots can move around the world, shoot, and interact with other robots and obstacles
- The server keeps track of all robots and their statuses, and can broadcast messages to all clients
- The world can be displayed in a GUI, and the server can send updates to the GUI to show the current state of the world
- For every robot that joins the server, information about previously-launched robots is sent to the client and is visible
- The server can send updates to the client's GUI to show the current state of all robots

<br>


### System Requirements:

- A linux operating system
- Java 11 or higher
- Maven (for dependency management)
- Access to a terminal that supports ANSI colors and Unicode characters
- Internet access

<br>


### Getting Started:

To get started with the Robot Worlds application, follow these steps:

    Clone the repository to your local machine
    Open the project in your favorite Java IDE (e.g. IntelliJ, Eclipse)
    Build and run the Server class to start the server
    Build and run the Client class to start a client and connect to the server
    Follow the prompts in the client to launch and control your robot

<br>


### Setup and Installation:

1. On a linux machine, connect to the internet and run:
        
        $ sudo apt update

2. To install maven run:
        
        $ sudo apt install maven -y

3. To verify the installation, run:
        
        $ mvn -version

4. Install java using:
        
        $ sudo apt install default-jdk -y

5. To verify the installation, run:
        
        $ java -version

<br>


### Cloning and compiling instructions:
1. Clone the repository in your local machine, do this by opening a terminal in a preffered folder
2. Run the following command:
        
        $ git clone git@gitlab.wethinkco.de:nmlaba023/dbn13_robot_worlds.git

3. Once the repository is cloned, run:
        
        $ cd /dbn13_robot_worlds/robot_worlds_dbn_13

4. Run the following command, to compile and generate the class files:
        
        $ mvn clean compile

<br>


### Running instructions:
##### Running the server
1. Run the server:
        
        $ mvn exec:java -Dexec.mainClass="robot_worlds_13.server.Server"

2. The port number and address is visible server side, when you run the Server class


##### Running the client:
1. Run the client, without the gui:
        
        $ mvn exec:java -Dexec.mainClass="robot_worlds_13.client.Client"
            
        or

        $ mvn exec:java -Dexec.mainClass="robot_worlds_13.client.Client" -Dexec.args="ip_address port"

        e.g mvn exec:java -Dexec.mainClass="robot_worlds_13.client.Client" -Dexec.args="20.20.19.0 2201"

2. Running the client with the gui:

        Use vscode / intelliJ and run the Client.java class
3. Ensure you are connected to the correct server and port (The port number is visible server side, when you run the Server class)

##### Run tests:
1. Run the tests with the following command:
        
        mvn test

<br>


### Usage:
##### Client commands:
Here are some basic commands you can use in the client to control your robot:

    forward <steps>: Move the robot forward by the specified number of steps
    back <steps>: Move the robot backward by the specified number of steps
    left: Turn the robot to the left
    right: Turn the robot to the right
    look: Show the current state of the world around the robot
    state: Show the current state of the robot
    fire: Shoot a bullet in the current direction of the robot
    repair: Repair the robot's shields
    reload: Reload the robot's ammo
    off: Quit the client and disconnect from the server

##### Server commands:
Here are some basic commands you can use to control your server:

    robots: lists all robots in server
    dump: lists the state of the world
    quit: quit the server

<br>


### Contributing:

We welcome contributions to the Robot Worlds project! To contribute, follow these steps:

    Fork the repository
    Create a new branch for your feature or bugfix
    Make your changes and commit them to your branch
    Push your changes to your forked repository
    Create a pull request from your forked repository to the original repository



### Contributors:

* Summer Ngcobo
* Sindiswa Zama
* Nkosikhona Mlaba
* Ayanda Mzimela (on martenity leave)
* Thobile Mvuni

### Advanced by:
* Antony Maposa
* Lindani Jonas

<br>


### Documentation:

For detailed documentation and additional resources, please visit our [Wiki](https://gitlab.wethinkco.de/nmlaba023/dbn13_robot_worlds/-/wikis/Robot-Worlds-Wiki/).

<br>

### License:

The Robot Worlds project is licensed under the MIT License. See the LICENSE file for more information.

