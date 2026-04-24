# SmartCampusAPI

A JAX-RS REST API for managing smart campus resources (Rooms and Sensors) built for the 5COSC022W - Client-Server Architectures coursework at the University of Westminster.

## Technology Stack

*   **Framework:** Jersey 2.40
*   **API Standard:** JAX-RS (javax.ws.rs)
*   **Web Server:** Apache Tomcat 9.x
*   **Language:** Java 11
*   **Build Tool:** Maven

## How to Build and Run

1.  **Prerequisites:** Install Java 11 or above and Apache Tomcat 9.x. Also, ensure you have Maven installed.
2.  **Build the Project:** Open a terminal in the root directory of the project and run:
    ```bash
    mvn clean package
    ```
    This will generate a `SmartCampusAPI.war` file in the `target/` directory.
3.  **Deployment:** Copy the generated `SmartCampusAPI.war` file into the `webapps` folder of your Tomcat 9 installation. (Note: The WAR deploys specifically to Tomcat 9 due to the underlying `javax` namespace support).
4.  **Start Tomcat:** Start the Tomcat server. The API will be accessible at:
    `http://localhost:8080/SmartCampusAPI/api/v1/`

## Limitations

*   This project currently uses an in-memory `DataStore` for persisting data. Data will be lost upon server restart. (A database implementation would be required for a real-world scenario).
