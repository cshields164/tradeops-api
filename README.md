Project Name: TradeOps-API
Description: TradeOps API is a portfolio backend project that demonstrates real-world backend engineering: clean architecture (controller/service/domain), validation and error handling, automated tests, and an API surface for trade management and portfolio positions. Later phases add Postgres persistence, auth, Docker, CI, and observability.
Tech Stack: Java 21 
Requirements: Java 21, Maven wrapper included
How to run
./mvnw spring-boot:run
How to test
./mvnw test
Health check
curl -s http://localhost:8080/health