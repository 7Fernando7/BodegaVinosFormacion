# 🍷 BodegaVinosFormacion - Análisis Arquitectónico

## 📋 Resumen Ejecutivo

**BodegaVinosFormacion** es una aplicación de gestión de bodega de vinos desarrollada en **Spring Boot** que implementa una arquitectura hexagonal (Ports & Adapters). El sistema permite gestionar el inventario de vinos, procesar pedidos y importar productos desde fuentes externas como Open Food Facts.

---

## 🎯 Problema que Resuelve

1. **Gestión de Inventario**: Control de stock de vinos con alertas de bajo inventario
2. **Catálogo de Vinos**: Creación, búsqueda y mantenimiento de productos
3. **Pedidos**: Procesamiento de órdenes de compra con cálculo de totales
4. **Integración Externa**: Importación de vinos desde APIs externas mediante códigos de barras

---

## 🏗️ Arquitectura (Hexagonal / Ports & Adapters)

```
┌─────────────────────────────────────────────────────────────────┐
│                      APPLICATION LAYER                          │
│  ┌─────────────────┐  ┌─────────────────────────────────────┐   │
│  │   WineUseCase   │  │  ImportExternalWinesUseCase        │   │
│  └────────┬────────┘  └──────────────────┬────────────────────┘   │
│           │                            │                        │
│  ┌────────▼────────┐  ┌────────────────▼────────────────────┐   │
│  │   WineDTO       │  │  CreateWineRequest                │   │
│  │   (Record)      │  │  (Record)                          │   │
│  └─────────────────┘  └─────────────────────────────────────┘   │
└─────────────────────────────┬─────────────────────────────────┘
                              │
┌─────────────────────────────▼─────────────────────────────────┐
│                        DOMAIN LAYER                            │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │                    ENTITIES                            │   │
│  │  Wine ────► StockItem ◄──── WineOrder                   │   │
│  └─────────────────────────────────────────────────────────┘   │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │                    PORTS (Interfaces)                  │   │
│  │  WineRepository ◄────────────── ExternalWineProvider    │   │
│  └─────────────────────────────────────────────────────────┘   │
└─────────────────────────────┬─────────────────────────────────┘
                              │
┌─────────────────────────────▼─────────────────────────────────┐
│                    INFRASTRUCTURE LAYER                       │
│  ┌───────────────────┐  ┌─────────────────────────────────┐   │
│  │ CONTROLLERS       │  │ ADAPTERS & REPOSITORIES        │   │
│  │ WineController   │  │ InMemoryWineRepository         │   │
│  │   (REST API)     │  │ OpenFoodFactsWineAdapter      │   │
│  └───────────────────┘  └─────────────────────────────────┘   │
└───────────────────────────────────────────────────────────────┘
```

---

## 📦 Módulos Principales

### 1. Domain Layer (`domain/`)

**Entidades de Negocio:**

| Entidad | Responsabilidad | Clave |
|---------|-----------------|-------|
| [`Wine`](src/main/java/bodegavininho/domain/model/Wine.java:9) | Representa un vino con propiedades: nombre, año, precio, stock, país, tipo | Inmutable (builders) |
| [`StockItem`](src/main/java/bodegavininho/domain/model/StockItem.java:9) | Gestión de inventario con umbrales mínimos | Valor total del item |
| [`WineOrder`](src/main/java/bodegavininho/domain/model/WineOrder.java:12) | Pedido con items, estado y total | Calcula subtotales |

**Puertos (Interfaces):**

| Puerto | Propósito | Implementación |
|--------|-----------|-----------------|
| [`WineRepository`](src/main/java/bodegavininho/domain/port/WineRepository.java:10) | Persistencia de vinos | InMemoryWineRepository |
| [`ExternalWineProvider`](src/main/java/bodegavininho/domain/port/ExternalWineProvider.java:9) | Integración APIs externas | OpenFoodFactsWineAdapter |

### 2. Application Layer (`application/`)

**Casos de Uso:**

| Caso de Uso | Endpoint | Descripción |
|-------------|----------|-------------|
| [`WineUseCase`](src/main/java/bodegavininho/application/usecase/WineUseCase.java:17) | CRUD vinos | Create, Read, Update, Delete de vinos |
| [`ImportExternalWinesUseCase`](src/main/java/bodegavininho/application/usecase/ImportExternalWinesUseCase.java:16) | `/import*` | Importación masiva y por código de barras |

**DTOs:**

- [`WineDTO`](src/main/java/bodegavininho/application/dto/WineDTO.java:9) - Transferencia datos al cliente
- [`CreateWineRequest`](src/main/java/bodegavininho/application/dto/CreateWineRequest.java:6) - Payload de creación

### 3. Infrastructure Layer (`infrastructure/`)

**Controlador REST:**

[`WineController`](src/main/java/bodegavininho/infrastructure/controller/WineController.java:20) expone:

