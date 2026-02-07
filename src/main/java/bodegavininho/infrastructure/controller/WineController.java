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
    
    @PostMapping("/import")
    public ResponseEntity<List<WineDTO>> importExternalWines() {
        logger.info("POST /api/wines/import");
        List<WineDTO> importedWines = importExternalWinesUseCase.importAllWines();
        return ResponseEntity.ok(importedWines);
    }
    
    @PostMapping("/import/barcode/{barcode}")
    public ResponseEntity<WineDTO> importWineByBarcode(@PathVariable String barcode) {
        logger.info("POST /api/wines/import/barcode/{}", barcode);
        WineDTO importedWine = importExternalWinesUseCase.importByBarcode(barcode);
        
        if (importedWine != null) {
            return ResponseEntity.ok(importedWine);
        }
        return ResponseEntity.notFound().build();
    }
}
