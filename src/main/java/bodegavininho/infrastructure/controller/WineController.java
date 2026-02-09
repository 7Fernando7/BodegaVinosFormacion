package bodegavininho.infrastructure.controller;

import bodegavininho.application.dto.CreateWineRequest;
import bodegavininho.application.dto.WineDTO;
import bodegavininho.application.usecase.ImportExternalWinesUseCase;
import bodegavininho.application.usecase.WineUseCase;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de vinos.
 *
 * ARQUITECTURA: Este controller solo orquesta llamadas a los Use Cases.
 * No contiene lógica de negocio, solo mapeo de HTTP a domain/application.
 *
 * RUTAS: Se usan prefijos explícitos para evitar conflictos de routing.
 * El patrón "/{id}" genérico causa errores 500 porque Spring lo interpreta
 * antes que rutas más específicas como /search o /external.
 */
@RestController
@RequestMapping("/api/wines")
public class WineController {

    private static final Logger logger = LoggerFactory.getLogger(WineController.class);

    private final WineUseCase wineUseCase;
    private final ImportExternalWinesUseCase importExternalWinesUseCase;

    public WineController(
            WineUseCase wineUseCase,
            ImportExternalWinesUseCase importExternalWinesUseCase
    ) {
        this.wineUseCase = wineUseCase;
        this.importExternalWinesUseCase = importExternalWinesUseCase;
    }

    // ================================
    // ENDPOINTS DE LECTURA (READ)
    // ================================

    /**
     * Lista todos los vinos.
     * Ruta: GET /api/wines
     */
    @GetMapping
    public ResponseEntity<List<WineDTO>> getAllWines() {
        logger.info("GET /api/wines - Listando todos los vinos");
        return ResponseEntity.ok(wineUseCase.getAllWines());
    }

    /**
     * Busca un vino por su ID.
     * Ruta: GET /api/wines/by-id/{id}
     *
     * NOTA: Usamos "by-id" explícito para evitar conflictos.
     * Si usáramos "/{id}" genérico, Spring lo interpretaría antes
     * que rutas como "/search" o "/external", causando errores 500.
     */
    @GetMapping("/by-id/{id}")
    public ResponseEntity<WineDTO> getWineById(@PathVariable String id) {
        logger.info("GET /api/wines/by-id/{}", id);
        return wineUseCase.getWineById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Busca un vino por nombre (búsqueda exacta).
     * Ruta: GET /api/wines/by-name?name={nombre}
     *
     * @param name Nombre del vino a buscar
     */
    @GetMapping("/by-name")
    public ResponseEntity<WineDTO> getWineByName(@RequestParam String name) {
        logger.info("GET /api/wines/by-name?name={}", name);
        return wineUseCase.getWineByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ================================
    // ENDPOINTS DE ESCRITURA (WRITE)
    // ================================

    /**
     * Crea un nuevo vino.
     * Ruta: POST /api/wines
     *
     * @param request DTO con los datos del vino a crear
     * @return 201 Created con el vino creado
     */
    @PostMapping
    public ResponseEntity<WineDTO> createWine(
            @Valid @RequestBody CreateWineRequest request
    ) {
        logger.info("POST /api/wines - Creando vino: {}", request.name());
        WineDTO createdWine = wineUseCase.createWine(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdWine);
    }

    /**
     * Actualiza el stock de un vino.
     * Ruta: PUT /api/wines/{id}/stock?quantity={cantidad}
     *
     * @param id       ID del vino
     * @param quantity Nueva cantidad de stock
     * @return El vino con el stock actualizado
     */
    @PutMapping("/{id}/stock")
    public ResponseEntity<WineDTO> updateStock(
            @PathVariable String id,
            @RequestParam int quantity
    ) {
        logger.info("PUT /api/wines/{}/stock?quantity={}", id, quantity);
        WineDTO updatedWine = wineUseCase.updateStock(id, quantity);
        // Si el vino no existe, updateStock lanza excepción
        return ResponseEntity.ok(updatedWine);
    }

    /**
     * Elimina un vino por ID.
     * Ruta: DELETE /api/wines/{id}
     *
     * @param id ID del vino a eliminar
     * @return 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWine(@PathVariable String id) {
        logger.info("DELETE /api/wines/{}", id);
        wineUseCase.deleteWine(id);
        return ResponseEntity.noContent().build();
    }

    // ================================
    // ENDPOINTS EXTERNOS (EXTERNAL)
    // ================================

    /**
     * Previsualiza vinos desde fuente externa (solo lectura, no guarda).
     * Ruta: GET /api/wines/external
     *
     * Útil para ver qué vinos hay disponibles antes de importar.
     */
    @GetMapping("/external")
    public ResponseEntity<List<WineDTO>> previewExternalWines() {
        logger.info("GET /api/wines/external - Previsualizando vinos externos");
        return ResponseEntity.ok(importExternalWinesUseCase.previewExternalWines());
    }

    /**
     * Importa y guarda todos los vinos desde la fuente externa.
     * Ruta: POST /api/wines/import
     *
     * WARNING: Esta operación puede ser lenta y traer muchos datos.
     */
    @PostMapping("/import")
    public ResponseEntity<List<WineDTO>> importExternalWines() {
        logger.info("POST /api/wines/import - Importando todos los vinos externos");
        return ResponseEntity.ok(importExternalWinesUseCase.importAllWines());
    }
}
