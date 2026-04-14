# Tax Gap Detection & Compliance Validation Service

## 📌 Overview

This application processes financial transactions, validates them, calculates expected tax, detects discrepancies (tax gaps), applies rule-based compliance checks, and generates audit logs and exception records.

---

## ⚙️ Tech Stack

* Java 17
* Spring Boot
* PostgreSQL
* JPA / Hibernate
* Maven / Gradle
* JUnit & Mockito

---

## 🚀 How to Run the Application

### 1. Clone Repository

```bash
git clone https://github.com/<your-username>/tax-service.git
cd tax-service
```

### 2. Build Project


Using Gradle:

```bash
./gradlew build
```

### 3. Run Application

```bash
./gradlew bootRun
```

Application runs at:

```
http://localhost:8080
```

---

## 🗄️ Database Setup (PostgreSQL)

### Create Database

```sql
CREATE DATABASE taxdb;
```

### Update application.properties

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/taxdb
spring.datasource.username=postgres
spring.datasource.password=yourpassword

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

---

## 📬 API Endpoint

### Upload Transactions

POST `/api/transactions/upload`

---

## 📥 Sample JSON

```json
[
  {
    "transactionId": "TXN001",
    "customerId": "CUST001",
    "amount": 1000,
    "tax": 100,
    "transactionType": "SALE",
    "date": "2024-04-10"
  }
]
```

---

## 🔗 Sample CURL

```bash
curl -X POST http://localhost:8080/api/transactions/upload \
-H "Content-Type: application/json" \
-d @sample-data.json
```

---

## 📮 Postman Steps

1. Open Postman
2. Select POST
3. URL: http://localhost:8080/api/transactions/upload
4. Body → raw → JSON
5. Paste sample JSON
6. Click Send

---

## 📊 Features

* Transaction validation
* Tax gap detection
* Rule engine checks
* Exception generation
* Audit logging

---

## 📌 Author

Harish Tarikeri