| Método | Endpoint | Acción | Tipo |
|--------|----------|--------|------|
| GET | `/api/wines` | Listar todos | Read |
| GET | `/api/wines/{id}` | Buscar por ID | Read |
| GET | `/api/wines/search?name=` | Buscar por nombre | Read |
| **GET** | **`/api/wines/external`** | **Previsualizar externos** | **Read (Preview)** |
| **GET** | **`/api/wines/external/barcode/{barcode}`** | **Preview por código** | **Read (Preview)** |
| POST | `/api/wines` | Crear vino | Write |
| PUT | `/api/wines/{id}/stock` | Actualizar stock | Write |
| DELETE | `/api/wines/{id}` | Eliminar | Write |
| **POST** | **`/api/wines/import`** | **Importar todos** | **Write (Import)** |
| **POST** | **`/api/wines/import/barcode/{barcode}`** | **Importar por código** | **Write (Import)** |

**Adaptadores:**

- [`InMemoryWineRepository`](src/main/java/bodegavininho/infrastructure/repository/InMemoryWineRepository.java:17) - Persistencia en memoria (HashMap)
- [`OpenFoodFactsWineAdapter`](src/main/java/bodegavininho/infrastructure/adapter/OpenFoodFactsWineAdapter.java:21) - Consume Open Food Facts API

---

## 🔄 Flujo de una Request

### Ejemplo: Crear un Vino

```
┌────┐     ┌───────┐     ┌─────────┐     ┌────────────┐     ┌────────────────┐
│HTTP│────►│CONTROL│────►│ USECASE │────►│ REPOSITORY │────►│ IN-MEMORY DB  │
│REQ │     │LAYER  │     │ LAYER   │     │ INTERFACE  │     │ (HashMap)     │
└────┘     └───────┘     └─────────┘     └────────────┘     └────────────────┘
               │              │               │                     │
               │              │               │                     │
           RestController  Orchestrates  Defines Contract        Stores Data
           Validates DTOs  Business      Abstraction            (Singleton)
           Maps to UseCase Logic         Interface
```

**Pasos detallados:**

1. **HTTP Request** → `POST /api/wines` con JSON body
2. **WineController** valida el [`CreateWineRequest`](src/main/java/bodegavininho/application/dto/CreateWineRequest.java:6) (record Java)
3. Llama a [`wineUseCase.createWine()`](src/main/java/bodegavininho/application/usecase/WineUseCase.java:27)
4. **WineUseCase** convierte el request a entidad [`Wine`](src/main/java/bodegavininho/domain/model/Wine.java:9)
5. Persiste vía [`WineRepository.save()`](src/main/java/bodegavininho/domain/port/WineRepository.java:11)
6. **InMemoryWineRepository** almacena en HashMap
7. Convierte entidad a [`WineDTO`](src/main/java/bodegavininho/application/dto/WineDTO.java:9)
8. **WineController** retorna `201 Created` con WineDTO

---

## 🔀 Patrón Preview vs Import

### Resumen de la Refactorización

Se ha separado la responsabilidad de **previsualización** (lectura) e **importación** (escritura):

| Verb | Endpoint | Descripción | Efecto Colateral |
|------|----------|-------------|------------------|
| GET | `/api/wines/external` | Lista vinos externos | ❌ Ninguno |
| GET | `/api/wines/external/barcode/{barcode}` | Detalle vino externo | ❌ Ninguno |
| POST | `/api/wines/import` | Importa todos los vinos | ✅ Guarda en DB |
| POST | `/api/wines/import/barcode/{barcode}` | Importa un vino por código | ✅ Guarda en DB |

### Firmas de los Nuevos Métodos

**[`ImportExternalWinesUseCase`](src/main/java/bodegavininho/application/usecase/ImportExternalWinesUseCase.java:16):**

```java
// Preview (Solo Lectura - Sin Efectos Colaterales)
public List<WineDTO> previewExternalWines()
public WineDTO previewByBarcode(String barcode)

// Import (Persistencia - Con Efectos Colaterales)  
public List<WineDTO> importAllWines()
public WineDTO importByBarcode(String barcode)
```

**[`WineController`](src/main/java/bodegavininho/infrastructure/controller/WineController.java:20):**

```java
// Endpoints de Preview (GET - Idempotente)
@GetMapping("/external")
public ResponseEntity<List<WineDTO>> previewExternalWines()

@GetMapping("/external/barcode/{barcode}")
public ResponseEntity<WineDTO> previewWineByBarcode(@PathVariable String barcode)

// Endpoints de Import (POST - Modifica Estado)
@PostMapping("/import")
public ResponseEntity<List<WineDTO>> importExternalWines()

@PostMapping("/import/barcode/{barcode}")
public ResponseEntity<WineDTO> importWineByBarcode(@PathVariable String barcode)
```

### Principios Aplicados

1. **Command-Query Separation (CQS)**: Los métodos de preview son queries (solo lectura), los de import son commands (escritura)
2. **Controlador Delgado**: El controlador solo recibe/retorna DTOs, sin lógica de negocio
3. **Single Responsibility**: Cada método tiene una responsabilidad única
4. **Arquitectura Hexagonal**: El dominio no conoce la infraestructura, los adapters son intercambiables

