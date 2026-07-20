Stop-Process -Name java -Force -ErrorAction SilentlyContinue
Stop-Process -Name node -Force -ErrorAction SilentlyContinue

Write-Host "Starting Discovery Server..."
Start-Process -FilePath "C:\maven\apache-maven-3.9.9\bin\mvn.cmd" -ArgumentList "spring-boot:run -DskipTests -pl discovery-server" -RedirectStandardOutput "discovery.log" -RedirectStandardError "discovery.err" -WindowStyle Hidden
Start-Sleep -Seconds 15

Write-Host "Starting API Gateway..."
Start-Process -FilePath "C:\maven\apache-maven-3.9.9\bin\mvn.cmd" -ArgumentList "spring-boot:run -DskipTests -pl api-gateway" -RedirectStandardOutput "gateway.log" -RedirectStandardError "gateway.err" -WindowStyle Hidden
Start-Sleep -Seconds 10

Write-Host "Starting Domain Services..."
Start-Process -FilePath "C:\maven\apache-maven-3.9.9\bin\mvn.cmd" -ArgumentList "spring-boot:run -DskipTests -pl identity-service" -RedirectStandardOutput "identity.log" -RedirectStandardError "identity.err" -WindowStyle Hidden
Start-Process -FilePath "C:\maven\apache-maven-3.9.9\bin\mvn.cmd" -ArgumentList "spring-boot:run -DskipTests -pl product-catalog-service" -RedirectStandardOutput "product.log" -RedirectStandardError "product.err" -WindowStyle Hidden
Start-Process -FilePath "C:\maven\apache-maven-3.9.9\bin\mvn.cmd" -ArgumentList "spring-boot:run -DskipTests -pl inventory-service" -RedirectStandardOutput "inventory.log" -RedirectStandardError "inventory.err" -WindowStyle Hidden
Start-Process -FilePath "C:\maven\apache-maven-3.9.9\bin\mvn.cmd" -ArgumentList "spring-boot:run -DskipTests -pl order-service" -RedirectStandardOutput "order.log" -RedirectStandardError "order.err" -WindowStyle Hidden
Start-Process -FilePath "C:\maven\apache-maven-3.9.9\bin\mvn.cmd" -ArgumentList "spring-boot:run -DskipTests -pl notification-service" -RedirectStandardOutput "notification.log" -RedirectStandardError "notification.err" -WindowStyle Hidden

Write-Host "Starting Frontend..."
Set-Location C:\INTERN_B2B\frontend
Start-Process -FilePath "npm.cmd" -ArgumentList "run dev" -RedirectStandardOutput "frontend.log" -RedirectStandardError "frontend.err" -WindowStyle Hidden
Set-Location C:\INTERN_B2B

Write-Host "Waiting 80 seconds for microservices to register with Eureka..."
Start-Sleep -Seconds 80

Write-Host "Running Demo Script..."
powershell -ExecutionPolicy Bypass -File C:\INTERN_B2B\run-demo.ps1

Write-Host 'Keeping process alive so background services do not terminate...'
while($true) { Start-Sleep -Seconds 3600 }
