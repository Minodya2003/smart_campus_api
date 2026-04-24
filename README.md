# Smart Campus Sensor & Room Management API

## 📝 Overview
This project is a RESTful API developed for the **5COSC022W Client-Server Architectures** coursework. It models a simple smart campus environment in which:
*   Rooms can be created, listed, retrieved, and deleted.
*   Sensors can be registered and linked to rooms.
*   Sensor readings can be stored and retrieved.
*   Sensors can be filtered by type.
*   Errors are returned as structured JSON responses.
*   Incoming requests and outgoing responses are logged.

The system is implemented using **JAX-RS (Jersey)** and stores all data in memory using Java collections. This means the project intentionally does not use a database, and all stored data is lost when the application restarts.

---

## 🎓 Coursework Context
This API is based on the Smart Campus scenario in the coursework brief. The system models three main resources:
*   **Room:** A physical campus room with an ID, name, capacity, and a list of assigned sensor IDs.
*   **Sensor:** A device assigned to a room, with an ID, type, status, current value, and room reference.
*   **SensorReading:** A historical reading event with an ID, timestamp, and numeric value.

**The specification focuses on:**
*   RESTful design principles.
*   Resource hierarchy and nested resources.
*   Validation and business rules.
*   Exception mapping and safe error handling.
*   Request/response logging.

---

## 🛠 Technology Stack
*   **Java 11+**
*   **JAX-RS / javax.ws.rs**
*   **Jersey 2.40**
*   **Maven**
*   **Apache Tomcat 9.0** (Servlet 4.0 / Java EE 8)
*   **NetBeans IDE**

---

## 🏗 Project Architecture
The project follows a domain-driven layered structure organised into four top-level layers:
*   **app/** — Application bootstrap and JAX-RS entry point.
*   **core/** — Pure domain logic: models and custom exceptions.
*   **infrastructure/** — Technical concerns: in-memory persistence and HTTP logging.
*   **api/** — REST layer: endpoint resources, exception mappers, and response DTOs.

---

## 📂 Project Structure
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

**Base Path:** `/api/v1`

| Method | Endpoint | Description |
|:-------|:---------|:------------|
| GET | `/api/v1/` | Discovery endpoint with version and resource map |
| GET | `/api/v1/rooms` | List all rooms |
| POST | `/api/v1/rooms` | Create a room |
| GET | `/api/v1/rooms/{id}` | Fetch one room |
| DELETE | `/api/v1/rooms/{id}` | Delete a room if no sensors remain assigned |
| GET | `/api/v1/sensors` | List all sensors (supports `?type=` filter) |
| POST | `/api/v1/sensors` | Create a new sensor linked to a room |
| GET | `/api/v1/sensors/{id}/readings` | Get readings history for a sensor |
| POST | `/api/v1/sensors/{id}/readings` | Append a new reading and update the sensor's value |

---

## 💾 Data Storage Strategy
The application uses in-memory collections in `DataStore` under `infrastructure/persistence/`:
*   `Map<String, Room> rooms`
*   `Map<String, Sensor> sensors`
*   `Map<String, List<SensorReading>> readings`

This approach is appropriate for the coursework because the brief explicitly forbids database use and expects storage using structures such as `HashMap` and `ArrayList`.

---

## 🚀 How to Build and Run

### Option 1: Run with NetBeans and Tomcat
1.  Install Java 11 or higher.
2.  Install Apache Tomcat 9.0 (Servlet 4.0 compatible).
3.  Open the project in NetBeans.
4.  Make sure Tomcat is added as a server in NetBeans.
5.  Clean and build the project.
6.  Deploy the WAR to Tomcat through NetBeans.
7.  Open the API base URL: `http://localhost:8080/SmartCampusAPI/api/v1/`

### Option 2: Build with Maven and Deploy Manually
1.  Build the project: `mvn clean package`
2.  Locate the WAR file: `target/SmartCampusAPI.war`
3.  Copy the WAR file into Tomcat's `webapps` folder.
4.  Start Tomcat and access the API.

---

## 🧪 Sample curl Commands

**1. Discovery Endpoint**
```bash
curl -X GET http://localhost:8080/SmartCampusAPI/api/v1/
```

**2. Create a Room**
```bash
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/rooms \
  -H "Content-Type: application/json" \
  -d '{"id":"R1","name":"Lab 1","capacity":40}'
```

**3. Create a Sensor**
```bash
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors \
  -H "Content-Type: application/json" \
  -d '{"id":"S1","type":"Temperature","status":"ACTIVE","roomId":"R1"}'
```

**4. Add a Sensor Reading**
```bash
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors/S1/readings \
  -H "Content-Type: application/json" \
  -d '{"value":24.8}'
```

---

## ⚠️ Error Handling Strategy
The API uses dedicated exception classes under `core/errors/` and exception mappers under `api/endpoints/` to avoid default server error pages and to return structured JSON error bodies.

**Implemented Custom Error Scenarios:**
*   **409 Conflict:** Deleting a room that still contains assigned sensors.
*   **422 Unprocessable Entity:** Creating a sensor with a `roomId` that does not exist.
*   **403 Forbidden:** Posting a reading to a sensor in `MAINTENANCE`.
*   **500 Internal Server Error:** Unexpected unhandled runtime failures.

---

## 📝 Logging
A custom logging filter in `infrastructure/web/LoggingFilter.java` captures:
*   The HTTP method and request URI for every incoming request.
*   The final HTTP status code for every outgoing response.

**Example:**
*   Incoming Request: `GET http://localhost:8080/api/v1/rooms`
*   Outgoing Response: `HTTP 200`

---

## 🚫 Limitations
*   No persistence layer is used, so all data is reset when the server restarts.
*   The current implementation returns created entities directly, but for stronger REST practice a POST could explicitly return 201 Created with a Location header.
*   The current readings implementation is modular and nested by path, but could be further optimized.

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

**Q9: Why is HTTP 422 more semantically accurate than 404 when a referenced roomId doesn't exist?**
An HTTP 404 implies the endpoint itself is missing. However, in this case, the endpoint exists, but the data provided in the request body is logically invalid (referencing a non-existent room). HTTP 422 (Unprocessable Entity) correctly signals that the request is well-formed but contains semantic errors.

**Q10: Cybersecurity risks of exposing Java stack traces?**
Stack traces reveal internal implementation details, such as package names, third-party library versions, and potential vulnerabilities in the code logic. Attackers can use this "information leakage" to map out the system and plan targeted exploits. Standardized JSON error messages hide these details while still informing the client of the error type.

---
*Developed for 5COSC022W Coursework - University of Westminster.*
