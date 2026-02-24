.PHONY: help run build test-unit test-integration test-mutation lint spotbugs update-deps

JAR := target/blackjack-1.0.1.jar
SOURCES := $(shell find src/main -name '*.java')

help: ## Show this help
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-20s\033[0m %s\n", $$1, $$2}'

$(JAR): $(SOURCES) pom.xml
	./mvnw -q package -DskipTests

build: $(JAR) ## Build the project

run: $(JAR) ## Build and run the Blackjack game
	java --enable-native-access=ALL-UNNAMED -jar $(JAR)

test-unit: ## Run I/O-free unit tests
	./mvnw test -DexcludedGroups=integration

test-integration: ## Run integration tests
	./mvnw test -Dgroups=integration

test-mutation: ## Run mutation testing with Pitest
	./mvnw pitest:mutationCoverage

lint: ## Format code (CI=1 for check-only)
ifeq ($(CI),1)
	./mvnw spotless:check
else
	./mvnw spotless:apply
endif

spotbugs: ## Run SpotBugs bytecode analysis
	./mvnw spotbugs:check

update-deps: ## Update all dependency and plugin versions
	./mvnw versions:use-latest-versions versions:update-properties versions:update-parent -DgenerateBackupPoms=false
