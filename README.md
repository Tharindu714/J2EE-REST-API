# 🌐 J2EE REST API Module

> A JAX-RS–powered RESTful API built with Java EE, providing CRUD endpoints under `com.tharindu.web.rest` for core domain entities.

---

## 📑 Table of Contents

1. [✨ Project Overview](#-project-overview)
2. [📂 Project Structure](#-project-structure)
3. [⚙️ Technologies & Dependencies](#️-technologies--dependencies)
4. [🚀 Getting Started](#-getting-started)
5. [🔧 Configuration](#-configuration)
6. [📄 REST Endpoints](#-rest-endpoints)
7. [🛡️ Error Handling & Validation](#-error-handling--validation)
8. [📚 Testing](#-testing)
9. [🤝 Contributing](#-contributing)
10. [📜 License](#-license)

---

## ✨ Project Overview

This module exposes a set of RESTful services using **JAX-RS** under the Java EE platform.
All resources live in `com.tharindu.web.rest` and support JSON serialization for easy integration with front-end apps or external clients.

Key features:

* **CRUD operations** for your domain entities
* **Annotation-driven** resource configuration (@Path, @GET, @POST, etc.)
* **Content negotiation** (JSON input/output)
* **Exception mappers** for uniform error responses

---

## 📂 Project Structure

```
src/main/java/com/tharindu/web/rest/
├── Home.java     
├── RESTApplicationPath.java 
├── User.java          
└── UserResource.java
```

---

## ⚙️ Technologies & Dependencies

* **Java EE / Jakarta EE** (JAX-RS, CDI)
* **Maven** for project management
* **Jersey** (reference implementation) or your server’s built-in JAX-RS
* **JSON-B** / Jackson for JSON binding
* **JUnit 5** / REST-assured for testing

---

## 🚀 Getting Started

### Prerequisites

* JDK 11+
* Maven 3.6+
* Java EE–compliant application server (TomEE, WildFly, GlassFish)

### Clone & Build

```bash
git clone https://github.com/Tharindu714/J2EE-REST-API.git
cd J2EE-REST-API
mvn clean package
```

### Deploy

1. Copy the generated WAR (`target/rest-api.war`) to your server’s `deployments/` folder.
2. Start or restart the server.
3. The API base URL is:

   ```
   http://localhost:8080/<context-root>/api
   ```

---

## 🔧 Configuration

* **`ApplicationConfig.java`** (or `web.xml` alternative):

  ```java
  @ApplicationPath("/api")
  public class ApplicationConfig extends Application {}
  ```
* **MIME Types**: By default, endpoints produce/consume `application/json`.
* **CORS**: If needed, implement a `ContainerResponseFilter` to add CORS headers.

---

## 📄 REST Endpoints

Below is an overview of each resource under `com.tharindu.web.rest.resource`:

| Resource              | HTTP Method | Path              | Description                         |
| --------------------- | ----------- | ----------------- | ----------------------------------- |
| `EntityAResource`     | GET         | `/entitiesA`      | List all EntityA records            |
|                       | POST        | `/entitiesA`      | Create a new EntityA                |
|                       | GET         | `/entitiesA/{id}` | Retrieve EntityA by ID              |
|                       | PUT         | `/entitiesA/{id}` | Update EntityA record               |
|                       | DELETE      | `/entitiesA/{id}` | Delete EntityA by ID                |
| `EntityBResource`     | GET/POST/…  | `/entitiesB`      | Similar CRUD operations for EntityB |
| `HealthCheckResource` | GET         | `/health`         | Service health/status endpoint      |

> **Note:** Replace `EntityA`/`EntityB` with your actual resource names.

---

## 🛡️ Error Handling & Validation

* **Validation**: Use `@Valid` on method parameters and Bean Validation annotations (`@NotNull`, `@Size`) in DTOs.
* **Exception Mappers**:

  * `NotFoundExceptionMapper` → returns `404 Not Found` for missing records.
  * `ValidationExceptionMapper` → returns `400 Bad Request` with detailed validation errors.

---

## 📚 Testing

* **Unit Tests**: JUnit 5 for individual resource logic.
* **Integration Tests**: REST-assured or Jersey Test Framework.

Example (REST-assured):

```java
@Test
public void testGetEntitiesA() {
  given()
    .when().get("/api/entitiesA")
    .then().statusCode(200)
    .body("size()", greaterThanOrEqualTo(0));
}
```

---

## 🤝 Contributing

1. Fork the repo.
2. Create a feature branch: `git checkout -b feat/resource-X`
3. Commit changes: `git commit -m "feat: add ResourceX endpoints"`
4. Push and open a PR.

Please adhere to coding standards and include tests for new endpoints.

---

## 📜 License

This project is licensed under the **MIT License**. See [LICENSE](LICENSE) for details.

---

> Built with ☕️ and 🔗 by Tharindu714
