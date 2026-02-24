.PHONY: help test-unit test-integration test-mutation lint spotbugs update-deps

help: ## Show this help
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-20s\033[0m %s\n", $$1, $$2}'

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
