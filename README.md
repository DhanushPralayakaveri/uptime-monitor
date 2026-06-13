# 🛡️ Uptime Monitor SaaS

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Angular](https://img.shields.io/badge/Angular-DD0031?style=for-the-badge&logo=angular&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Neon](https://img.shields.io/badge/Neon_DB-00E599?style=for-the-badge&logo=neon&logoColor=black)

**Infrastructure telemetry, simplified.** A full-stack, enterprise-grade monitoring platform that tracks mission-critical endpoints in real-time, calculates SLA uptime percentages, and dispatches automated downtime alerts.

---

## ✨ Enterprise Features

* **Real-Time Polling Engine:** A dedicated, asynchronous Java background worker continuously pings monitored endpoints without blocking main application threads.
* **Instant Downtime Alerts:** Integrated SMTP support dispatches critical failure emails the millisecond an endpoint fails a health check.
* **SLA Analytics:** Automatically calculates and tracks historical response times (ms) and uptime percentages.
* **Zero-Friction UI:** Angular frontend utilizes Optimistic UI updates and manual change detection for 0ms visual latency when deploying new monitors.
* **Stateless Security:** Fully secured API using stateless JWT (JSON Web Tokens) and BCrypt password hashing.

## 🏗️ Architecture

This project is built using a decoupled microservice architecture:
* **Frontend:** Angular 17 SPA featuring a responsive dark-mode UI, custom authentication guards, and RxJS observables.
* **Backend:** Spring Boot 3 REST API managing business logic, secure user context via `Principal`, and the scheduled telemetry engine.
* **Database:** Serverless PostgreSQL (Neon) integrated via Spring Data JPA and Hibernate for seamless object-relational mapping.

---

## 📸 Dashboard Preview
*(Add a screenshot of your beautiful dark-mode dashboard here by dragging and dropping an image into the GitHub editor!)*
<img src="" width="800" alt="Dashboard Preview">

---

## 🚀 Getting Started (Local Development)

### Prerequisites
* Node.js & Angular CLI
* Java 17+ & Maven
* PostgreSQL database (or a free cloud instance like Neon.tech)
* A Gmail App Password (for SMTP email alerts)

### 1. Clone the Repository
```bash
git clone [https://github.com/DhanushPralayakaveri/uptime-monitor.git](https://github.com/DhanushPralayakaveri/uptime-monitor.git)
cd uptime-monitor
