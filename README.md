# BTG Pactual – API Gestión de Fondos de Inversión

API REST construida con **Java 21 + Spring Boot** para la gestión de fondos de inversión BTG Pactual. Almacenamiento NoSQL en **MongoDB**, notificaciones por **AWS SES** (email) y **AWS SNS** (SMS), autenticación con **JWT** y despliegue mediante **AWS CloudFormation**.

---

## Tecnologías

| Capa | Tecnología |
|------|-----------|
| Lenguaje | Java 21 |
| Framework | Spring Boot 4 |
| Base de datos | MongoDB |
| Seguridad | Spring Security + JWT |
| Notificaciones | AWS SES (email) + AWS SNS (SMS) |
| Infraestructura | AWS CloudFormation + ECS Fargate |
| Build | Gradle |

---

## Arquitectura

El proyecto sigue **Clean Architecture / Arquitectura Hexagonal**:
```
src/main/java/com/btg/funds_api/
├── application/
│   └── usecase/          ← Casos de uso (lógica de negocio)
├── domain/
│   ├── exception/        ← Excepciones de dominio
│   ├── gateway/          ← Interfaces (puertos)
│   └── model/            ← Modelos de dominio
└── infrastructure/
    ├── adapter/          ← Implementaciones de gateways
    ├── config/           ← Configuración Spring / AWS
    ├── controller/       ← Controllers REST
    ├── exception/        ← Manejo global de excepciones
    ├── mapper/           ← MapStruct mappers
    ├── persistence/      ← Repositorios MongoDB
    └── security/         ← JWT Filter + Security Config
```

---

## Modelo de datos NoSQL (MongoDB)

### Colección `users`
```json
{
  "_id": "uuid",
  "name": "string",
  "email": "string",
  "phone": "string",
  "password": "bcrypt hash",
  "balance": 500000.0,
  "notificationPreference": "EMAIL | SMS",
  "role": "ROLE_USER | ROLE_ADMIN"
}
```

### Colección `funds`
```json
{
  "_id": "1",
  "name": "FPV_BTG_PACTUAL_RECAUDADORA",
  "minAmount": 75000.0,
  "category": "FPV | FIC"
}
```

### Colección `transactions`
```json
{
  "_id": "uuid",
  "userId": "string",
  "fundId": "string",
  "fundName": "string",
  "amount": 75000.0,
  "type": "OPEN | CANCEL",
  "date": "2025-01-01T00:00:00"
}
```

---

## Fondos disponibles

| ID | Nombre | Monto mínimo | Categoría |
|----|--------|-------------|-----------|
| 1 | FPV_BTG_PACTUAL_RECAUDADORA | COP $75.000 | FPV |
| 2 | FPV_BTG_PACTUAL_ECOPETROL | COP $125.000 | FPV |
| 3 | DEUDAPRIVADA | COP $50.000 | FIC |
| 4 | FDO-ACCIONES | COP $250.000 | FIC |
| 5 | FPV_BTG_PACTUAL_DINAMICA | COP $100.000 | FPV |

---

## Requisitos

- Java 21+
- Gradle 9+
- MongoDB 7+ (local o Docker)
- Docker (opcional)
- AWS CLI (para despliegue en AWS)

---

## Ejecución local

### 1. Clonar el repositorio
```bash
git clone <url-repositorio>
cd funds-api
```

### 2. Levantar MongoDB con Docker
```bash
docker run -d --name mongodb -p 27017:27017 mongo:latest
```

### 3. Configurar variables de entorno
```bash
export AWS_REGION=us-east-1
export SES_FROM_EMAIL=no-reply@btgpactual.com
export AWS_ACCESS_KEY_ID=<tu-access-key>
export AWS_SECRET_ACCESS_KEY=<tu-secret-key>
```

### 4. Ejecutar la aplicación
```bash
./gradlew bootRun
```

La API queda disponible en `http://localhost:8080`

> Los 5 fondos se insertan automáticamente en MongoDB al arrancar.

---

## Endpoints

### Autenticación (público)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `POST` | `/auth/register` | Registrar nuevo usuario |
| `POST` | `/auth/login` | Iniciar sesión |

**Registro:**
```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Yeiderson",
    "email": "y@test.com",
    "phone": "3001234567",
    "password": "Pass123!",
    "notificationPreference": "EMAIL"
  }'
```

**Respuesta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "id": "abc123",
  "name": "Yeiderson",
  "role": "ROLE_USER"
}
```

**Login:**
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "y@test.com",
    "password": "Pass123!"
  }'
```

### Fondos (requiere JWT)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/funds` | Listar todos los fondos |
```bash
curl http://localhost:8080/funds \
  -H "Authorization: Bearer <token>"
```

### Suscripciones (requiere JWT)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `POST` | `/subscriptions` | Suscribirse a un fondo |
| `POST` | `/subscriptions/cancel` | Cancelar suscripción |

**Suscribirse:**
```bash
curl -X POST http://localhost:8080/subscriptions \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"fundId": "1"}'
```

**Cancelar:**
```bash
curl -X POST http://localhost:8080/subscriptions/cancel \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"fundId": "1"}'
```

### Transacciones (requiere JWT)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/transactions` | Historial de transacciones |
```bash
curl http://localhost:8080/transactions \
  -H "Authorization: Bearer <token>"
```

---

## Errores comunes

| HTTP | Descripción |
|------|-------------|
| `400` | Saldo insuficiente o campos inválidos |
| `401` | Token inválido o expirado |
| `403` | Sin permisos |
| `404` | Usuario o fondo no encontrado |
| `409` | Ya tiene suscripción activa al fondo |

---

## Pruebas unitarias
```bash
./gradlew test
```

---

## Despliegue en AWS

Ver sección [CloudFormation](#cloudformation) abajo.
```bash
aws cloudformation deploy \
  --template-file infrastructure/cloudformation.yml \
  --stack-name btg-funds-api \
  --parameter-overrides \
    DockerImage=<ECR_URI>:latest \
    MongoUri=<MONGODB_URI> \
    JwtSecret=<JWT_SECRET> \
  --capabilities CAPABILITY_NAMED_IAM \
  --region us-east-1
```