Goal: “Create Spring project, add /health, get tests running. Start the environment set up procedure to then start adding more features to the project.”
Problems + Fixes: “Port 8080 in use → changed server.port to 8081” or “killed PID” and a few git issues to push the project to my repository when it was first implemented. Also a few issues trying to switch Java to the correct version.
Commands: lsof -iTCP:8080 -sTCP:LISTEN, kill -9 …, ./mvnw test
Next: “Day 2: domain model + 8 tests”