---

## ⭐ Partes Más Importantes para Entender el Sistema

| Prioridad | Archivo | Porqué |
|-----------|---------|--------|
| **1** | [`Wine.java`](src/main/java/bodegavininho/domain/model/Wine.java) | Entidad core del negocio - contiene lógica de dominio |
| **2** | [`WineController.java`](src/main/java/bodegavininho/infrastructure/controller/WineController.java) | Define todos los endpoints disponibles (CRUD + Preview + Import) |
| **3** | [`WineUseCase.java`](src/main/java/bodegavininho/application/usecase/WineUseCase.java) | Orquesta la lógica de negocio de vinos |
| **4** | [`ImportExternalWinesUseCase.java`](src/main/java/bodegavininho/application/usecase/ImportExternalWinesUseCase.java) | **Nuevo**: Separa preview (lectura) vs import (escritura) |
| **5** | [`WineRepository.java`](src/main/java/bodegavininho/domain/port/WineRepository.java) | Puerto que define abstracción de datos |
| **6** | [`ExternalWineProvider.java`](src/main/java/bodegavininho/domain/port/ExternalWineProvider.java) | Puerto para integración externa |

---

## 🚀 Mejoras para Producción

### 1. Persistencia de Datos
```
❌ Actual: InMemoryWineRepository (HashMap)
✅ Futuro: PostgreSQL/MySQL + Spring Data JPA
   - Agregar Flyway migrations
   - Conexión pool (HikariCP)
```

### 2. Manejo de Errores
```
❌ Actual: Excepciones genéricas, response 404
✅ Futuro:
   - GlobalExceptionHandler @ControllerAdvice
   - DTOs de error estandarizados
   - Códigos HTTP apropiados (400, 401, 403, 409...)
```

### 3. Validación
```
❌ Actual: Sin validación de entrada
✅ Futuro:
   - @Valid @NotNull en DTOs
   - Bean Validation (Jakarta EE)
   - Custom validators
```

### 4. Documentación API
```
❌ Actual: No existe
✅ Futuro:
   - Springdoc OpenAPI 3
   - Swagger UI en /swagger-ui.html
   - OpenAPI specs en /v3/api-docs
```

### 5. Testing
```
❌ Actual: Unit tests básicos
✅ Futuro:
   - Integration tests con @SpringBootTest
   - Testcontainers para BDD
   - Cobertura > 80%
   - Mutation testing (PIT)
```

### 6. Seguridad
```
❌ Actual: Sin autenticación
✅ Futuro:
   - Spring Security + JWT
   - Roles: ADMIN, USER
   - Rate limiting
   - CORS configurado
```

### 7. Observabilidad
```
❌ Actual: SLF4J básico
✅ Futuro:
   - Micrometer + Prometheus metrics
   - Distributed tracing (Zipkin)
   - Health checks /actuator
   - Log aggregation (ELK)
```

### 8. Cache
```
❌ Actual: Sin cache
✅ Futuro:
   - Spring Cache (Redis)
   - @Cacheable en getAllWines()
   - Cache invalidation en updates
```

### 9. API Versioning
```
❌ Actual: Sin versionado
✅ Futuro:
   - /api/v1/wines
   - Header-based o URL-based versioning
   - Deprecation strategy
```

### 10. Configuración Externa
```
❌ Actual: Hardcoded en beans
✅ Futuro:
   - application.yml externo
   - Spring Cloud Config
   - Secrets management (Vault)
```

---

## 📊 Resumen Técnico

| Aspecto | Estado Actual | Recomendación |
|---------|---------------|---------------|
| **Framework** | Spring Boot | ✅ Listo para producción |
| **Arquitectura** | Hexagonal | ✅ Buena separación concerns |
| **Persistencia** | In-Memory | ⚠️ Cambiar a BDD relacional |
| **Testing** | Unit tests | ⚠️ Expandir a integración |
| **Documentación** | Ninguna | ❌ Agregar OpenAPI |
| **Seguridad** | Ninguna | ❌ Implementar JWT |
| **API REST** | Basic CRUD | ✅ Cumple propósito formativo |

---

## 🎓 Recomendaciones de Estudio

1. **Iniciar por**: [`Wine.java`](src/main/java/bodegavininho/domain/model/Wine.java) → [`WineController.java`](src/main/java/bodegavininho/infrastructure/controller/WineController.java)
2. **Seguir con**: [`WineUseCase.java`](src/main/java/bodegavininho/application/usecase/WineUseCase.java) → [`WineRepository.java`](src/main/java/bodegavininho/domain/port/WineRepository.java)
3. **Entender preview vs import**: [`ImportExternalWinesUseCase.java`](src/main/java/bodegavininho/application/usecase/ImportExternalWinesUseCase.java) - Aplica patrón CQS
4. **Finalizar con**: [`OpenFoodFactsWineAdapter.java`](src/main/java/bodegavininho/infrastructure/adapter/OpenFoodFactsWineAdapter.java) para entender integración externa

---

*Documento generado automáticamente - BodegaVinosFormacion v1.0*
