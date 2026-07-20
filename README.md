# Enterprise B2B E-Commerce Platform

A microservices-based B2B E-Commerce platform built with Spring Boot, React, and a modern Dockerized infrastructure. This project serves as a comprehensive prototype for an enterprise-grade backend ecosystem.

## Architecture

The platform is designed around the **Bounded Context** pattern from Domain-Driven Design (DDD). It utilizes a distributed microservices architecture to ensure scalability, fault tolerance, and separation of concerns.

### Microservices
1. **Discovery Server (`discovery-server`)**: Netflix Eureka server for service registry and discovery.
2. **API Gateway (`api-gateway`)**: Spring Cloud Gateway that routes all incoming frontend traffic to the appropriate backend microservices.
3. **Identity Service (`identity-service`)**: Handles user authentication, credential storage (PostgreSQL), and JWT token generation.
4. **Product Catalog Service (`product-catalog-service`)**: Manages the product catalog. Uses MongoDB for flexible schema design to handle diverse product attributes.
5. **Inventory Service (`inventory-service`)**: Manages product stock levels. Uses PostgreSQL for ACID compliance.
6. **Order Service (`order-service`)**: Handles order placement. Communicates synchronously with the Inventory Service (via FeignClient) and asynchronously with the Notification Service (via Apache Kafka).
7. **Notification Service (`notification-service`)**: Listens to Kafka topics for `OrderPlacedEvent`s and processes asynchronous notifications.

### Infrastructure (Dockerized)
- **PostgreSQL**: Relational database for Identity, Inventory, and Order services.
- **MongoDB**: NoSQL database for the Product Catalog service.
- **Redis**: Distributed caching (configured for API Gateway / caching layers).
- **Apache Kafka**: Event broker for asynchronous communication (KRaft mode).

### Frontend
- **Vite + React SPA**: A modern, glassmorphic Single Page Application built with React, React Router, and Lucide React icons.

## Prerequisites
- Java 17+
- Apache Maven 3.9+
- Node.js 18+
- Docker Desktop (with WSL 2 enabled on Windows)

## Getting Started

### 1. Start Infrastructure
Start the required databases and Kafka broker using Docker Compose:
```bash
docker compose up -d
```

### 2. Build the Backend
Compile all the Spring Boot microservices:
```bash
mvn clean install -DskipTests
```

### 3. Run the Services
You can run the provided `start-all.bat` script on Windows to automatically launch all 7 microservices and the React frontend in separate command windows.
```bash
./start-all.bat
```
*(Note: Ensure you start the `discovery-server` first and wait a few seconds before starting the other services so they can register successfully).*

### 4. Access the Platform
- **Frontend UI**: `http://localhost:3000`
- **API Gateway**: `http://localhost:8080`
- **Eureka Dashboard**: `http://localhost:8761`

## Testing
The core domain services (`order-service` and `inventory-service`) include JUnit 5 and Mockito tests for the business logic.
To run the tests:
```bash
mvn test
```

## Technologies Used
- **Backend**: Java 17, Spring Boot 3, Spring Cloud, Spring Data JPA, Spring Data MongoDB, FeignClient, Resilience4j (Circuit Breaker).
- **Frontend**: React 18, Vite, React Router DOM, Vanilla CSS (Design System).
- **Event Streaming**: Apache Kafka.
- **Databases**: PostgreSQL, MongoDB, Redis.
