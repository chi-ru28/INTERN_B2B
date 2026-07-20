@echo off
echo Starting Discovery Server...
start "Discovery Server" cmd /k "C:\maven\apache-maven-3.9.9\bin\mvn spring-boot:run -DskipTests -pl discovery-server"
timeout /t 15

echo Starting API Gateway...
start "API Gateway" cmd /k "C:\maven\apache-maven-3.9.9\bin\mvn spring-boot:run -DskipTests -pl api-gateway"
timeout /t 5

echo Starting Domain Services...
start "Identity Service" cmd /k "C:\maven\apache-maven-3.9.9\bin\mvn spring-boot:run -DskipTests -pl identity-service"
start "Product Catalog Service" cmd /k "C:\maven\apache-maven-3.9.9\bin\mvn spring-boot:run -DskipTests -pl product-catalog-service"
start "Inventory Service" cmd /k "C:\maven\apache-maven-3.9.9\bin\mvn spring-boot:run -DskipTests -pl inventory-service"
start "Order Service" cmd /k "C:\maven\apache-maven-3.9.9\bin\mvn spring-boot:run -DskipTests -pl order-service"
start "Notification Service" cmd /k "C:\maven\apache-maven-3.9.9\bin\mvn spring-boot:run -DskipTests -pl notification-service"

echo Starting React Frontend...
start "React Frontend" cmd /k "cd frontend && npm install && npm run dev"

echo All Spring Boot microservices and the React Frontend have been launched!
