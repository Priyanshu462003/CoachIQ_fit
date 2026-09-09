# CoachIQ — Production-Oriented Modular Monolith

CoachIQ is a **single Spring Boot application** organized into business modules. It keeps the original project style — `Controller / Service / Repository / DTO / Entity` — while removing the operational overhead of the previous microservice stack.

## Architecture

```text
                         ┌──────────────────┐
                         │     Frontend     │
                         └────────┬─────────┘
                                  │ HTTPS + JWT
                                  ▼
                 ┌────────────────────────────────┐
                 │        CoachIQ Application     │
                 │      one Spring Boot JAR       │
                 │                                │
                 │  UserService                   │
                 │  WorkoutPlanService ───────┐   │
                 │  DietService ──────────────┤   │
                 │  ActivityService ──────────┤   │
                 │  Progress/Tracking ────────┤   │
                 │                             ▼   │
                 │                         AiService│
                 └───────┬───────────────┬─────────┘
                         │               │
                         ▼               ▼
                    ┌────────┐     ┌────────────┐
                    │ MySQL  │     │  MongoDB   │
                    │ Users  │     │ Fitness    │
                    └────────┘     └────────────┘
                         │
                         ▼
                     Keycloak
                         │
                         ▼
                       JWT

AiService ──HTTPS──► Groq / OpenAI-compatible LLM
Progress ──PDF────► Email SMTP
```

## Directory style

```text
src/main/java/com/fitness/coachiq/
│
├── Config/
│   ├── SecurityConfig.java
│   ├── CurrentUser.java
│   ├── GlobalExceptionHandler.java
│   ├── MongoConfig.java
│   ├── OpenApiConfig.java
│   ├── RequestTraceFilter.java
│   └── RestClientConfig.java
│
├── UserService/
│   ├── Controller/
│   ├── Service/
│   ├── Repository/
│   ├── DTO/
│   └── Entity/
│
├── WorkoutPlanService/
│   ├── Controller/
│   ├── Service/
│   ├── Repository/
│   ├── DTO/
│   └── Entity/
│
├── DietService/
│   ├── Controller/
│   ├── Service/
│   ├── Repository/
│   ├── DTO/
│   └── Entity/
│
├── ActivityService/
│   ├── Controller/
│   ├── Service/
│   ├── Repository/
│   ├── DTO/
│   └── Entity/
│
├── Progress/Tracking/
│   ├── Controller/
│   ├── Service/
│   ├── Repository/
│   ├── DTO/
│   └── Entity/
│
└── AiService/
    ├── Controller/
    ├── Service/
    ├── Repository/
    ├── DTO/
    ├── Entity/
    ├── prompt/
    └── parser/
```

## Main request flows

### Authentication

```text
Frontend
  │
  ├── register/login
  ▼
UserController
  ▼
UserService
  ├── KeycloakUserService ──► Keycloak
  └── UserRepository ───────► MySQL

Authenticated request
  │ Bearer JWT
  ▼
Spring Security
  ▼
CurrentUser
  ▼
Business module
```

Authenticated APIs no longer trust the `X-User-ID` header. The user ID comes from the validated JWT principal.

### Workout generation

```text
POST /api/workout-plans
        ↓
WorkoutPlanController
        ↓
WorkoutplanService
        ↓
UserService (validate user)
        ↓
WorkoutAIService
        ↓
Groq / LLM
        ↓
WorkoutParser
        ↓
WorkoutplanService
        ↓
MongoDB
```

### Diet generation

```text
POST /api/diets
   ↓
DietController → DietService → DietAIService → LLM
                                      ↓
                                  DietParser
                                      ↓
                                  MongoDB
```

### Activity tracking

Activity persistence is the critical operation. AI recommendation generation is **best effort**, so a temporary LLM failure does not make a successfully saved activity fail.

```text
POST /api/activities
        ↓
ActivityService
        ├── save activity ──► MongoDB
        │
        └── AI recommendation ──► MongoDB
             (failure is logged, activity remains saved)
```

### Progress and monthly AI report

```text
Daily / Weekly / Assessment APIs
              ↓
        ProgressService
              ↓
           MongoDB

Monthly scheduler
      ↓
UserService.getAllUserIds()
      ↓
Load monthly progress
      ↓
ProgressAnalysisService
      ↓
LLM
      ↓
Save analysis
      ↓
Generate PDF
      ↓
Send email
```

A failure for one user's monthly AI report does not stop processing for other users. Email delivery is also best effort after the report has been persisted.

## What was removed

- Eureka Server
- API Gateway
- Config Server
- RabbitMQ
- RabbitMQ listeners and publishers
- Load-balanced WebClient calls between modules
- Duplicated `UserValidationService` classes
- Microservice-specific ports and service discovery configuration

## Why this architecture

This is a **modular monolith**, not a collection of microservices placed in one project. Each business area owns its controllers, services, repositories, DTOs and entities. Modules communicate through direct Java service calls.

This gives CoachIQ:

- one deployable JAR
- simpler local development
- fewer infrastructure dependencies
- lower latency for internal calls
- easier debugging and testing
- clear module boundaries
- a straightforward path to extract a module later if scale requires it

## Production-oriented improvements

- Keycloak JWT resource-server authentication
- User identity derived from the JWT instead of a client-supplied header
- User ownership checks for resource-by-ID APIs
- Environment-variable based secrets
- No credentials/API keys committed to source
- Constructor injection
- Global exception handling
- Bean validation support
- CORS allow-list
- Graceful shutdown
- Actuator health/info/metrics
- Request trace ID support
- Mongo auditing
- Configurable monthly scheduler
- AI failure isolation for non-critical recommendations
- No public internal `/generated` endpoints

## Configuration

Copy `.env.example` values into your deployment environment. Do not commit `.env`.

Important variables include:

- `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`
- `MONGODB_URI`
- `KEYCLOAK_SERVER_URL`, `KEYCLOAK_REALM`, `KEYCLOAK_CLIENT_ID`, `KEYCLOAK_CLIENT_SECRET`
- `KEYCLOAK_ISSUER_URI`
- `GROQ_API_KEY`
- `AI_BASE_URL`, `AI_COMPLETIONS_PATH`, `AI_MODEL`
- `MAIL_USERNAME`, `MAIL_PASSWORD`
- `CORS_ALLOWED_ORIGINS`

## Run

```bash
./mvnw clean spring-boot:run
```

Build:

```bash
./mvnw clean package
```

The application listens on `http://localhost:8080` by default.

## Important deployment note

The original microservices used separate MongoDB database names. This monolith intentionally uses one configurable MongoDB database with separate collections. Existing production data should be migrated deliberately before switching deployments.
