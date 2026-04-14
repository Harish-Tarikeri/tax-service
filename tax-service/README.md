# Tax Gap Detection & Compliance Validation Service

## 📌 Overview

This application processes financial transactions, validates them, calculates expected tax, detects tax gaps, and applies compliance rules. It also generates exceptions and maintains audit logs.

---

## ⚙️ Tech Stack

* Java 17
* Spring Boot
* PostgreSQL
* Gradle
* JUnit & Mockito

---

## 🚀 How to Run the Application

### 1. Clone the Repository

```bash
git clone https://github.com/<your-username>/tax-service.git
cd tax-service
```

### 2. Build the Project

If using Gradle:

```bash
./gradlew build
```

### 3. Run the Application



Using Gradle:

```bash
./gradlew bootRun
```

Application will start on:

```
http://localhost:8080
```

---

## 🗄️ Database Setup (PostgreSQL)

### 1. Create Database

Login to PostgreSQL and run:

```sql
CREATE DATABASE taxdb;
```

### 2. Update application.properties

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/taxdb
spring.datasource.username=postgres
spring.datasource.password=yourpassword

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

---

## 📬 API Endpoints

### ➤ Upload Transactions

**POST** `/api/transactions/upload`

---

## 📥 Sample JSON Request

```json
[
  {
    "transactionId": "TXN001",
    "customerId": "CUST001",
    "amount": 1000,
    "tax": 100,
    "transactionType": "SALE",
    "date": "2024-04-10"
  },
  {
    "transactionId": "TXN002",
    "customerId": "CUST002",
    "amount": 2000,
    "tax": 150,
    "transactionType": "PURCHASE",
    "date": "2024-04-11"
  }
]
```

---

## 🔗 Sample CURL Command

```bash
curl -X POST http://localhost:8080/api/transactions/upload \
-H "Content-Type: application/json" \
-d @sample.json
```

---

## 📮 Postman Usage

1. Open Postman
2. Select **POST**
3. Enter URL:

```
http://localhost:8080/api/transactions/upload
```

4. Go to **Body → raw → JSON**
5. Paste sample JSON
6. Click **Send**

---

## 📊 Features

* Transaction validation
* Tax calculation
* Rule engine processing
* Exception handling
* Audit logging

---

## 📌 Author

Harish Tarikeri
