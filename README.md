\# Uber Ride Booking System



A backend ride-booking system inspired by Uber, built using Java, Spring Boot, Redis, Apache Kafka, and PostgreSQL. The project follows a microservices-based architecture to separate ride management, driver location tracking, and driver matching.



\## Tech Stack



\* \*\*Java\*\* — Backend development

\* \*\*Spring Boot\*\* — REST APIs and application services

\* \*\*Redis\*\* — Driver location management and nearby-driver lookup

\* \*\*Apache Kafka\*\* — Asynchronous communication between services

\* \*\*PostgreSQL\*\* — Persistent ride data storage

\* \*\*Docker Compose\*\* — Local Kafka and ZooKeeper setup

\* \*\*Maven\*\* — Build and dependency management



\## Architecture



The application consists of three backend microservices.



\### 1. Location Service



\* Manages driver location-related operations.

\* Uses Redis for location data.

\* Provides nearby-driver lookup functionality.

\* Port: `8082`



\### 2. Ride Service



\* Handles ride-related operations.

\* Uses PostgreSQL for persistent storage.

\* Publishes and consumes ride-related Kafka events.

\* Port: `8083`



\### 3. Matching Service



\* Handles driver-matching logic.

\* Consumes ride request events through Kafka.

\* Communicates with the Location Service to retrieve nearby-driver information.

\* Port: `8084`



\### Communication Flow



1\. A ride request is created through the Ride Service.

2\. Ride-related events are communicated using Apache Kafka.

3\. The Matching Service processes the event.

4\. The Matching Service requests nearby-driver information from the Location Service.

5\. The services communicate through their configured APIs and Kafka events.



\## Project Structure



```text

uber-ride-booking/

├── location-service/

│   └── src/

├── matching-service/

│   └── src/

├── ride-service/

│   └── src/

├── docker-compose.yml

└── README.md

```



\## Prerequisites



Install the following before running the project:



\* Java 21

\* Docker Desktop

\* PostgreSQL

\* Redis



\## Local Setup



\### 1. Clone the repository



```bash

git clone https://github.com/shivam123450/uber-ride-booking.git

cd uber-ride-booking

```



\### 2. Start Kafka and ZooKeeper



From the project root, run:



```bash

docker compose up -d

```



This starts the Kafka and ZooKeeper containers defined in `docker-compose.yml`.



Kafka is configured to use port `9092`.



\### 3. Configure PostgreSQL



Create a PostgreSQL database named:



```sql

CREATE DATABASE userapp;

```



Make sure PostgreSQL is running on port `5432`.



Set the database password as an environment variable in PowerShell:



```powershell

$env:DB\_PASSWORD = Read-Host "Enter PostgreSQL password"

```



The Ride Service reads its database password from `DB\_PASSWORD`.



\### 4. Start Redis



Make sure Redis is running on `localhost:6379`, as configured in the Location Service.



\### 5. Build the services



Open separate PowerShell terminals from the project root.



Build the Location Service:



```powershell

cd location-service

.\\mvnw.cmd clean package

```



Build the Ride Service:



```powershell

cd ride-service

.\\mvnw.cmd clean package

```



Build the Matching Service:



```powershell

cd matching-service

.\\mvnw.cmd clean package

```



\### 6. Run the services



Set `DB\_PASSWORD` in the terminal used to start the Ride Service, then run each service in its own terminal:



```powershell

cd location-service

.\\mvnw.cmd spring-boot:run

```



```powershell

cd ride-service

.\\mvnw.cmd spring-boot:run

```



```powershell

cd matching-service

.\\mvnw.cmd spring-boot:run

```



Ensure PostgreSQL, Redis, and Kafka are running before starting the services.



\## Service Ports



| Service          | Port |

| ---------------- | ---: |

| Location Service | 8082 |

| Ride Service     | 8083 |

| Matching Service | 8084 |

| Kafka            | 9092 |

| Redis            | 6379 |

| PostgreSQL       | 5432 |



\## Key Learning Areas



\* Building REST APIs with Spring Boot

\* Structuring a backend using microservices

\* Using Redis for location-related operations

\* Implementing asynchronous event communication with Kafka

\* Persisting ride data with PostgreSQL

\* Configuring and running services in a local development environment



\## Future Improvements



\* Add authentication and authorization.

\* Implement ride lifecycle management.

\* Improve driver availability and matching strategies.

\* Add integration tests and API documentation.

\* Containerize all microservices.

\* Introduce monitoring and centralized logging.



\## Author



\*\*Shivam Vishwakarma\*\*



GitHub: https://github.com/shivam123450



