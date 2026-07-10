# URL Shortener Service

A production-ready, containerized URL shortening service built with **Spring Boot 3** and **PostgreSQL**. This service allows users to transform long, unwieldy URLs into clean, trackable short links.

---

## Features

* **Smart Shortening:** Instantly convert long URLs into unique, high-entropy short codes.
* **Custom Vanity Aliases:** Allow users to define branding-friendly custom slugs (e.g., `/promo2026`).
* **Instant Redirections:** Zero-latency HTTP 302 redirection from the short code to the target destination.
* **Real-time Analytics:** Tracks click metrics passively on every redirect.
* **Time-To-Live (TTL) Expiration:** Optional link expiration timestamps.
* **Automated System Hygiene:** A scheduled daily background worker that purges expired records to optimize database performance.

---

## Technology Stack

| Layer | Technology | Purpose |
| :--- | :--- | :--- |
| **Backend Framework** | Java 17 / Spring Boot 3 | Core application ecosystem |
| **Data Access** | Spring Data JPA / Hibernate | Object-Relational Mapping |
| **Primary Database** | PostgreSQL | Persistent storage for production |
| **In-Memory Database**| H2 Database | Fast, zero-config local development & testing |
| **Frontend UI** | Thymeleaf + HTML5/CSS3 | Simple manual control dashboard |
| **Containerization** | Docker & Docker Compose | Uniform local environment replication |

---
## Prerequisites
Ensure you have the following installed:
* [Docker Desktop](https://www.docker.com/products/docker-desktop/) (includes Docker Compose)
* [Git](https://git-scm.com/)

### One-Command Deployment

1. **Clone the repository:**
```text
git clone <your-repository-url>
cd <repository-directory>
```
2. **Boot the entire stack:**
```text
docker-compose up --build
```
This command compiles the Java binary, builds the isolated application container, spins up a PostgreSQL instance, connects them via an internal network, and mounts a persistent data volume.

3. **Access the Service:**
```bash
Web Dashboard:
Go to `http://localhost:8080` in your browser.
```

4. **Tearing Down:**
```bash
# Stop containers safely
docker-compose down

# Stop containers and wipe the database volume cleanly
docker-compose down -v

```