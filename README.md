# Smart Campus Sensor & Room Management API

## 📌 Project Overview
This project is a high-performance RESTful API designed for the **5COSC022W Client-Server Architectures** coursework. It provides a digital twin of a smart campus environment, allowing for efficient management of physical spaces and IoT devices.

**Key Capabilities:**
*   Manage campus rooms (Create, List, Retrieve, Delete).
*   Register IoT sensors and associate them with specific rooms.
*   Record and track historical sensor readings.
*   Dynamic sensor filtering based on device types.
*   Robust error management with structured JSON feedback.
*   Comprehensive HTTP traffic logging for auditing.

The system is built using the **JAX-RS (Jersey)** framework. In accordance with the coursework requirements, it utilizes an in-memory persistence layer, ensuring no external database dependencies while focusing on core architectural principles.

---

## 🎓 Coursework Context
This API implements the Smart Campus scenario defined in the coursework brief. It focuses on three core entities:
*   **Room:** A physical location defined by its ID, name, capacity, and the set of sensors it contains.
*   **Sensor:** A device monitoring a specific room, characterized by its type, operational status, and real-time value.
*   **SensorReading:** A time-stamped data event representing a recorded value from a sensor.

**Design Priorities:**
*   Adherence to RESTful maturity (Resource hierarchy & sub-resources).
*   Strict input validation and business logic enforcement.
*   Safe exception handling through specialized mappers.

---

## 🛠 Technology Stack
*   **Java:** Version 11 or higher
*   **Standard:** JAX-RS (javax.ws.rs)
*   **Implementation:** Jersey 2.40
*   **Build Tool:** Maven
*   **Deployment:** Apache Tomcat 9.0 (Java EE 8)
*   **IDE:** NetBeans

---

### 📂 Project Structure
```text
SmartCampusAPI/
├── pom.xml
├── README.md
├── nb-configuration.xml
├── src/
│   └── main/
│       ├── java/com/smartcampus/
│       │   ├── app/
│       │   │   └── SmartCampusApplication.java
│       │   ├── core/
│       │   │   ├── domain/
│       │   │   │   ├── room/
│       │   │   │   │   └── Room.java
│       │   │   │   ├── sensor/
│       │   │   │   │   └── Sensor.java
│       │   │   │   └── reading/
│       │   │   │       └── SensorReading.java
│       │   │   └── errors/
│       │   │       ├── LinkedResourceNotFoundException.java
│       │   │       ├── RoomNotEmptyException.java
│       │   │       └── SensorUnavailableException.java
│       │   ├── infrastructure/
│       │   │   ├── persistence/
│       │   │   │   └── DataStore.java
│       │   │   └── web/
│       │   │       └── LoggingFilter.java
│       │   └── api/
│       │       ├── endpoints/
│       │       │   ├── DiscoveryResource.java
│       │       │   ├── RoomResource.java
│       │       │   ├── SensorResource.java
│       │       │   ├── SensorReadingResource.java
│       │       │   ├── GlobalExceptionMapper.java
│       │       │   ├── LinkedResourceNotFoundExceptionMapper.java
│       │       │   ├── RoomNotEmptyExceptionMapper.java
│       │       │   └── SensorUnavailableExceptionMapper.java
│       │       └── response/
│       │           └── ErrorResponse.java
│       └── webapp/
│           ├── index.html
│           ├── META-INF/
│           │   └── context.xml
│           └── WEB-INF/
│               └── web.xml
```

---

## 📡 API Design Summary

**Base URL:** `http://localhost:8080/SmartCampusAPI/api/v1`

| Method | Endpoint | Description |
|:-------|:---------|:------------|
| GET | `/` | API Metadata & Discovery |
| GET | `/rooms` | Retrieve all registered rooms |
| POST | `/rooms` | Add a new campus room |
| DELETE | `/rooms/{id}` | Remove a room (only if empty) |
| GET | `/sensors` | List all sensors (supports `?type=` filter) |
| POST | `/sensors` | Register a new sensor |
| GET | `/sensors/{id}/readings` | Fetch historical data for a sensor |
| POST | `/sensors/{id}/readings` | Record a new measurement |

