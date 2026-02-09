package bodegavininho.infrastructure.controller;

import bodegavininho.application.dto.CreateWineRequest;
import bodegavininho.application.dto.WineDTO;
import bodegavininho.application.usecase.ImportExternalWinesUseCase;
import bodegavininho.application.usecase.WineUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de vinos.
 * Endpoints:
 * - /api/wines      : CRUD de vinos internos
 * - /api/wines/external : Vista previa de vinos externos (sin persistir)
 * - /api/wines/import   : Importación de vinos externos (con persistencia)
 */
@RestController
@RequestMapping("/api/wines")
public class WineController {
    
    private static final Logger logger = LoggerFactory.getLogger(WineController.class);
    
    private final WineUseCase wineUseCase;
    private final ImportExternalWinesUseCase importExternalWinesUseCase;
    
    public WineController(WineUseCase wineUseCase, ImportExternalWinesUseCase importExternalWinesUseCase) {
        this.wineUseCase = wineUseCase;
        this.importExternalWinesUseCase = importExternalWinesUseCase;
    }
    
    // ==================== VINOS INTERNOS (CRUD) ====================
    
    @GetMapping
    public ResponseEntity<List<WineDTO>> getAllWines() {
        logger.info("GET /api/wines");
        List<WineDTO> wines = wineUseCase.getAllWines();
        return ResponseEntity.ok(wines);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<WineDTO> getWineById(@PathVariable String id) {
        logger.info("GET /api/wines/{}", id);
        return wineUseCase.getWineById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/search")
    public ResponseEntity<WineDTO> getWineByName(@RequestParam String name) {
        logger.info("GET /api/wines/search?name={}", name);
        return wineUseCase.getWineByName(name)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<WineDTO> createWine(@RequestBody CreateWineRequest request) {
        logger.info("POST /api/wines - Creando vino: {}", request.name());
        WineDTO createdWine = wineUseCase.createWine(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdWine);
    }
    
    @PutMapping("/{id}/stock")
    public ResponseEntity<WineDTO> updateStock(
            @PathVariable String id, 
            @RequestParam int quantity) {
        logger.info("PUT /api/wines/{}/stock - Actualizando a: {}", id, quantity);
        try {
            WineDTO updatedWine = wineUseCase.updateStock(id, quantity);
            return ResponseEntity.ok(updatedWine);
        } catch (Exception e) {
            logger.error("Error al actualizar stock", e);
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWine(@PathVariable String id) {
        logger.info("DELETE /api/wines/{}", id);
        wineUseCase.deleteWine(id);
        return ResponseEntity.noContent().build();
    }
    
    // ==================== VINOS EXTERNOS (PREVIEW - Solo Lectura) ====================
    
    /**
     * Previsualiza vinos desde la fuente externa sin modificar estado del sistema.
     * GET /api/wines/external
     */
    @GetMapping("/external")
    public ResponseEntity<List<WineDTO>> previewExternalWines() {
        logger.info("GET /api/wines/external - Previsualizando vinos externos");
        List<WineDTO> externalWines = importExternalWinesUseCase.previewExternalWines();
        return ResponseEntity.ok(externalWines);
    }
    
    /**
     * Previsualiza un vino por código de barras sin modificar estado del sistema.
     * GET /api/wines/external/barcode/{barcode}
     */
    @GetMapping("/external/barcode/{barcode}")
    public ResponseEntity<WineDTO> previewWineByBarcode(@PathVariable String barcode) {
        logger.info("GET /api/wines/external/barcode/{} - Previsualizando vino", barcode);
        WineDTO wine = importExternalWinesUseCase.previewByBarcode(barcode);
        
        if (wine != null) {
            return ResponseEntity.ok(wine);
        }
        return ResponseEntity.notFound().build();
    }
    
    // ==================== IMPORTACIÓN (Persistencia) ====================
    
    /**
     * Importa y guarda todos los vinos desde la fuente externa.
     * POST /api/wines/import
     */
    @PostMapping("/import")
    public ResponseEntity<List<WineDTO>> importExternalWines() {
        logger.info("POST /api/wines/import - Importando vinos externos");
        List<WineDTO> importedWines = importExternalWinesUseCase.importAllWines();
        return ResponseEntity.ok(importedWines);
    }
    
    /**
     * Importa y guarda un vino por código de barras.
     * POST /api/wines/import/barcode/{barcode}
     */
    @PostMapping("/import/barcode/{barcode}")
    public ResponseEntity<WineDTO> importWineByBarcode(@PathVariable String barcode) {
        logger.info("POST /api/wines/import/barcode/{} - Importando vino", barcode);
        WineDTO importedWine = importExternalWinesUseCase.importByBarcode(barcode);
        
        if (importedWine != null) {
            return ResponseEntity.ok(importedWine);
        }
        return ResponseEntity.notFound().build();
    }
}
