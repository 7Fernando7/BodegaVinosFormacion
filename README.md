# Bodega Vininho - Arquitectura Hexagonal

Proyecto de gestión de bodega de vinos implementado con arquitectura hexagonal (Ports & Adapters).

## 🏗️ Arquitectura

```
src/main/java/bodegavininho/
├── domain/                    # Capa de dominio (puro, sin dependencias externas)
│   ├── model/                 # Entidades del dominio
│   │   ├── Wine.java          # Entidad principal del vino
│   │   ├── StockItem.java     # Item de inventario
│   │   └── WineOrder.java     # Pedido de vinos
│   └── port/                  # Interfaces (puertos)
│       ├── WineRepository.java         # Puerto para persistencia
│       └── ExternalWineProvider.java   # Puerto para API externa
├── application/               # Capa de aplicación
│   ├── dto/                   # Data Transfer Objects
│   │   ├── WineDTO.java
│   │   └── CreateWineRequest.java
│   └── usecase/               # Casos de uso
│       ├── WineUseCase.java
│       └── ImportExternalWinesUseCase.java
└── infrastructure/            # Capa de infraestructura
    ├── adapter/
    │   └── OpenFoodFactsWineAdapter.java  # Adaptador API externa
    ├── controller/
    │   └── WineController.java            # Controlador REST
    └── VinoApplication.java              # Punto de entrada
```

## 🚀 Características

- **Gestión de Vinos**: CRUD completo de vinos con tipos (Tinto, Blanco, Rosado)
- **Control de Stock**: Inventario con alertas de stock bajo
- **Pedidos**: Creación y gestión de pedidos de vinos
- **Integración Externa**: Consumo de API Open Food Facts
- **API REST**: Endpoints para todas las operaciones

## 📡 Endpoints API

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/wines` | Lista todos los vinos |
| GET | `/api/wines/{id}` | Obtiene un vino por ID |
| GET | `/api/wines/search?name=` | Busca vino por nombre |
| POST | `/api/wines` | Crea un nuevo vino |
| PUT | `/api/wines/{id}/stock?quantity=` | Actualiza stock |
| DELETE | `/api/wines/{id}` | Elimina un vino |
| POST | `/api/wines/import` | Importa vinos desde Open Food Facts |
| POST | `/api/wines/import/barcode/{barcode}` | Importa por código de barras |

## 📮 Ejemplos Postman / cURL

### 1. Obtener todos los vinos

**cURL:**
```bash
curl -X GET "http://localhost:8080/api/wines"
```

**Postman:**
```
GET http://localhost:8080/api/wines
```

---

### 2. Obtener un vino por ID

**cURL:**
```bash
curl -X GET "http://localhost:8080/api/wines/{id}"
```

**Postman:**
```
GET http://localhost:8080/api/wines/{{wineId}}
```

---

### 3. Buscar vino por nombre

**cURL:**
```bash
curl -X GET "http://localhost:8080/api/wines/search?name=El%20Coto"
```

**Postman:**
```
GET http://localhost:8080/api/wines/search?name=El%20Coto
```

---

### 4. Crear un nuevo vino

**cURL:**
```bash
curl -X POST "http://localhost:8080/api/wines" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "El Coto",
    "year": 2020,
    "price": 15.99,
    "stock": 50,
    "country": "La Rioja",
    "type": "RED"
  }'
```

**Postman:**
```
POST http://localhost:8080/api/wines
Content-Type: application/json

{
  "name": "El Coto",
  "year": 2020,
  "price": 15.99,
  "stock": 50,
  "country": "La Rioja",
  "type": "RED"
}
```

**Ejemplo de respuesta:**
```json
{
  "id": "abc123",
  "name": "El Coto",
  "year": 2020,
  "price": 15.99,
  "stock": 50,
  "country": "La Rioja",
  "type": "RED",
  "typeDisplayName": "Vino Tinto"
}
```

---

### 5. Actualizar stock de un vino

**cURL:**
```bash
curl -X PUT "http://localhost:8080/api/wines/{id}/stock?quantity=100"
```

**Postman:**
```
PUT http://localhost:8080/api/wines/{{wineId}}/stock?quantity=100
```

---

### 6. Eliminar un vino

**cURL:**
```bash
curl -X DELETE "http://localhost:8080/api/wines/{id}"
```

**Postman:**
```
DELETE http://localhost:8080/api/wines/{{wineId}}
```

---

### 7. Importar vinos desde Open Food Facts

**cURL:**
```bash
curl -X POST "http://localhost:8080/api/wines/import"
```

**Postman:**
```
POST http://localhost:8080/api/wines/import
```

---

### 8. Importar vino por código de barras

**cURL:**
```bash
curl -X POST "http://localhost:8080/api/wines/import/barcode/5000112637719"
```

**Postman:**
```
POST http://localhost:8080/api/wines/import/barcode/5000112637719
```

---

## 📋 Colección Postman

También puedes importar la colección desde `postman_collection.json`:

1. Abre Postman
2. Click en "Import"
3. Selecciona el archivo `postman_collection.json`
4. Configura la variable `baseUrl` como `http://localhost:8080`
5. Ejecuta las peticiones

## 🧪 Tests

```bash
# Ejecutar todos los tests
mvn test

# Ver cobertura
mvn test jacoco:report
```

### Archivos de Test

| Ubicación | Descripción |
|-----------|-------------|
| `test/java/.../domain/model/WineTest.java` | Tests de entidad Wine |
| `test/java/.../domain/model/StockItemTest.java` | Tests de entidad StockItem |
| `test/java/.../domain/model/WineOrderTest.java` | Tests de entidad WineOrder |
| `test/java/.../infrastructure/repository/InMemoryWineRepositoryTest.java` | Tests del repositorio |
| `test/java/.../application/usecase/WineUseCaseTest.java` | Tests del caso de uso |

## 🛠️ Tecnologías

- **JDK 21** - Oracle JDK
- **Spring Boot 3.3.0** - Framework web
- **Maven** - Gestión de dependencias
- **JUnit 5** - Tests unitarios
- **SLF4J/Logback** - Logging

## 📦 Dependencias Principales

```xml
<!-- Spring Boot -->
spring-boot-starter-web
spring-boot-starter-validation

<!-- Logging -->
slf4j-api
logback-classic

<!-- Tests -->
junit-jupiter-api
junit-jupiter-params
```

## ▶️ Ejecutar la Aplicación

```bash
# Compilar
mvn clean package

# Ejecutar
java -jar target/Vino-1.0-SNAPSHOT.jar

# O usando Maven
mvn spring-boot:run
```

## 📁 Estructura de Archivos

```
Vino/
├── pom.xml                          # Configuración Maven
├── README.md                        # Este archivo
└── src/
    ├── main/
    │   ├── java/bodegavininho/
    │   │   ├── VinoApplication.java
    │   │   ├── domain/
    │   │   ├── application/
    │   │   └── infrastructure/
    │   └── resources/
    │       └── logback.xml
    └── test/
        └── java/bodegavininho/
            ├── domain/model/
            ├── infrastructure/repository/
            └── application/usecase/
```

## 🔄 Flujo de Datos

```
Controlador (REST) 
    → Caso de Uso (Application)
        → Puerto (Interfaz)
            → Adaptador (Infraestructura)
                → Dominio (Entidades)
```

## 📝 Notas

- El dominio es completamente independiente de frameworks
- Los puertos definen contratos que la infraestructura implementa
- Los DTOs separan la capa de API del dominio
- Los tests verifican la lógica de negocio sin dependencias externas
