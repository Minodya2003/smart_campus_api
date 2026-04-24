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

The coursework specification requires a public GitHub repository, a README with API overview, build/run instructions, and sample curl commands, while the conceptual report answers must also be organised in the README.md. It also explicitly restricts the implementation to JAX-RS only, forbids database use, and warns against submitting a ZIP instead of hosting the project on GitHub.

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
*   **core/** — Pure domain logic: models and custom exceptions, no framework dependencies.
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
| GET | `/api/v1/sensors` | List all sensors |
| GET | `/api/v1/sensors?type=CO2` | Filter sensors by type |
| POST | `/api/v1/sensors` | Create a new sensor linked to a room |
| GET | `/api/v1/sensors/{sensorId}/readings` | Get readings history for a sensor |
| POST | `/api/v1/sensors/{sensorId}/readings` | Append a new reading and update the sensor's current value |

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
7.  Open the API base URL in a browser or Postman.

**Typical base URL:**
`http://localhost:8080/SmartCampusAPI/api/v1`

If your IDE deploys the app under a different context path such as `smart-campus-api`, then use:
`http://localhost:8080/smart-campus-api/api/v1`

### Option 2: Build with Maven and Deploy Manually
1.  Clone the repository:
    `git clone https://github.com/Minodya2003/smart_campus_api.git`
2.  cd `smart_campus_api`
3.  Build the project:
    `mvn clean package`
4.  Locate the generated WAR file:
    `target/SmartCampusAPI.war`
5.  Copy the WAR file into Tomcat's `webapps` folder.
6.  Start Tomcat.
7.  Access the API in the browser or Postman.

---

## 🧪 Sample curl Commands
Replace the base URL if your deployment context path differs.

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

**3. List All Rooms**
```bash
curl -X GET http://localhost:8080/SmartCampusAPI/api/v1/rooms
```

**4. Get a Room by ID**
```bash
curl -X GET http://localhost:8080/SmartCampusAPI/api/v1/rooms/R1
```

**5. Create a Sensor**
```bash
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors \
  -H "Content-Type: application/json" \
  -d '{"id":"S1","type":"Temperature","status":"ACTIVE","currentValue":22.5,"roomId":"R1"}'
```

**6. Filter Sensors by Type**
```bash
curl -X GET "http://localhost:8080/SmartCampusAPI/api/v1/sensors?type=Temperature"
```

**7. Add a Sensor Reading**
```bash
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors/S1/readings \
  -H "Content-Type: application/json" \
  -d '{"value":24.8}'
```

**8. Get Reading History**
```bash
curl -X GET http://localhost:8080/SmartCampusAPI/api/v1/sensors/S1/readings
```

**9. Try to Delete a Room That Still Has Sensors**
```bash
curl -X DELETE http://localhost:8080/SmartCampusAPI/api/v1/rooms/R1
```

---

## ⚠️ Error Handling Strategy
The API uses dedicated exception classes under `core/errors/` and exception mappers under `api/endpoints/` to avoid default server error pages and to return structured JSON error bodies.

**Implemented Custom Error Scenarios:**
*   **409 Conflict** — deleting a room that still contains assigned sensors.
*   **422 Unprocessable Entity** — creating a sensor with a `roomId` that does not exist.
*   **403 Forbidden** — posting a reading to a sensor in `MAINTENANCE`.
*   **500 Internal Server Error** — unexpected unhandled runtime failures.

**Example Error JSON:**
```json
{
  "status": 409,
  "error": "Conflict",
  "message": "Room cannot be deleted because it still contains sensors"
}
```

---

## 📝 Logging
A custom logging filter in `infrastructure/web/LoggingFilter.java` captures:
*   The HTTP method and request URI for every incoming request.
*   The final HTTP status code for every outgoing response.

**Example:**
*   Incoming Request: `GET http://localhost:8080/SmartCampusAPI/api/v1/rooms`
*   Outgoing Response: `HTTP 200`

---

## 🚫 Limitations
*   No persistence layer is used, so all data is reset when the server restarts.
*   The current code uses `ConcurrentHashMap` and synchronized collections which are safe for concurrent workloads.
*   The current implementation returns created entities directly, but for stronger REST practice a POST could explicitly return 201 Created with a Location header.
*   The current readings implementation is modular and nested by path, but not written as a classic sub-resource locator method.

---

## 📝 Conceptual Report (Q&A)

**Q1: What is the default lifecycle of a JAX-RS resource class?**
By default, JAX-RS resources follow a "per-request" lifecycle. The framework instantiates a new object for every incoming HTTP request. While this prevents state leakage between requests within the class, it means shared data must be stored externally (like our `DataStore` singleton) to remain persistent across multiple calls.

**Q2: Why is Hypermedia (HATEOAS) considered important?**
Hypermedia means the API response tells the client what it can do next by including links. Instead of the client hardcoding every URL from external docs, the server guides navigation. This makes the API more self-describing and reduces tight coupling. If a URL changes, clients following links adapt automatically. Our discovery endpoint at GET /api/v1/ does a basic version of this by exposing the main resource paths in one place.

**Q3: Implications of returning only IDs vs full objects in lists?**
Returning only IDs keeps the response small and fast, good for large datasets. But the client then needs extra requests to get each room's details, which adds latency. Returning full objects is more convenient — everything is in one response — but the payload is bigger. For this coursework the dataset is small so returning full room objects is the better choice.

**Q4: Is the DELETE operation idempotent in this API?**
Yes. Whether you call DELETE once or ten times, the final state of the server is the same: the resource is gone. The first call returns success, while subsequent calls return a 404, but the server's state does not change further, satisfying the definition of idempotency.

**Q5: Sending the wrong Content-Type to a @Consumes(APPLICATION_JSON) endpoint?**
JAX-RS checks the Content-Type header before even calling the resource method. If it doesn't match application/json, the framework immediately rejects the request with HTTP 415 Unsupported Media Type. The method code never runs.

**Q6: Why use @QueryParam instead of a path segment for filtering?**
The path /api/v1/sensors represents the sensors collection. Adding ?type=CO2 is just a filter on that same collection — it doesn't identify a different resource. Using a path like /sensors/type/CO2 implies "type" is a sub-resource, which is conceptually wrong. Query parameters are also composable and keep the base URI clean.

**Q7: Benefits of the Sub-Resource Locator pattern?**
Instead of cramming all nested endpoints into one big class, the sub-resource locator delegates /sensors/{id}/readings to a dedicated SensorReadingResource class. This gives separation of concerns — sensor logic and reading logic stay in different files. Each class becomes smaller and easier to maintain.

**Q8: Why should a POST to a reading also update the parent sensor's currentValue?**
Because the two values represent the same real-world fact from different angles. If POST /sensors/S1/readings adds a new measurement but GET /sensors/S1 still shows the old currentValue, the API is internally inconsistent. Updating currentValue on every successful reading POST keeps the system consistent.

**Q9: Why is HTTP 422 more semantically accurate than 404 when a referenced roomId doesn't exist?**
404 Not Found means the requested URL doesn't exist. But here the URL is valid — the problem is inside the JSON body where roomId references something that doesn't exist. 422 Unprocessable Entity means the syntax is fine but the content is semantically wrong.

**Q10: Cybersecurity risks of exposing Java stack traces?**
A raw stack trace leaks internal details like class names, package structure, and framework versions. An attacker can use this to fingerprint the tech stack and identify vulnerabilities. The safe approach is to log the full trace internally and return only a generic 500 Internal Server Error message to the client.

---
*Developed for 5COSC022W Coursework - University of Westminster.*
