# Enterprise B2B Bounded-Context E-Commerce Platform

A production-grade, distributed B2B e-commerce platform built with Java Spring Cloud Microservices, Apache Kafka, and React.

## 🚀 Architecture Overview

This platform is designed to overcome the limitations of monolithic architectures by implementing a highly available, fault-tolerant, and horizontally scalable microservices ecosystem.

### Core Technologies
- **Backend:** Java 17, Spring Boot 3, Spring Cloud
- **Frontend:** React, Vite, Vanilla CSS (Glassmorphism UI)
- **Service Discovery:** Netflix Eureka
- **API Gateway:** Spring Cloud Gateway (with Global JWT Authentication)
- **Event Choreography:** Apache Kafka (KRaft mode)
- **Resilience:** Resilience4j (Circuit Breakers)
- **Polyglot Persistence:** PostgreSQL, MongoDB, Redis

### Microservices Ecosystem
1. **API Gateway (`8080`):** The single entry point. Handles routing and globally validates JWTs.
2. **Discovery Server (`8761`):** Netflix Eureka registry for dynamic service location.
3. **Identity Service (`8081`):** Handles user authentication and issues signed JSON Web Tokens (JWT). Backed by PostgreSQL.
4. **Product Catalog Service (`8082`):** Manages dynamic product data using MongoDB. Implements Redis for high-speed caching of read operations.
5. **Inventory Service (`8083`):** Strict, transactional inventory management using PostgreSQL. Acts as a Kafka Consumer to deduct stock asynchronously.
6. **Order Service (`8084`):** The orchestrator. Uses OpenFeign to synchronously verify stock, saves orders in PostgreSQL, and acts as a Kafka Producer to trigger the order lifecycle.
7. **Notification Service (`8085`):** A lightweight Kafka Consumer that handles sending email/SMS confirmations asynchronously.

## ⚙️ Event-Driven Choreography
When an order is placed, the `order-service` publishes an `OrderPlacedEvent` to the `orderTopic` in Kafka. The `inventory-service` and `notification-service` independently consume this event to deduct stock and notify the customer without blocking the main checkout flow.

## 🛡️ Fault Tolerance
Synchronous calls (e.g., `order-service` calling `inventory-service` via OpenFeign) are wrapped in **Resilience4j Circuit Breakers**. If the inventory service experiences an outage or high latency, the circuit trips open, preventing cascading failures and instantly returning a graceful fallback message to the user.

## 🛠️ Getting Started (Local Development)

### Prerequisites
- Java 17
- Maven
- Node.js (for frontend)
- Docker & Docker Compose

### 1. Start Infrastructure
Start the required databases and message broker:
```bash
docker-compose up -d
```
This spins up PostgreSQL, MongoDB, Redis, and Apache Kafka.

### 2. Build & Run Backend Services
From the root directory:
```bash
mvn clean install
```
Start the services in the following order:
1. `discovery-server`
2. `api-gateway`
3. Domain Services (`identity`, `product-catalog`, `inventory`, `order`, `notification`)

### 3. Start Frontend
Navigate to the frontend directory:
```bash
cd frontend
npm install
npm run dev
```
The React SPA will be available at `http://localhost:3000`.

## 🧪 Testing
JUnit 5 and Mockito tests are included for critical business workflows in the `order-service` and `inventory-service`. Run tests via Maven:
```bash
mvn test
```

## 📄 License
MIT License
