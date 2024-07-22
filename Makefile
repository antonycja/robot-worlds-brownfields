# Define variables
JAVAC = javac
JAR = jar
JAVA = java
PROJECT_DIR = robot_worlds_dbn_13
SRC_DIR = $(PROJECT_DIR)/src/main/java
TEST_DIR = $(PROJECT_DIR)/src/test/java
BIN_DIR = bin
BUILD_DIR = build
LIB_DIR = libs
MAIN_CLASS = robot_worlds_13.server.Server
VERSION = 1.0.0
JAR_NAME = brownies-$(VERSION).jar
TEST_SERVER = libs/reference-server-0.1.0.jar

# Classpath for external libraries
CLASSPATH = $(BIN_DIR):$(SRC_DIR):$(PROJECT_DIR)/libs/*

# mvn commands
MVN = mvn -f $(PROJECT_DIR)/pom.xml

# Default target
#all: build

# Build all steps in sequence
#build: check-dependencies compile jar test package

# Check dependencies
check-dependencies:
	@echo "Checking dependencies..."
	$(MVN) dependency:analyze
	@echo "All dependencies are satisfied."

# Compile all Java files
#compile:
#	mkdir -p $(BIN_DIR)
#	$(JAVAC) -cp $(CLASSPATH) -d $(BIN_DIR) $(shell find $(SRC_DIR) -name "*.java")

# Create the JAR file
#jar: compile
#	mkdir -p $(BUILD_DIR)
#	$(JAR) cfe $(BUILD_DIR)/$(JAR_NAME) $(MAIN_CLASS) -C $(BIN_DIR) .

# Run the server
run-server:
	@echo "Running server..."
	$(JAVA) -jar $(TEST_SERVER)

# Run all tests
test:
	@echo "Running tests..."
	$(MVN) test
	@echo "All tests completed."

# Launch the application
#run: compile
#	$(JAVA) -cp $(CLASSPATH):$(BIN_DIR) $(MAIN_CLASS)

# Package the application for deployment
#package: jar
#	@echo "Packaging application..."
#	@echo "Application packaged as $(BUILD_DIR)/$(JAR_NAME)."

# Clean build artifacts
clean:
	rm -rf $(BIN_DIR) $(BUILD_DIR)

# Phony targets
.PHONY: all build check-dependencies clean test run package run-server compile jar