---

## 🚀 Build and Run Instructions

### Prerequisites
1.  Java 11+ installed and configured.
2.  Apache Tomcat 9.x server available.

### Manual Deployment
1.  Clone the repository and navigate to the project root.
2.  Run the Maven build:
    ```bash
    mvn clean package
    ```
3.  Copy `target/SmartCampusAPI.war` to the Tomcat `webapps` folder.
4.  Start the Tomcat server.

---

## 🧪 Sample curl Commands

**1. API Discovery**
```bash
curl -X GET http://localhost:8080/SmartCampusAPI/api/v1/
```

**2. Create a New Room**
```bash
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/rooms \
  -H "Content-Type: application/json" \
  -d '{"id":"R101","name":"Main Lab","location":"Block A","capacity":30}'
```

**3. Register a Sensor**
```bash
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors \
  -H "Content-Type: application/json" \
  -d '{"id":"S101","type":"TEMP","status":"ACTIVE","roomId":"R101"}'
```

---

## 📝 Conceptual Report (Q&A)

**Q1: What is the default lifecycle of a JAX-RS resource class?**
By default, JAX-RS resources follow a "per-request" lifecycle. The framework instantiates a new object for every incoming HTTP request. While this prevents state leakage between requests within the class, it means shared data must be stored externally (like our `DataStore` singleton) to remain persistent across multiple calls.

**Q2: Why is Hypermedia (HATEOAS) considered important?**
HATEOAS allows an API to be self-guiding. By including navigation links in the response, the server tells the client what actions are currently available. This decouples the client from hardcoded URLs, making the system more flexible and easier to update without breaking existing client implementations.

**Q3: Implications of returning IDs vs full objects in lists?**
Returning only IDs minimizes bandwidth and speeds up initial requests, but forces the client to make multiple follow-up calls to get details. Returning full objects provides all data at once, reducing latency but increasing the response size. For small-scale systems like this campus API, full objects are generally more efficient.

**Q4: Is the DELETE operation idempotent in this API?**
Yes. Whether you call DELETE once or ten times, the final state of the server is the same: the resource is gone. The first call returns success, while subsequent calls return a 404, but the server's state does not change further, satisfying the definition of idempotency.

**Q5: Sending the wrong Content-Type to a @Consumes(APPLICATION_JSON) endpoint?**
The JAX-RS runtime performs an early check on the `Content-Type` header. If a client sends `text/plain` to an endpoint expecting JSON, the server will immediately issue an HTTP 415 (Unsupported Media Type) response before the method logic is even executed.

**Q6: Why use @QueryParam instead of a path segment for filtering?**
Path segments are meant to identify specific resources or hierarchies. Filtering (like selecting sensors by type) is a "view" or a "query" on an existing collection, not a new resource. Query parameters are the standard RESTful way to provide optional parameters for searching, sorting, and filtering.

**Q7: Benefits of the Sub-Resource Locator pattern?**
It promotes modularity. Instead of having one massive class for all sensor-related logic, the locator delegates reading-specific operations to a separate `SensorReadingResource`. This makes the code cleaner, more readable, and easier to maintain as the API expands.

**Q8: Why should a POST to a reading also update the parent sensor's currentValue?**
To ensure "Data Integrity." If a new reading is recorded, the sensor's current state must reflect that measurement. If they aren't synchronized, the API would provide conflicting information, confusing the client and rendering the "current value" field useless.

**Q9: Why is 422 more accurate than 404 for a missing roomId in a POST?**
An HTTP 404 implies the endpoint itself is missing. However, in this case, the endpoint exists, but the data provided in the request body is logically invalid (referencing a non-existent room). HTTP 422 (Unprocessable Entity) correctly signals that the request is well-formed but contains semantic errors.

**Q10: Cybersecurity risks of exposing Java stack traces?**
Stack traces reveal internal implementation details, such as package names, third-party library versions, and potential vulnerabilities in the code logic. Attackers can use this "information leakage" to map out the system and plan targeted exploits. Standardized JSON error messages hide these details while still informing the client of the error type.

---
*Developed for 5COSC022W Coursework - University of Westminster.*
