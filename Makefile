# Define variables
VERSION := $(shell mvn help:evaluate -Dexpression=project.version -q -DforceStdout)
RELEASE_VERSION := $(subst -SNAPSHOT,,$(VERSION))
BUILD_TYPE := development # default build type
POM_FILE := pom.xml
REF_SERVER := $(shell pgrep -f runRefServer.sh)
OWN_SERVER := $(shell pgrep -f runServer.sh)

.PHONY: all compile test_ref test_own version release_version package tag clean run_ref_server stop_ref_server run_own_server stop_own_server stop_servers

# Default target
all: compile test_ref stop_servers

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

run_own_server:
	@if lsof -i :5000 > /dev/null; then \
		echo "Port 5000 is already in use. Killing the process..."; \
		kill $$(lsof -t -i:5000); \
		sleep 2; \
	fi
	./runServer.sh &

stop_own_server:
	@echo "Stopping own server if running"
	@if [ -n "$(OWN_SERVER)" ]; then \
		kill $(OWN_SERVER); \
	else \
		echo "No own server process found"; \
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

package: test_ref test_own
ifeq ($(BUILD_TYPE),release)
	@echo "Packaging for release"
	mvn package
else
	@echo "Skipping packaging for development build"
endif

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
