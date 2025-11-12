## EduMate Backend

### Prerequisites

- Java 17+
- Maven 3.9+
- MySQL (port 3306)
- MongoDB (port 27017)

### Configuration

Default config: `src/main/resources/application.yml`

- MySQL: jdbc:mysql://localhost:3306/edumate_db (root/password)
- MongoDB: mongodb://localhost:27017/edumate_mongo
- Server: http://localhost:3001
- CORS: http://localhost:5173
- JWT: secret and expiration are configurable

You can override via env vars or a local `application-local.yml` and run with `--spring.profiles.active=local`.

### Setup

1. Start MySQL and MongoDB
2. Ensure MySQL user/password in `application.yml` are valid

### Run

```bash
mvn spring-boot:run
```

API base: `http://localhost:3001`

### Auth API

- POST `/api/auth/register` { firstName,lastName,email,password }
- POST `/api/auth/login` { email,password }
- GET `/api/auth/verify` header `Authorization: Bearer <token>`

Response: `{ token, user: { id, firstName, lastName, email } }`

### Notes

- Schema auto-updates via JPA (ddl-auto=update)
- Passwords hashed (BCrypt)
