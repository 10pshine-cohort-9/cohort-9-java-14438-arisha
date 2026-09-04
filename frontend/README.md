# Contact Management System - Frontend

This folder contains the React frontend for the Contact Management System.

The frontend provides the user interface for authentication, contact management, profile management, and CSV import/export functionality. It communicates with the Spring Boot backend through REST API endpoints.

## Technologies Used

- React 19
- Vite 8
- React Router
- JavaScript
- CSS
- ESLint

## Features

- User registration
- User login with JWT authentication
- Responsive dashboard
- Contact creation
- Contact editing and deletion
- Contact search with live suggestions
- Pagination and alphabetical grouping
- Contact email management
- Contact phone number management
- User profile viewing and editing
- Password change
- CSV contact import
- CSV contact export
- Responsive navigation for smaller screens

## Project Structure

```text
frontend/
├── src/
│   ├── components/
│   ├── pages/
│   ├── App.jsx
│   ├── main.jsx
│   └── index.css
├── index.html
├── package.json
└── vite.config.js
```

## Prerequisites

Before running the frontend, make sure you have:

- Node.js installed
- npm installed
- The backend application running on port `8080`

## Installation

Open a terminal inside the `frontend` directory and install the dependencies:

```bash
npm install
```

## Run the Application

Start the development server:

```bash
npm run dev
```

Vite will display the local frontend URL in the terminal.

## Production Build

Create a production build with:

```bash
npm run build
```

## Linting

Run ESLint with:

```bash
npm run lint
```

## Backend Connection

Frontend requests beginning with `/api` are forwarded by Vite to:

```text
http://localhost:8080
```

The Spring Boot backend should therefore be running before testing features that require API access.

## Authentication

After a successful login, the frontend stores the JWT token and sends it with authenticated API requests using the `Authorization` header.

Protected pages redirect unauthenticated or expired sessions back to the login page.

## Related Project

This frontend is part of the full-stack Contact Management System built with React, Spring Boot, SQL Server, and JWT authentication.
