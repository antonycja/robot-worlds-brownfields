# Define variables
VERSION := $(shell mvn help:evaluate -Dexpression=project.version -q -DforceStdout)
RELEASE_VERSION := $(subst -SNAPSHOT,,$(VERSION))
BUILD_TYPE := development # default build type
POM_FILE := pom.xml
REF_SERVER := $(shell pgrep -f runRefServer.sh)
OWN_SERVER := $(shell pgrep -f runServer.sh)
IMAGE_NAME := gitlab.wethinkco.de:5050/amaposa023/cpt13_brownfields_2024
CONTAINER_NAME := robot_worlds_container
PORT := 5050

.PHONY: all compile test_ref test_own version release_version package tag clean run_ref_server stop_ref_server run_own_server stop_own_server stop_servers docker-build docker-run docker-stop docker-push

# Default target
all: compile test_own stop_own_server

compile:
	mvn compile -f $(POM_FILE)

stop_ref_server:
	@echo "Stopping reference server if running"
	@if [ -n "$(REF_SERVER)" ]; then \
		kill $(REF_SERVER); \
	else \
		echo "No reference server process found"; \
	fi

run_ref_server:
	@if lsof -i :5001 > /dev/null; then \
		echo "Port 5001 is already in use. Killing the process..."; \
		kill $$(lsof -t -i:5001); \
		sleep 2; \
	fi
	bash runRefServer.sh -a &
	sleep 5  # Give the server more time to start

test_ref: stop_ref_server run_ref_server
	sleep 2
	mvn test -Dserver=reference
	make stop_ref_server

run_own_server: stop_server
	./runServer.sh > own_server.log 2>&1 &
	sleep 5  # Give the server more time to start
	@echo "Own server log:"
	@cat own_server.log

stop_own_server:
	@echo "Stopping own server if running"
	@if [ -n "$(OWN_SERVER)" ]; then \
		kill $(OWN_SERVER); \
	else \
		echo "No own server process found"; \
	fi

stop_server:
	@if lsof -i :$(PORT) > /dev/null; then \
		echo "Port $(PORT) is already in use. Killing the process..."; \
		kill $$(lsof -t -i:$(PORT)); \
		sleep 2; \
	else \
		echo "No process found on port $(PORT)"; \
	fi

test_own: stop_own_server run_own_server
	sleep 10
	mvn test -Dserver=own
	make stop_own_server

stop_servers: stop_ref_server stop_own_server
	@echo "All servers stopped"

version:
	@echo "Current version: $(VERSION)"
	@echo "Release version: $(RELEASE_VERSION)"
	@echo "Build type: $(BUILD_TYPE)"

release_version:
	@echo "Preparing release version"
	sed -i 's/$(VERSION)/$(RELEASE_VERSION)/' $(POM_FILE)
	@echo "Version updated to $(RELEASE_VERSION) in $(POM_FILE)"

package:test_own #test_ref
	mvn package

tag: package
ifeq ($(BUILD_TYPE),release)
	@echo "Tagging release version"
	git tag release-$(RELEASE_VERSION)
	git push origin release-$(RELEASE_VERSION)
else
	@echo "Skipping tagging for development build"
endif

clean:
	mvn clean -f $(POM_FILE)

.PHONY: release
release: BUILD_TYPE := release
release: clean release_version compile package tag

.PHONY: development
development: BUILD_TYPE := development
development: clean compile package

# Docker targets
docker-build:
	docker build -t $(IMAGE_NAME) .

docker-run: docker-stop docker-build
	docker run -d --name $(CONTAINER_NAME) -p $(PORT):$(PORT) $(IMAGE_NAME)

docker-stop:
	@echo "Stopping Docker container if running"
	@if docker ps -q -f name=$(CONTAINER_NAME); then \
		docker stop $(CONTAINER_NAME); \
		docker rm $(CONTAINER_NAME); \
	else \
		echo "No Docker container found"; \
	fi

docker-push: docker-build
	@echo "Pushing Docker image to GitLab registry"
	docker push $(IMAGE_NAME)

docker-release: docker-stop docker-build docker-run docker-push
