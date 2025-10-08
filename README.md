# JameBackend

This is  for the Jame application.

## Table of Contents

- [About](#about)  
- [Features](#features)  
- [Tech Stack](#tech-stack)  
- [Getting Started](#getting-started)  
  - [Prerequisites](#prerequisites)  
  - [Installation](#installation)  
  - [Configuration](#configuration)  
  - [Running](#running)  
- [API Endpoints / WebSocket](#interface)  


---

## About

**JameBackend** is the server-side component for the **Jame** application. It handles REST APIs, WebSocket (STOMP) communication, data persistence, and the business logic behind user interactions and messaging.

---

## Features

- User management (signup, login, profiles, etc.)  
- CRUD operations for resources (questions, posts, etc.)  
- WebSocket + STOMP support for real-time messaging / notifications  
- Data persistence using JPA / Hibernate  
- Secure endpoints (authentication / authorization)  
- Validation, error handling, and DTO mapping  

---

## Tech Stack

- Java (17+)  
- Spring Boot (Web, WebSocket, Data JPA, Security)  
- Hibernate / JPA  
- WebSocket with STOMP  
- Gradle  
- (Optional) H2 / PostgreSQL / MySQL (depending on your configuration)  

---

## Getting Started

### Prerequisites

- Java 17+  
- Gradle or wrapper script  
- A database (e.g. PostgreSQL, MySQL) or use an in-memory one for development  
- (Optional) WebSocket / STOMP clients for testing (e.g. STOMP.js, Postman WebSocket, etc.)

### Installation

1. Clone this repository:

   ```bash
   git clone https://github.com/dhranny/JameBackend.git
   cd JameBackend

### Configuration

In src/main/resources/application.properties (or application.yml), set the following:

   ```bash
   spring.datasource.url=jdbc:postgresql://localhost:5432/jame_db
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   spring.jpa.hibernate.ddl-auto=update
  ```

### Build:

`./gradlew clean build`

### Running
`./gradlew bootRun`

---
## Interface

### REST	Path	Description
POST	 `/api/auth/register`	Register a new user

POST	`/api/auth/login`	Authenticate & receive token

POST  `/question/addquestion` This is to add a new source of question

POST  `/exam/format` Create a new for for a particular type of test

POST `/exam/init` Initiate a particular type of test

GET	`/api/questions`	List questions

### WebSocket Message Paths

SEND `/init` This is to start an exam that has been loaded for the user

SEND `/answer` This is the path for the users answer. Any realtime change to the
user's answer is sent throuh it.

RECEIVE `/topi/report` All response is received through this path.

---
## Enjoy building with JameBackend! 🎉
