# MSnap v2 - Architecture Documentation (v1)

> Last Updated: February 2026

## Overview

MSnap v2 is a multi-module Maven project built with **Spring Boot 4.0.2** and **Java 21**. The project is designed to automatically manage Microsoft Accounts, particularly those associated with Minecraft and related server assets. The architecture follows a modular, contract-first approach with clear separation of concerns.

## High-Level Architecture

```
┌─────────────────────────────────────────────────────────────────────┐
│                          External Clients                          │
│                    (Discord Users, Web Interface)                  │
└─────────────────────────────────────────────────────────────────────┘
                                   │
                                   ▼
┌─────────────────────┐    ┌─────────────────────┐
│   discord-service   │    │   (future) web-ui   │
│   (Discord Bot)     │    │                     │
└─────────────────────┘    └─────────────────────┘
           │                        │
           └────────┬───────────────┘
                    │
                    ▼
          ┌─────────────────────┐
          │     api-client      │
          │  (Generated Client) │
          └─────────────────────┘
                    │
                    │ HTTP/REST
                    ▼
          ┌─────────────────────┐
          │    api-service      │
          │   (REST API Core)   │
          └─────────────────────┘
                    │
                    ▼
          ┌─────────────────────┐
          │     (future)        │
          │  Database / Cache   │
          └─────────────────────┘
```

## Module Structure

```
msnap-v2/
├── pom.xml                 # Parent POM - dependency management
├── api-service/            # REST API backend service
├── api-client/             # Generated OpenAPI client library
├── contracts/              # OpenAPI specifications (contract-first)
├── discord-service/        # Discord bot frontend
└── docs/                   # Documentation
```

---

## Modules

### 1. **contracts** (Contract-First Design)

**Purpose:** Defines the API contract via OpenAPI specification. This module serves as the single source of truth for API definitions.

**Key Files:**
- `src/main/java/net/msnap/contracts/openapi/api.yml` - OpenAPI 3.1 specification

**Current Endpoints:**
| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/v1/ping` | GET | Health check endpoint, returns server status and timestamp |

**Responsibilities:**
- Define all API endpoints, request/response schemas
- Version API contracts
- Shared by both `api-service` (implementation) and `api-client` (consumer)

---

### 2. **api-service** (Backend REST API)

**Purpose:** The core REST API service that exposes endpoints for managing Microsoft Accounts and all associated data.

**Tech Stack:**
- Spring Boot 4.0.2
- Spring Web MVC
- Spring Actuator (health/metrics)
- Spring Validation
- SpringDoc OpenAPI (Swagger UI)

**Package Structure:**
```
net.msnap.apiservice/
├── ApiServiceApplication.java   # Main entry point
├── service/                     # Business logic services
│   └── RandomMessageService.java
└── v1/                          # API version 1
    ├── controller/              # REST controllers
    │   └── PingController.java
    └── dto/                     # Data Transfer Objects
```

**Configuration:**
- Swagger UI available at: `/swagger-ui.html`
- API docs at: `/v1/api-docs`
- Default port: `8080`

**Responsibilities:**
- Implement API endpoints defined in `contracts`
- Business logic and data processing
- Account management (CRUD operations)
- Valuation system
- Price list management

---

### 3. **api-client** (Generated API Client)

**Purpose:** A Java client library automatically generated from the OpenAPI specification. Used by other modules to communicate with `api-service`.

**Tech Stack:**
- OpenAPI Generator Maven Plugin (v7.19.0)
- Native Java HTTP client
- Jackson for JSON serialization

**How It Works:**
1. During Maven build, the plugin reads `contracts/src/main/java/net/msnap/contracts/openapi/api.yml`
2. Generates Java classes in `target/generated-sources/openapi/`
3. Generated packages:
   - `net.msnap.apiclient.api` - API interface classes (e.g., `PingControllerApi`)
   - `net.msnap.apiclient.model` - Model/DTO classes (e.g., `PingResponse`)
   - `net.msnap.apiclient.invoker` - HTTP client infrastructure

**Usage Example:**
```java
PingControllerApi api = new PingControllerApi();
PingResponse response = api.ping();
```

**Responsibilities:**
- Provide type-safe API client for internal services
- Handle HTTP communication, serialization/deserialization
- Simplify integration for consumer modules

---

### 4. **discord-service** (Discord Bot Frontend)

**Purpose:** A Discord bot that provides a user interface for managing accounts via slash commands.

**Tech Stack:**
- Spring Boot 4.0.2
- JDA 6.3.0 (Java Discord API)
- dotenv-java (environment configuration)
- Lombok

**Package Structure:**
```
net.msnap.discordservice/
├── DiscordServiceApplication.java  # Main entry point
├── MSnapDiscordBot.java            # Bot initialization & JDA setup
├── commands/                       # Slash command handlers
│   └── PingCommand.java
├── config/                         # Configuration classes
└── service/                        # Service layer
    └── ApiService.java             # Wrapper for api-client calls
