<div align="center">

# 🧠 CoachIQ
### AI-Powered Fitness Coaching Platform

**Personalized workouts. Smarter nutrition. Progress tracking. AI-powered insights.**

A full-stack fitness coaching platform built with **Spring Boot**, **React**, **Spring AI**, **Groq**, **Keycloak**, **MySQL** and **MongoDB**.

[![Java](https://img.shields.io/badge/Java-21-orange)](#-tech-stack)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.x-brightgreen)](#-tech-stack)
[![React](https://img.shields.io/badge/React-18-61DAFB)](#-tech-stack)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](#-license)

</div>

---

## 📑 Table of Contents

- [Overview](#-overview)
- [Key Features](#-key-features)
- [Architecture](#️-architecture)
- [Why a Modular Monolith?](#-why-a-modular-monolith)
- [Tech Stack](#-tech-stack)
- [Project Structure](#-project-structure)
- [API Reference](#-api-reference)
- [Swagger / OpenAPI](#-swagger--openapi)
- [Local Setup](#️-local-setup)
- [Environment Variables](#-environment-variables)
- [Testing](#-testing)
- [Health & Monitoring](#-health--monitoring)
- [Security](#️-security)
- [Backend Module Responsibilities](#-backend-module-responsibilities)
- [Frontend Architecture](#-frontend-architecture)
- [Data Storage Strategy](#️-data-storage-strategy)
- [Future Enhancements](#-future-enhancements)
- [Contributing](#-contributing)
- [License](#-license)
- [Author](#-author)

---

## 🚀 Overview

**CoachIQ** is a full-stack AI fitness coaching platform that helps users plan workouts, generate personalized diet plans, track fitness progress, and receive AI-powered progress analysis.

CoachIQ is built as a **modular monolith** — a single Spring Boot application with clearly separated business modules — combining:

- 🏋️ AI-generated workout plans
- 🥗 AI-generated diet plans
- 📈 Daily, weekly, and assessment-based progress tracking
- 🧠 AI-powered progress analysis
- 🔐 JWT authentication via Keycloak
- 🗄️ MySQL for user data, 🍃 MongoDB for fitness-domain data
- 📄 Automated monthly PDF progress reports, delivered by email
- 📚 Swagger / OpenAPI documentation
- ❤️ Actuator health and metrics endpoints
- ⚛️ React + Vite frontend with Tailwind CSS

---

## ✨ Key Features

### 🏋️ AI Workout Planner
Generates personalized, multi-day workout plans based on user goals and requirements, with structured exercise data and full workout-plan history, persisted in MongoDB.

```text
User Input → WorkoutPlanController → WorkoutPlanService → WorkoutAIService
           → Spring AI → Groq LLM → Structured Workout Plan → MongoDB
```

### 🥗 AI Diet Planner
Generates personalized diet plans with daily calorie targets, macronutrient breakdowns, meals, and day-wise structure, with saved history in MongoDB.

```text
User Input → DietController → DietService → DietAIService
           → Spring AI → Groq LLM → Structured Diet Plan → MongoDB
```

### 📊 Progress Tracking
Users can log daily and weekly progress and fitness assessments, and receive AI-generated progress analysis (including a "latest analysis" endpoint).

```text
Daily / Weekly / Assessment Data → ProgressService → MongoDB
      → ProgressAnalysisService → Groq LLM → AI Progress Analysis
```

### 📄 Automated Monthly Reports
A scheduled job (configured for the **Asia/Kolkata** timezone) analyzes each user's monthly progress, generates a PDF report, and emails it out. A failure while processing one user's report does not block processing for other users.

```text
Monthly Scheduler → Load User Progress → AI Progress Analysis
      → Save Analysis → Generate PDF → Send Email
```

### 🔐 Authentication & Authorization
Authentication is handled by **Keycloak** with JWT-based security. The backend derives the authenticated user's identity from the validated JWT — it never trusts a client-supplied user ID header.

```text
Frontend → Register / Login → Spring Boot → Keycloak
      → JWT Access Token → Spring Security Resource Server → Authenticated API
```

### 📈 Monitoring & Observability
Spring Boot Actuator exposes health, info, metrics, and Prometheus-compatible endpoints, plus readiness/liveness probes and request trace IDs (e.g. `INFO [traceId=abc123] ...`).

---

## 🏗️ Architecture

```text
                         ┌──────────────────────┐
                         │      React App        │
                         │   Vite + Tailwind      │
                         └──────────┬────────────┘
                                    │
                              REST + JWT
                                    │
                                    ▼
                 ┌──────────────────────────────────────┐
                 │            CoachIQ Backend            │
                 │         Spring Boot Application       │
                 │                                        │
                 │  ┌─────────────┐   ┌───────────────┐  │
                 │  │ UserService │   │ WorkoutPlan   │  │
                 │  └─────────────┘   │   Service     │  │
                 │                     └───────────────┘  │
                 │  ┌─────────────┐   ┌───────────────┐  │
                 │  │ DietService │   │   Progress    │  │
                 │  └─────────────┘   │   Tracking    │  │
                 │                     └───────────────┘  │
                 │          ┌───────────────────┐         │
                 │          │     AiService      │         │
                 │          │  Spring AI + LLM   │         │
                 │          └─────────┬──────────┘         │
                 └────────────────────┼──────────────────┘
                                      │
                    ┌─────────────────┼──────────────────┐
                    │                 │                  │
                    ▼                 ▼                  ▼
               ┌─────────┐      ┌─────────┐        ┌──────────┐
               │  MySQL  │      │ MongoDB │        │ Keycloak │
               │  Users  │      │ Fitness │        │   JWT    │
               └─────────┘      └────┬────┘        └──────────┘
                                      │
                                      ▼
                                Groq / LLM API
```

---

## 🧩 Why a Modular Monolith?

CoachIQ intentionally uses a modular monolith instead of independently deployed microservices. Each business domain (`UserService`, `WorkoutPlanService`, `DietService`, `Progress/Tracking`, `AiService`) follows the same internal structure — `Controller → Service → Repository → DTO → Entity` — and modules communicate through direct Java service calls inside the same Spring Boot application.

**Benefits:**

| Benefit | Why it matters |
|---|---|
| One deployable application | Simpler deployment pipeline |
| Easier local development | No orchestration needed to run locally |
| Lower operational complexity | No service discovery, API gateway, or message broker required |
| Faster internal communication | In-process calls instead of network hops |
| Clear business boundaries | Modules stay decoupled despite being co-deployed |
| Future flexibility | Can be split into microservices later if scale requires it |

---

## 🧰 Tech Stack

| Category             | Technology                    |
| --------------------- | ------------------------------ |
| Frontend               | React 18                        |
| Frontend Build Tool     | Vite                             |
| Styling                 | Tailwind CSS                    |
| Routing                 | React Router                    |
| Backend                 | Java 21                          |
| Framework               | Spring Boot 3.5.x                |
| API                     | Spring Web / REST                |
| Validation               | Spring Boot Validation           |
| Security                 | Spring Security                  |
| Authentication            | Keycloak + JWT                   |
| Relational Database        | MySQL                             |
| NoSQL Database              | MongoDB                            |
| AI Integration                | Spring AI                           |
| LLM Provider                    | Groq (OpenAI-compatible API)        |
| API Documentation                 | Springdoc OpenAPI / Swagger          |
| Email                                | Spring Mail / Gmail SMTP               |
| PDF Generation                        | Apache PDFBox                            |
| Monitoring                              | Spring Boot Actuator                      |
| Build Tool                                | Maven                                       |

---

## 📁 Project Structure

```text
CoachIQ/
│
├── CoachIQ Backend/
│   ├── src/main/java/com/fitness/coachiq/
│   │   ├── AiService/            # Config, DTO, Service, prompt
│   │   ├── Config/                # Security, exception handling, tracing, Mongo/OpenAPI config
│   │   ├── UserService/           # Controller, DTO, Entity, Repository, Service
│   │   ├── WorkoutPlanService/    # Controller, DTO, Entity, Repository, Service
│   │   ├── DietService/           # Controller, DTO, Entity, Repository, Service
│   │   └── Progress/Tracking/     # Controller, DTO, Entity, Repository, Service
│   ├── src/main/resources/application.properties
│   ├── .env.example
│   ├── pom.xml
│   └── mvnw
│
├── CoachIQ Frontend/
│   ├── src/
│   │   ├── components/    # AppShell, ProtectedRoute, Ui
│   │   ├── context/       # AuthContext
│   │   ├── lib/           # api.js
│   │   ├── pages/         # Dashboard, DietPlans, WorkoutPlans, Progress, Login, Register, ...
│   │   ├── App.jsx
│   │   ├── index.css
│   │   └── main.jsx
│   ├── .env.example
│   ├── package.json
│   ├── tailwind.config.js
│   ├── vite.config.js
│   └── index.html
│
└── README.md
```

---

## 🔌 API Reference

### 👤 User APIs

| Method | Endpoint               | Description          |
| ------ | ----------------------- | ---------------------- |
| `POST` | `/api/users/register`    | Register a new user     |
| `POST` | `/api/users/login`        | Authenticate user        |
| `GET`  | `/api/users/me`            | Get current user          |

### 🏋️ Workout APIs

| Method | Endpoint                              | Description                |
| ------ | -------------------------------------- | ---------------------------- |
| `POST` | `/api/workout-plans`                    | Generate a workout plan       |
| `GET`  | `/api/workout-plans`                     | Get user's workout plans       |
| `GET`  | `/api/workout-plans/{id}`                 | Get a specific workout plan     |
| `GET`  | `/api/workout-plans/{id}/days/{day}`       | Get a specific workout day        |

### 🥗 Diet APIs

| Method | Endpoint                           | Description             |
| ------ | ------------------------------------ | -------------------------- |
| `POST` | `/api/diet-plans`                      | Generate a diet plan         |
| `GET`  | `/api/diet-plans`                       | Get user's diet plans          |
| `GET`  | `/api/diet-plans/{id}`                   | Get a specific diet plan         |
| `GET`  | `/api/diet-plans/{id}/days/{day}`          | Get a specific diet day             |

### 📊 Progress APIs

| Method | Endpoint                          | Description                    |
| ------ | ------------------------------------ | --------------------------------- |
| `POST` | `/api/progress/daily`                  | Save daily progress                  |
| `GET`  | `/api/progress/daily`                   | Get daily progress                    |
| `POST` | `/api/progress/weekly`                   | Save weekly progress                   |
| `GET`  | `/api/progress/weekly`                    | Get weekly progress                     |
| `POST` | `/api/progress/assessment`                 | Save an assessment                        |
| `GET`  | `/api/progress/assessment`                  | Get assessments                            |
| `GET`  | `/api/progress/analysis`                     | Get progress analysis                       |
| `GET`  | `/api/progress/analysis/latest`               | Get the latest progress analysis              |

---

## 📚 Swagger / OpenAPI

With the backend running locally, API documentation is available via Springdoc:

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

---

## ⚙️ Local Setup

### Prerequisites

- Java 21
- Maven (optional — the Maven Wrapper is included)
- Node.js + npm
- MySQL
- MongoDB
- Keycloak
- A Groq API key

### 1. Clone the repository

```bash
git clone <YOUR_GITHUB_REPOSITORY_URL>
cd CoachIQ
```

### 2. Configure MySQL

```sql
CREATE DATABASE userservicedb;
```

The backend uses Spring Data JPA with `spring.jpa.hibernate.ddl-auto=update`.

```bash
export MYSQL_URL="jdbc:mysql://localhost:3306/userservicedb"
export MYSQL_USERNAME="root"
export MYSQL_PASSWORD="your_mysql_password"
```

### 3. Configure MongoDB

```bash
export MONGODB_URI="mongodb://localhost:27017/coachiq"
```

MongoDB stores workout plans, diet plans, daily/weekly progress, assessments, and progress analysis.

### 4. Configure Keycloak

```text
Realm:  fitness-oauth2
Client: coachiq-backend
```

The backend expects Keycloak at `http://localhost:8181` by default:

```bash
export KEYCLOAK_SERVER_URL="http://localhost:8181"
export KEYCLOAK_REALM="fitness-oauth2"
export KEYCLOAK_CLIENT_ID="coachiq-backend"
export KEYCLOAK_CLIENT_SECRET="your_client_secret"
export KEYCLOAK_ISSUER_URI="http://localhost:8181/realms/fitness-oauth2"
export KEYCLOAK_TOKEN_URL="http://localhost:8181/realms/fitness-oauth2/protocol/openid-connect/token"
```

> **Important:** Keycloak configuration must match the realm, client, and authentication settings used by the application.

### 5. Configure Groq / AI

CoachIQ uses **Spring AI** with Groq's OpenAI-compatible API (base URL `https://api.groq.com/openai`, model `openai/gpt-oss-120b`).

```bash
export GROQ_API_KEY="your_groq_api_key"
```

### 6. Configure Email

Monthly progress reports are sent via Gmail SMTP. Use a Gmail **App Password** rather than your account password if your account requires it.

```bash
export MAIL_USERNAME="your_email@gmail.com"
export MAIL_PASSWORD="your_app_password"
```

### 7. Configure the frontend

Create `CoachIQ Frontend/.env`:

```env
VITE_API_BASE_URL=http://localhost:8080
```

### 8. Run the backend

```bash
cd "CoachIQ Backend"
./mvnw clean spring-boot:run      # Windows: mvnw.cmd clean spring-boot:run
```

Backend runs at `http://localhost:8080`.

### 9. Run the frontend

```bash
cd "CoachIQ Frontend"
npm install
npm run dev
```

Vite serves the frontend, typically at `http://localhost:5173`.

---

## 🔒 Environment Variables

Secrets are kept outside source code.

### Backend

| Variable                  | Purpose                    |
| --------------------------- | ----------------------------- |
| `MYSQL_URL`                    | MySQL JDBC URL                     |
| `MYSQL_USERNAME`                | MySQL username                      |
| `MYSQL_PASSWORD`                | MySQL password                       |
| `MONGODB_URI`                    | MongoDB connection URI                |
| `KEYCLOAK_SERVER_URL`             | Keycloak server                        |
| `KEYCLOAK_REALM`                   | Keycloak realm                          |
| `KEYCLOAK_CLIENT_ID`                | Keycloak client                          |
| `KEYCLOAK_CLIENT_SECRET`             | Keycloak client secret                    |
| `KEYCLOAK_ISSUER_URI`                 | JWT issuer                                 |
| `KEYCLOAK_TOKEN_URL`                    | Keycloak token endpoint                     |
| `GROQ_API_KEY`                            | Groq API key                                 |
| `MAIL_USERNAME`                             | SMTP username                                 |
| `MAIL_PASSWORD`                               | SMTP password                                   |
| `CORS_ALLOWED_ORIGINS`                          | Allowed frontend origins                          |

### Frontend

| Variable            | Purpose               |
| --------------------- | ------------------------ |
| `VITE_API_BASE_URL`      | Backend API base URL       |

> ⚠️ Never commit real passwords, API keys, Keycloak client secrets, SMTP credentials, or tokens.

---

## 🧪 Testing

```bash
# Run backend tests
./mvnw test              # Windows: mvnw.cmd test

# Build the backend
./mvnw clean package

# Build the frontend
npm run build
```

---

## 🩺 Health & Monitoring

Spring Boot Actuator exposes:

| Endpoint | URL |
|---|---|
| Health | `http://localhost:8080/actuator/health` |
| Info | `http://localhost:8080/actuator/info` |
| Metrics | `http://localhost:8080/actuator/metrics` |
| Prometheus | `http://localhost:8080/actuator/prometheus` |

> Production deployments should protect operational endpoints appropriately (e.g. restrict access, disable sensitive details).

---

## 🛡️ Security

- JWT-based authentication via Keycloak identity management
- Spring Security resource-server token validation
- User identity derived from authenticated JWTs (never a client-supplied header)
- Environment-variable–based secrets — no hard-coded API keys or database passwords
- CORS allow-list configuration
- Bean validation and global exception handling
- Request trace IDs for observability
- Protected resource access based on authenticated users

**Never commit:** `.env` files, API keys, database passwords, Keycloak client secrets, SMTP passwords, access tokens, or refresh tokens.

---

## 🧱 Backend Module Responsibilities

| Module | Responsibilities |
|---|---|
| **UserService** | Registration, login, user retrieval, Keycloak integration, user persistence |
| **WorkoutPlanService** | Workout plan creation, retrieval, workout-day retrieval, persistence |
| **DietService** | Diet plan creation, retrieval, diet-day retrieval, persistence |
| **Progress/Tracking** | Daily/weekly progress, assessments, progress analysis, monthly report scheduling, PDF generation, email delivery |
| **AiService** | Workout & diet AI generation, progress analysis, prompt construction, Spring AI configuration, LLM integration |

---

## 🎨 Frontend Architecture

The React frontend is intentionally lightweight:

- React functional components
- React Context for authentication (`AuthContext`)
- `useState` / `useEffect` for local state
- Native `fetch` API for data access
- React Router for navigation
- Tailwind CSS for styling
- No Redux, no React Query, no external UI component library

The goal is to keep the frontend easy to understand and maintain.

---

## 🗃️ Data Storage Strategy

CoachIQ uses SQL and NoSQL databases for different responsibilities:

- **MySQL** — structured user data and JPA-managed entities
- **MongoDB** — fitness-domain, document-style data: workout plans, diet plans, daily/weekly progress, assessments, and progress analysis

This separation lets each database be used where its data model fits best.

---

## 🔮 Future Enhancements

- 🔄 Refresh-token rotation
- ✉️ Email verification
- 🔑 Password reset
- 🧑‍💼 Role-based administration
- ✏️ Edit/delete workout and diet plans
- 📄 Improved report templates
- 📊 Advanced progress analytics
- 📱 Mobile application
- 🔔 Workout and nutrition reminders
- 🧠 More personalized AI recommendations
- 🏃 Activity tracking integrations
- 📈 Advanced dashboard aggregation
- ⚡ Caching for frequently accessed data
- 🔍 Better AI response validation and observability

---

## 🤝 Contributing

Contributions, issues, and feature requests are welcome.

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/your-feature`)
3. Commit your changes with clear messages
4. Open a pull request describing what changed and why

---

## 📄 License

This project is available under the [MIT License](LICENSE). Update this section if a different license applies.

---

## 👨‍💻 Author

**Priyanshu Kashyap**
B.Tech Computer Science Engineering Student · Java · Spring Boot · React · AI Engineering

- GitHub: [Priyanshu462003](https://github.com/Priyanshu462003)
- LinkedIn: [priyanshukashyap9897](https://www.linkedin.com/in/priyanshukashyap9897/)

---

<div align="center">

### ⭐ Support

If you find **CoachIQ** interesting or useful, consider giving the repository a star.
Feedback, suggestions, and contributions are welcome.

Built with ☕ Java, ⚛️ React, and 🧠 AI

</div>
