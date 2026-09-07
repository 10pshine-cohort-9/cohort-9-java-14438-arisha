# Contact Management System - Backend

This folder contains the Spring Boot backend for the Contact Management System.

The backend provides REST APIs for authentication, contact management, profile management, email and phone management, and CSV import/export. It also handles database access, security, validation, and application-level business logic.

## Technologies Used

- Java 21
- Spring Boot 4.1
- Spring Web MVC
- Spring Data JPA
- Spring Security
- Jakarta Validation
- SQL Server
- JWT authentication
- Maven
- Apache Commons CSV
- H2 for testing
- JaCoCo for test coverage
- SonarQube for code quality analysis

## Features

- User registration
- User login
- JWT-based authentication
- Contact creation, retrieval, editing, and deletion
- Contact search
- Pagination and sorting
- Email management for contacts
- Phone number management for contacts
- User profile viewing and editing
- Password change
- CSV contact import
- CSV contact export
- Input validation
- Centralized exception handling
- Secure API endpoints
- Logging
- Automated backend testing

## Project Structure

```text
backend/
├── src/
│   ├── main/
│   │   ├── java/com/contactmanagement/backend/
│   │   │   ├── config/
│   │   │   ├── controller/
│   │   │   ├── dto/
│   │   │   ├── entity/
│   │   │   ├── exception/
│   │   │   ├── repository/
│   │   │   ├── security/
│   │   │   ├── service/
│   │   │   └── BackendApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── pom.xml
├── mvnw
└── mvnw.cmd
```

## Prerequisites

Before running the backend, make sure you have:

- Java 21 installed
- SQL Server running
- A SQL Server database available
- The required environment variables configured

## Environment Variables

The application expects the following environment variables:

```text
DB_URL
DB_USERNAME
DB_PASSWORD
JWT_SECRET
```

`JWT_SECRET` must be Base64 encoded and at least 256 bits long.

Example database URL:

```text
jdbc:sqlserver://localhost:1433;databaseName=ContactManagement;encrypt=true;trustServerCertificate=true
```

Use the database name and connection settings that match your own SQL Server setup.

## Run the Application

From the `backend` directory on Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

On macOS or Linux:

```bash
./mvnw spring-boot:run
```

By default, the backend runs on:

```text
http://localhost:8080
```

## Run Tests

On Windows:

```powershell
.\mvnw.cmd test
```

On macOS or Linux:

```bash
./mvnw test
```

## Build the Application

On Windows:

```powershell
.\mvnw.cmd clean package
```

On macOS or Linux:

```bash
./mvnw clean package
```

## Database

The backend uses SQL Server as the main database.

Hibernate is configured with:

```text
spring.jpa.hibernate.ddl-auto=update
```

This allows Hibernate to update the database schema based on the JPA entities.

## Authentication

The application uses JWT authentication.

After login, the backend generates a JWT token. The frontend sends this token with authenticated requests using:

```text
Authorization: Bearer <token>
```

Protected endpoints require a valid JWT token.

## Testing and Code Quality

The backend includes automated tests and uses:

- H2 for test database support
- JaCoCo for test coverage
- SonarQube for static analysis and code quality

JaCoCo is configured through Maven to generate coverage reports during verification.

## Frontend Connection

The React frontend communicates with this backend through REST API requests.

During development, the frontend Vite server proxies `/api` requests to:

```text
http://localhost:8080
```

The backend should therefore be running before testing frontend features that require API access.

## Related Project

This backend is part of the full-stack Contact Management System built with React, Spring Boot, SQL Server, and JWT authentication.