```

**Current Commands:**
| Command | Description |
|---------|-------------|
| `/ping` | Check bot latency and API connection |

**Configuration:**
- Bot token via environment variable: `MSNAP_DISCORD_BOT_TOKEN`
- Supports `.env.local` file for local development

**Responsibilities:**
- Provide Discord interface for account management
- Process slash commands
- Display account information in embeds
- Communicate with `api-service` via `api-client`

---

## Communication Flow

### Discord Command → API Response

```
1. User triggers /ping command in Discord
                    │
                    ▼
2. PingCommand.java receives SlashCommandInteractionEvent
                    │
                    ▼
3. ApiService.ping() is called
                    │
                    ▼
4. PingControllerApi (api-client) makes HTTP GET to /api/v1/ping
                    │
                    ▼
5. api-service PingController handles request
                    │
                    ▼
6. Response flows back through the chain
                    │
                    ▼
7. Discord embed sent to user with latency info
```

### Inter-Module Dependencies

```
discord-service
      │
      └─── depends on ──→ api-client
                              │
                              └─── reads spec from ──→ contracts
                              
api-service
      │
      └─── implements spec from ──→ contracts
```

---

## Build Instructions

### Prerequisites
- Java 21+
- Maven 3.9.0+

### Build All Modules

From the project root directory:

```bash
# Full build (compile, test, package)
mvn clean install

# Skip tests
mvn clean install -DSkipTests
```

### Build Individual Modules

```bash
# Build contracts first (if changed)
cd contracts && mvn clean install

# Build api-client (generates code from contracts)
cd api-client && mvn clean install

# Build api-service
cd api-service && mvn clean install

# Build discord-service
cd discord-service && mvn clean install
```

### Build Order (Important!)

Due to dependencies, modules should be built in this order:
1. `contracts` - API specifications
2. `api-client` - Depends on contracts for code generation
3. `api-service` - Can be built independently
4. `discord-service` - Depends on api-client

### Run Services

**Start API Service:**
```bash
cd api-service
mvn spring-boot:run
# Or: java -jar target/api-service-0.0.1-SNAPSHOT.jar
```
Access Swagger UI: http://localhost:8080/swagger-ui.html

**Start Discord Service:**
```bash
# Set environment variable first
export MSNAP_DISCORD_BOT_TOKEN=your_token_here
# Or create .env.local file with: MSNAP_DISCORD_BOT_TOKEN=your_token_here

cd discord-service
mvn spring-boot:run
```

---

## Planned Modules (Not Yet Implemented)

| Module | Purpose |
|--------|---------|
| `worker-service` | Background jobs for automation (Selenium, Mineflayer) |
| `shared-core` | Common entities, utilities, and domain logic |
| `integration-tests` | End-to-end integration tests |
| `web-ui` | Web interface for account management |

---

## Key Design Decisions

### 1. Contract-First API Design
The API is defined first in OpenAPI specification, then:
- `api-service` implements the spec
- `api-client` is auto-generated from the spec

**Benefits:** Type safety, consistent documentation, automatic client generation.

### 2. Modular Multi-Module Maven Project
Each concern is separated into its own module with clear responsibilities.

**Benefits:** Independent deployability, clear dependency management, easier testing.

### 3. Spring Boot Foundation
All services use Spring Boot for consistency in:
- Dependency injection
- Configuration management
- Testing infrastructure
- Production-ready features (actuator)

### 4. JDA for Discord Integration
JDA 6.3.0 provides a robust, type-safe way to interact with Discord API.

---

## Environment Variables

| Variable | Module | Description |
|----------|--------|-------------|
| `MSNAP_DISCORD_BOT_TOKEN` | discord-service | Discord bot authentication token |

---

## API Versioning

APIs are versioned via URL path: `/api/v1/...`

This allows for:
- Parallel running of multiple API versions
- Gradual migration of clients
- Backward compatibility

---

## Future Considerations

1. **Database Integration** - Currently no persistence layer; will likely use PostgreSQL with Spring Data JPA
2. **Authentication** - Planned hardcoded API key, later OAuth2/JWT
3. **Caching** - Account valuations will be cached and invalidated on changes
4. **Message Queue** - For async job processing in worker-service
5. **Resilience** - Resilience4j BOM already included for circuit breakers, retries

---

© 2026 MSnap

