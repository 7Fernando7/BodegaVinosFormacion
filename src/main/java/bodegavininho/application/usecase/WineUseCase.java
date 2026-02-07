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
 */
public class WineUseCase {
    
    private static final Logger logger = LoggerFactory.getLogger(WineUseCase.class);
    
    private final WineRepository wineRepository;
    
    public WineUseCase(WineRepository wineRepository) {
        this.wineRepository = wineRepository;
    }
    
    public WineDTO createWine(CreateWineRequest request) {
        logger.info("Creando vino: {}", request.name());
        
        Wine.WineType type = Wine.WineType.valueOf(request.type().toUpperCase());
        Wine wine = Wine.create(
            request.name(),
            request.year(),
            request.price(),
            request.stock(),
            request.country(),
            type
        );
        
        Wine savedWine = wineRepository.save(wine);
        logger.info("Vino creado con ID: {}", savedWine.getId());
        
        return WineDTO.fromDomain(savedWine);
    }
    
    public Optional<WineDTO> getWineById(String id) {
        logger.debug("Buscando vino por ID: {}", id);
        return wineRepository.findById(id)
            .map(WineDTO::fromDomain);
    }
    
    public Optional<WineDTO> getWineByName(String name) {
        logger.debug("Buscando vino por nombre: {}", name);
        return wineRepository.findByName(name)
            .map(WineDTO::fromDomain);
    }
    
    public List<WineDTO> getAllWines() {
        logger.debug("Obteniendo todos los vinos");
        return wineRepository.findAll().stream()
            .map(WineDTO::fromDomain)
            .collect(Collectors.toList());
    }
    
    public WineDTO updateStock(String id, int newQuantity) {
        logger.info("Actualizando stock del vino {} a: {}", id, newQuantity);
        Wine updatedWine = wineRepository.updateStock(id, newQuantity);
        return WineDTO.fromDomain(updatedWine);
    }
    
    public void deleteWine(String id) {
        logger.info("Eliminando vino: {}", id);
        wineRepository.deleteById(id);
    }
}
