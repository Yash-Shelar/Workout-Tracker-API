# Workout-Tracker

Welcome to Workout-Tracker project. This project is a RESTful API for workout tracker application that allows users to create workout from predefined exercises, schedule workouts and track workout sessions.

## Features

- JWT based authentication with refresh tokens
- Email verification during registration
- Exercise filtering by category and muscle group
- Workout creation with multiple exercises
- Workout scheduling
- Scheduled cleanup jobs
- OpenAPI documentation

## Tech Stack
- Spring Boot
- JPA/Hibernate
- PostgreSQL
- Thymeleaf (only for registration verification through email)

## Implementation Details

- **Authentication & Authorization**
  - User sign-up and authentication using JWT.
  - Short-lived signed JWT tokens with refresh support using JJWT.
  - Custom email verification flow using Jakarta Mail API and Thymeleaf templates.
  - Endpoints secured using Spring Security.

- **Database**
  - Predefined exercises inserted using a custom database seeder.
  - Entities modeled using JPA/Hibernate.
  - Persistence handled through Spring Data JPA.

- **Validation**
  - DTO validation implemented using Jakarta Validation API.

- **Scheduling**
  - Scheduled jobs used for cleanup tasks.

- **API Documentation**
  - OpenAPI / Swagger used for API documentation.

- **Error Handling**
  - Centralized exception handling using custom exceptions and Controller Advice.

- **Structure**
  - Follows the Controller-Service-Repository (CSR) pattern.

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
- The remaining configuration can be copied from application-prod.properties
### Run
```bash
mvn spring-boot:run
```

## API Documentation

- Swagger UI: /swagger-ui/index.html
- OpenAPI Document: /v3/api-docs