# Define variables
VERSION := $(shell mvn help:evaluate -Dexpression=project.version -q -DforceStdout)
RELEASE_VERSION := $(subst -SNAPSHOT,,$(VERSION))
BUILD_TYPE := development # default build type
POM_FILE := robot_worlds_dbn_13/pom.xml
REF_SERVER := $(shell pgrep -f runRefServer.sh)
#OWN_SERVER := $(shell pgrep -f runServer.sh)

.PHONY: all compile test_ref test_own version release_version package tag clean run_ref_server stop_ref_server run_own_server stop_own_server

# Default target
all: compile test_ref stop_servers
#all: compile test_ref test_own stop_servers

compile:
	mvn compile -f $(POM_FILE)


stop_ref_server:
	@echo "Stopping reference server if running"
	-kill $(REF_SERVER) || true

run_ref_server:
	@if lsof -i :5001 > /dev/null ; then \
		echo "Port 5001 is already in use. Killing the process..."; \
		kill $$(lsof -t -i:5001) || true; \
		sleep 2; \
	fi
	bash runRefServer.sh -a &
	sleep 5  # Give the server more time to start

test_ref: stop_ref_server run_ref_server
    # Wait for the server to start if necessary
	sleep 2
	cd robot_worlds_dbn_13 && mvn test -Dserver=reference
    # Stop the reference server after tests
	make stop_ref_server

run_own_server:
	./runServer.sh &

stop_own_server:
	@echo "Stopping own server if running"
	-kill $(OWN_SERVER_PID) || true

test_own: stop_own_server run_own_server
    # Wait for the server to start if necessary
	sleep 10
	mvn test -Dserver=own
    # Stop your own server after tests
	make stop_own_server

stop_servers: stop_ref_server #stop_own_server
	@echo "All servers stopped"

version:
    @echo "Current version: $(VERSION)"
    @echo "Release version: $(RELEASE_VERSION)"
    @echo "Build type: $(BUILD_TYPE)"

release_version:
	@echo "Preparing release version"
	sed -i 's/$(VERSION)/$(RELEASE_VERSION)/' $(POM_FILE)
	@echo "Version updated to $(RELEASE_VERSION) in $(POM_FILE)"

package: test_ref #test_own
ifeq ($(BUILD_TYPE), release)
	@echo "Packaging for release"
	mvn package
else
	@echo "Skipping packaging for development build"
endif

tag: package
ifeq ($(BUILD_TYPE), release)
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