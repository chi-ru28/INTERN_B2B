# B2B E-Commerce Platform Demo Script
Write-Host "=============================================" -ForegroundColor Cyan
Write-Host "  Enterprise B2B E-Commerce Platform Demo" -ForegroundColor Cyan
Write-Host "=============================================" -ForegroundColor Cyan

# Wait a moment to ensure API Gateway is up
Write-Host "Checking if API Gateway is responding..." -ForegroundColor Yellow
$env:Path = [System.Environment]::GetEnvironmentVariable("Path","Machine") + ";" + [System.Environment]::GetEnvironmentVariable("Path","User")
$gatewayUrl = "http://localhost:8888/api/products"

# 1. Create Products
Write-Host "`n1. Creating Products in MongoDB (Product Catalog Service)..." -ForegroundColor Yellow
$products = @(
    @{ name="ThinkPad X1 Carbon Gen 11"; description="Premium business laptop"; price=1499.99; skuCode="LAPTOP-TP-X1" },
    @{ name="ThinkVision P27h-20"; description="27-inch QHD Monitor"; price=359.99; skuCode="MONITOR-TV-P27" },
    @{ name="Lenovo USB-C Dock Gen 2"; description="Universal docking station"; price=199.99; skuCode="DOCK-USBC-G2" }
)

foreach ($product in $products) {
    $json = $product | ConvertTo-Json
    $response = Invoke-RestMethod -Uri $gatewayUrl -Method Post -Body $json -ContentType "application/json"
    Write-Host "  [+] Created Product: $($response.name) (SKU: $($response.skuCode))" -ForegroundColor Green
}

# 2. Insert Inventory
Write-Host "`n2. Populating Inventory in PostgreSQL (Inventory Service)..." -ForegroundColor Yellow
$sql = "
TRUNCATE TABLE t_inventory;
INSERT INTO t_inventory (sku_code, quantity) VALUES ('LAPTOP-TP-X1', 50);
INSERT INTO t_inventory (sku_code, quantity) VALUES ('MONITOR-TV-P27', 100);
INSERT INTO t_inventory (sku_code, quantity) VALUES ('DOCK-USBC-G2', 200);
"
docker exec b2b-postgres psql -U b2b_user -d b2b_ecommerce -c $sql
Write-Host "  [+] Inventory populated successfully for all SKUs." -ForegroundColor Green


# 3. Check Inventory
Write-Host "`n3. Checking Inventory levels via API Gateway..." -ForegroundColor Yellow
$checkInvUrl = "http://localhost:8888/api/inventory/check-stock?skuCode=LAPTOP-TP-X1&skuCode=MONITOR-TV-P27"
$invResponse = Invoke-RestMethod -Uri $checkInvUrl -Method Get
Write-Host "  [?] Are LAPTOP-TP-X1 and MONITOR-TV-P27 in stock? " -NoNewline
Write-Host $invResponse -ForegroundColor Green

# 4. Place an Order
Write-Host "`n4. Authenticating and Placing an Order via API Gateway..." -ForegroundColor Yellow

# Login first to get JWT token
$loginUrl = "http://localhost:8888/api/v1/auth/login"
$loginPayload = @{
    email = "patelruchi2830@gmail.com"
    password = "password123"
} | ConvertTo-Json

Write-Host "  [+] Logging in as patelruchi2830@gmail.com..." -ForegroundColor Green
$loginResponse = Invoke-RestMethod -Uri $loginUrl -Method Post -Body $loginPayload -ContentType "application/json"
$token = $loginResponse.token

$orderUrl = "http://localhost:8888/api/orders"
$orderPayload = @{
    orderLineItemsList = @(
        @{ skuCode = "LAPTOP-TP-X1"; price = 1499.99; quantity = 2 },
        @{ skuCode = "MONITOR-TV-P27"; price = 359.99; quantity = 4 }
    )
} | ConvertTo-Json -Depth 4

$orderResponse = Invoke-RestMethod -Uri $orderUrl -Method Post -Body $orderPayload -ContentType "application/json" -Headers @{ "Authorization" = "Bearer $token" }
Write-Host "  [+] Order Service Response: $orderResponse" -ForegroundColor Green

Write-Host "`n5. Check Notification Service console window!" -ForegroundColor Yellow
Write-Host "  The Order Service just published an event to Apache Kafka, and the Notification Service should have logged 'Received Notification for Order - ...'" -ForegroundColor Cyan

Write-Host "`nDemo Complete!" -ForegroundColor Green
