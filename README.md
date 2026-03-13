# Workout-Tracker

Welcome to Workout-Tracker project. This project is a RESTful API for workout tracker application that helps you create workout from provided exercises, schedule and track workouts for registered users.

Users can:
- Fetch current available set of exercises, can be sorted and filtered by category and muscle groups.
- Create and manage workout composed various exercises
- Schedule and mangage scheduled workouts for specific date and time.
- List active or pending scheduled workouts sorted by date and time.

## Tech Stack
- Spring Boot, Spring Security
- JPA/Hibernate
- PostgreSQL
- Thymeleaf (only for registration verification through email)

## Implementation Details
- User sign-up and authentication with jwt:
    - Uses short lived signed jwt which can be refreshed, uses JJWT for working with jwt.
    - Custom registration flow requires verification through user email with embedded html, uses JakartaMail API and thymeleaf.
    - Endpoints secured using Spring Security.
- Uses custom database seeder to insert predefined set of exercises.
- Scheduled jobs are used for clean up.
- DTO validation implemented using Jakarta Validation API.
- JPA for modeling entities and relations, persistence implemented using Spring Data JPA.
- Uses OpenAPI and Swagger for documentation.
- Exception handling through custom exceptions and controller advice.
- Follows Controller-Service-Repository (CSR) pattern.

## Requirements
- JDK 21
- Maven 3.9.*

## Setup
### Clone the repository
```bash
git clone https://github.com/Yash-Shelar/Workout-Tracker-API.git
cd Workout-Tracker-API
mvn clean install
```
### Create application-dev.properties
- Database configuration (use Postgresql or H2 with PostgreSQL mode)
```
# Replace here or add as env variables
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}
```
- JWT configuration
```
# Replace with base64 encoded key
security.jwt.secret-key=${JWT_SECRET_KEY}
```
- Default account
```
# provide credentials for default or admin account which will be created at application start
admin.credentials.email=${ADMIN_EMAIL}
admin.credentials.password=${ADMIN_PASSWORD}
```
- Used by EmailService
```
# for running locally replace following with localhost:8080 and http
# used for generating html embedded in email sent on registration
application.domain=<domain-name>
application.protocol=<http/https>

# provide email id and app password which will be used by email service
spring.mail.username=${APP_EMAIL_ID}
spring.mail.password=${APP_PASSWORD}
```
- Set following to generate schema for test run
```
spring.jpa.hibernate.ddl-auto=create-drop
```
- Set profile in application.properties to dev
- Rest can be taken and tweaked from application-prod.properties