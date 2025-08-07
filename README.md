
# 🏦 Virtual Banking System

  

A scalable, secure **Virtual Banking System** built using **Java microservices**, a **Backend for Frontend (BFF)** layer, and **WSO2 API Gateway**. The system provides essential banking functionalities such as user registration, authentication, account management, and transaction handling.

  

---

  

## 📌 Project Goals

  

- Develop a **microservices-based** virtual banking system.

- Implement the **BFF pattern** to serve frontend apps efficiently.

- Use **WSO2 API Gateway** for centralized authentication, rate limiting, and API management.

- Apply real-world **asynchronous logging** with **Kafka**.

- Gain end-to-end hands-on experience with a production-style architecture.

  

---

  

## 🧱 Core Architecture Overview

  

### 🧩 1. Microservices (Spring Boot)

Each domain (Users, Accounts, Transactions, Logging) is isolated and independently deployable:

- Decoupled services

- RESTful APIs

- Domain-driven responsibilities

  

### 🧑‍💻 2. Backend for Frontend (BFF)

- Aggregates data from multiple services

- Serves tailored responses for frontend needs

- Reduces network chattiness and coupling between frontend and backend

  

### 🔐 3. WSO2 API Gateway

- Centralized entry point for all external API access

- Handles OAuth2, API Key-based authentication

- Supports throttling, analytics, and request/response transformations

- Routes requests to respective microservices or BFF

  

### 🪵 4. Kafka-Based Logging Microservice

- Produces request/response logs from each service

- Consumes logs into a centralized database via Kafka

- Enables real-time auditing and debugging

  

---

  

## 🗺️ High-Level Architecture Diagram

  



<img width="2493" height="3840" alt="Untitled diagram _ Mermaid Chart-2025-08-07-122257" src="https://github.com/user-attachments/assets/fbb208fe-b85d-41d2-869e-46964e62f132" />

  

---

  

## ⚙️ Microservices Overview

  

### 👤 User Service

Manages user registration, login, and profile data.

  

Endpoints:

-  `POST /users/register`

-  `POST /users/login`

-  `GET /users/{userId}/profile`

  

---

  

### 💳 Account Service

Manages accounts and balances.

  

Endpoints:

-  `POST /accounts`

-  `GET /accounts/{accountId}`

-  `GET /users/{userId}/accounts`

-  `PUT /accounts/transfer`

  

🕒 **Scheduled Job**: Marks accounts as inactive if idle for more than 24h (runs hourly)

  

---

  

### 💸 Transaction Service

Handles money movement between accounts and tracks history.

  

Endpoints:

-  `POST /transactions/transfer/initiation`

-  `POST /transactions/transfer/execution`

-  `GET /accounts/{accountId}/transactions`

  

---

  

### 🔄 BFF Service

Aggregates and orchestrates calls across services for frontend apps.

  

Endpoint:

-  `GET /bff/dashboard/{userId}`

→ Returns: Profile + all user accounts + recent transactions

  

---

  

### 📊 Logging Microservice

- Consumes logs from Kafka topic

- Parses and inserts into a centralized **dump table**

  

Log Message Format:

```json

{

"message": "{...escaped JSON...}",

"messageType": "Request or Response",

"dateTime": "2025-06-30T10:10:00Z"

}

```

  

---

  

## 🛡️ API Gateway (WSO2)

  

All client calls pass through the WSO2 API Manager:

  

| API Name | Resource Path | Backend Route |

|---------------|------------------------------------|-----------------------------------|

| auth | `/auth/register` | `/users/register` |

| auth | `/auth/login` | `/users/login` |

| Dashboard | `/dashboard/{userId}` | `/bff/dashboard/{userId}` |

| Transactions | `/transactions/transfer/initiation`<br>`/transactions/transfer/execution` | `/bff/transactions/...` |

| API Product | `/vbank/...` (bundles all above) | --- |

  

> 🔐 All APIs secured with **OAuth2** and **API Keys**

  

### Applications

-  `vbank-portal`: Handles traffic from web portal

-  `vbank-mobile`: Handles traffic from mobile app

---

  

## 🧪 API Examples

  

### Register a User

```http

POST /register

{

"username": "john.doe",

"password": "securePassword123",

"email": "john@example.com"

}

```

  

### Transfer Funds

```http

POST /transactions/transfer/initiation

{

"fromAccountId": "...",

"toAccountId": "...",

"amount": 50.00

}

```

  

---

  

## 🛠️ Tech Stack

  

| Area | Technology |

|-----------------------|--------------------------|

| Language | Java |

| Framework | Spring Boot |

| API Gateway | WSO2 API Manager |

| Communication | REST, Kafka |

| Auth | OAuth2, API Key |

| DBs | PostgreSQL |

| Containerization | Docker, Docker Compose |

| Testing | Postman |

  

---

  

## 🧠 Key Learnings

  

- Designed an end-to-end secure and scalable microservices architecture

- Implemented advanced orchestration using the BFF pattern

- Built real-time centralized logging using Kafka

- Applied API Gateway patterns: rate-limiting, request routing, authentication

  

---

  

## 🚀 Getting Started

  

```bash

# Clone the repo

git  clone  https://github.com/AhmedMagedC/Virtual-Bank-System.git

cd  Virtual-Bank-System

  

# Build & run using Docker Compose

docker-compose  up  --build

```
