# SmartCampusAPI

A robust JAX-RS REST API for managing smart campus resources (Rooms and Sensors) built for the **5COSC022W - Client-Server Architectures** coursework at the University of Westminster.

## 🚀 Features

*   **Comprehensive Resource Management:** CRUD operations for Rooms and Sensors.
*   **Sub-resource Pattern:** Sensor readings are managed as sub-resources of specific sensors (`/sensors/{id}/readings`).
*   **Global Exception Handling:** Custom `ExceptionMappers` ensure all errors return standardized JSON responses.
*   **Thread Safety:** Concurrent data access is handled using `ConcurrentHashMap` and synchronized collections.
*   **Request/Response Logging:** A custom `LoggingFilter` logs all incoming requests and outgoing responses.
*   **API Discovery:** A root endpoint provides basic API metadata.

## 🛠 Technology Stack

*   **Language:** Java 11
*   **API Standard:** JAX-RS (javax.ws.rs)
*   **Framework:** Jersey 2.40
*   **Web Server:** Apache Tomcat 9.x
*   **Data Format:** JSON (Jackson)
*   **Build Tool:** Maven

## 📂 Project Structure

*   `com.smartcampus.model`: Domain entities (Room, Sensor, SensorReading).
*   `com.smartcampus.resource`: JAX-RS resources defining the API endpoints.
*   `com.smartcampus.store`: In-memory `DataStore` using the Singleton pattern.
*   `com.smartcampus.exception`: Custom business exception classes.
*   `com.smartcampus.mapper`: Exception mappers for converting exceptions to JSON responses.
*   `com.smartcampus.filter`: Filters for logging and request processing.
*   `com.smartcampus.dto`: Data Transfer Objects (e.g., standardized ErrorResponse).

## 📡 API Endpoints

### Discovery
*   `GET /api/v1/` - Get API discovery metadata.

### Rooms
*   `GET /api/v1/rooms` - List all rooms.
*   `POST /api/v1/rooms` - Create a new room.
*   `GET /api/v1/rooms/{id}` - Get specific room details.
*   `DELETE /api/v1/rooms/{id}` - Delete a room (fails if sensors are attached).

### Sensors
*   `GET /api/v1/sensors` - List all sensors (supports filtering by `type`).
*   `POST /api/v1/sensors` - Create a new sensor (associates with a room).
*   `GET /api/v1/sensors/{id}` - Get specific sensor details.

### Sensor Readings
*   `GET /api/v1/sensors/{id}/readings` - Get history of readings for a sensor.
*   `POST /api/v1/sensors/{id}/readings` - Add a new sensor reading (updates sensor state).

## ⚠️ Error Handling

The API returns standardized JSON error responses:
```json
{
  "status": 404,
  "message": "Sensor not found: S101"
}
```
*   `400 Bad Request`: Validation errors or invalid relationships.
*   `403 Forbidden`: Business rule violations (e.g., sensor is offline).
*   `404 Not Found`: Resource does not exist.
*   `409 Conflict`: Duplicate IDs or invalid state transitions.

## 🏗 Build and Deployment

### 1. Build the WAR file
Run the following command in the project root:
```bash
mvn clean package
```
This generates `target/SmartCampusAPI.war`.

### 2. Deploy to Tomcat 9
Copy the `.war` file to your Tomcat `webapps` directory.

### 3. Access the API
The base URL will be:
`http://localhost:8080/SmartCampusAPI/api/v1/`

## 🧪 Testing with cURL

**Create a Room:**
```bash
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/rooms \
     -H "Content-Type: application/json" \
     -d '{"id":"R101", "name":"Main Lab", "location":"Block A", "capacity":30}'
```

**Add a Sensor:**
```bash
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors \
     -H "Content-Type: application/json" \
     -d '{"id":"S101", "type":"TEMPERATURE", "status":"ACTIVE", "roomId":"R101"}'
```

**Post a Reading:**
```bash
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors/S101/readings \
     -H "Content-Type: application/json" \
     -d '{"value":24.5, "timestamp":"2023-10-27T10:00:00Z"}'
```

---
*Developed as part of the Client-Server Architectures (5COSC022W) Coursework.*
