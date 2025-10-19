# Real-Time Log Analyzer & Alerter 

A Java-based monitoring tool built with Spring Boot that actively parses and analyzes application logs in real-time to provide immediate system health insights and trigger alerts on high-error-rate conditions.

This project demonstrates a practical application of multi-threading, real-time data processing, and system observability principles.

---

##  Key Features

- **Real-Time Log Tailing**: Uses Java's `WatchService` API to efficiently monitor a log file for new entries, similar to the `tail -f` command in Linux.
- **Log Parsing Engine**: Employs regular expressions to parse raw log strings into structured `LogEntry` objects (timestamp, level, message).
- **In-Memory Analytics**: Calculates system health metrics, such as the error rate over a rolling 60-second time window, using thread-safe data structures (`ConcurrentLinkedDeque`).
- **Configurable Alerting System**: A scheduled service runs every 10 seconds to check metrics against predefined thresholds, triggering stateful `ALERT` and `RESOLVED` notifications to the console.
- **REST API Endpoint**: Exposes a simple `/api/status` endpoint to provide a real-time JSON view of the system's health.

---

## Tech Stack

- **Language**: Java 21
- **Framework**: Spring Boot 3
- **Build Tool**: Maven

---

## Getting Started

Follow these instructions to get the project up and running on your local machine.

### Prerequisites

- JDK 21 or later
- Apache Maven
- Git

### How to Run

1.  **Clone the repository:**
    ```bash
    git clone [https://github.com/YourUsername/real-time-log-analyzer-java.git](https://github.com/YourUsername/real-time-log-analyzer-java.git)
    cd real-time-log-analyzer-java
    ```

2.  **Create the log file:**
    The application watches a file named `app.log`. Create an empty file with this name in the project's root directory.
    ```bash
    touch app.log
    ```

3.  **Run the Log Analyzer Application:**
    Open the project in your IDE and run the main class `LogleanalyzerApplication.java`, or use the Maven wrapper:
    ```bash
    ./mvnw spring-boot:run
    ```
    The analyzer is now running and watching `app.log` for changes.

4.  **Run the Log Simulator (in a separate terminal):**
    To generate sample logs, run the `LogSimulator.java` class. This will start writing new lines to `app.log` every couple of seconds.
    ```bash
    # Make sure you are in the project's root directory in a new terminal
    ./mvnw exec:java -Dexec.mainClass="com.Emmanuel.loganalyzer.LogSimulator"
    ```

---

## How to Use

### 1. Observe the Console Output

Watch the console where the main application is running. You will see:
- Real-time calculations of the error rate.
- Formatted `ALERT TRIGGERED` messages when the error rate exceeds the 50% threshold.
- `ALERT RESOLVED` messages when the error rate falls back to a normal level.

### 2. Check the API Endpoint

While the application is running, you can check its status via the REST API.

- **URL**: `GET http://localhost:8080/api/status`
- **Example Healthy Response (`OK`):**
  ```json
  {
    "errorRateLastMinute": 0.15,
    "totalErrors": 5,
    "status": "OK"
  }
