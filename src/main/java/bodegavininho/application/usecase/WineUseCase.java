package bodegavininho.application.usecase;

import bodegavininho.application.dto.CreateWineRequest;
import bodegavininho.application.dto.WineDTO;
import bodegavininho.domain.model.Wine;
import bodegavininho.domain.port.WineRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Caso de uso para gestionar vinos.
 * Maneja la lógica de negocio relacionada con vinos.
 */
public class WineUseCase {
    
    private static final Logger logger = LoggerFactory.getLogger(WineUseCase.class);
    
    private final WineRepository wineRepository;
    
    public WineUseCase(WineRepository wineRepository) {
        this.wineRepository = wineRepository;
    }
    
    /**
     * Crea un nuevo vino.
     * Validaciones:
     * - Verifica que no exista otro vino con el mismo nombre
     * - El tipo ya viene validado como enum en el DTO
     */
    public WineDTO createWine(CreateWineRequest request) {
        logger.info("Creando vino: {}", request.name());
        
        // 1. Verificar duplicado por nombre (uso explícito de Optional)
        Optional<Wine> existingWine = wineRepository.findByName(request.name());
        if (existingWine.isPresent()) {
            throw new IllegalArgumentException(
                "Ya existe un vino con el nombre: '" + request.name() + "'"
            );
        }
        
        // 2. Crear el vino (el tipo ya viene como enum, no necesita conversión)
        Wine wine = Wine.create(
            request.name(),
            request.year(),
            request.price(),
            request.stock(),
            request.country(),
            request.type()  // Ya es Wine.WineType, sin conversión
        );
        
        Wine savedWine = wineRepository.save(wine);
        logger.info("Vino creado con ID: {}", savedWine.getId());
        
        return WineDTO.fromDomain(savedWine);
    }
    
    /**
     * Busca un vino por ID.
     * @return Optional vacío si no existe
     */
    public Optional<WineDTO> getWineById(String id) {
        logger.debug("Buscando vino por ID: {}", id);
        return wineRepository.findById(id)
            .map(WineDTO::fromDomain);
    }
    
    /**
     * Busca un vino por nombre.
     * @return Optional vacío si no existe
     */
    public Optional<WineDTO> getWineByName(String name) {
        logger.debug("Buscando vino por nombre: {}", name);
        return wineRepository.findByName(name)
            .map(WineDTO::fromDomain);
    }
    
    /**
     * Lista todos los vinos.
     */
    public List<WineDTO> getAllWines() {
        logger.debug("Obteniendo todos los vinos");
        return wineRepository.findAll().stream()
            .map(WineDTO::fromDomain)
            .collect(Collectors.toList());
    }
    
    /**
     * Actualiza el stock de un vino.
     * Validación: no permite valores negativos.
     */
    public WineDTO updateStock(String id, int newQuantity) {
        // Validación simple: stock no puede ser negativo
        if (newQuantity < 0) {
            throw new IllegalArgumentException(
                "El stock no puede ser negativo: " + newQuantity
            );
        }
        
        logger.info("Actualizando stock del vino {} a: {}", id, newQuantity);
        Wine updatedWine = wineRepository.updateStock(id, newQuantity);
        return WineDTO.fromDomain(updatedWine);
    }
    
    /**
     * Elimina un vino por ID.
     */
    public void deleteWine(String id) {
        logger.info("Eliminando vino: {}", id);
        wineRepository.deleteById(id);
    }
}
