# Flight Advisor API Service

## Description

API service for finding the cheapest flight between two cities.

## Technologies

- Programming language: Java
- Framework: Spring Boot
- Databases: H2, Neo4j
- Security: Spring Security
- Containers: Docker
- Test reporting: Jacoco

## Running the project

### Running in the IDE
Before running the project, we need to start Neo4j database.
We can do that by running:
```bash
docker-compose -f docker-compose.yml up -d graph-database
```
After that, running the project is as simple as importing it in the IDE and running FlightAdvisorApplication.
The service is available on http://localhost:8080

#### Known issues
There's a known issue when running spring applications in IntelliJ with:
```yaml
active: @spring.profiles.active@
```
If you get an exception when running the application complaining about that issue, just re-import the maven project in IntelliJ.


### Running in Docker
The service can also be run completely in Docker by running
```bash
docker-compose up -d
```
The service is available on http://localhost:8080

## Generating test reports
Jacoco is being used for generating the test reports. 
We can generate the report by running:
```bash
./mvnw clean verify
``` 
We can view the report in  **./target/site/jacoco/index.html**

## Swagger documentation
When ran, the service generates a complete swagger documentation containing all relevant endpoints.
We can access the documentation on:
http://localhost:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config#/

## Postman collection
The **postman** directory contains a postman collection that can be used to easily test all relevant endpoints.

## Initial data
The database is pre-populated with some initial data. That data includes authorities
such as ROLE_USER and ROLE_ADMIN and a single test admin user:

- username: admin
- password: password 

## H2 Console

The service exposes an H2 console on port 9093.
- URL: jdbc:h2:tcp://localhost:9093/./target/h2db/db/flight_advisor
- User: sa
- Password: **(empty)**

## Neo4j dashboard

This service uses Neo4j for storing airports and routes. We can use neo4j dashboard
to get a visual overview of the data.

Neo4j dashboard: http://localhost:7474/browser/
