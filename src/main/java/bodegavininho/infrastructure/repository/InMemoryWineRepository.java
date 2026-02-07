package bodegavininho.infrastructure.repository;

import bodegavininho.domain.model.Wine;
import bodegavininho.domain.port.WineRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Implementación en memoria del repositorio de vinos.
 */
public class InMemoryWineRepository implements WineRepository {
    
    private static final Logger logger = LoggerFactory.getLogger(InMemoryWineRepository.class);
    
    private final Map<String, Wine> wineStore;
    
    public InMemoryWineRepository() {
        this.wineStore = new HashMap<>();
        logger.info("Repositorio de vinos en memoria inicializado");
    }
    
    @Override
    public Wine save(Wine wine) {
        logger.debug("Guardando vino: {}", wine.getId());
        wineStore.put(wine.getId(), wine);
        return wine;
    }
    
    @Override
    public Optional<Wine> findById(String id) {
        logger.debug("Buscando vino por ID: {}", id);
        return Optional.ofNullable(wineStore.get(id));
    }
    
    @Override
    public Optional<Wine> findByName(String name) {
        logger.debug("Buscando vino por nombre: {}", name);
        return wineStore.values().stream()
            .filter(wine -> wine.getName().equalsIgnoreCase(name))
            .findFirst();
    }
    
    @Override
    public List<Wine> findAll() {
        logger.debug("Obteniendo todos los vinos");
        return new ArrayList<>(wineStore.values());
    }
    
    @Override
    public void deleteById(String id) {
        logger.debug("Eliminando vino: {}", id);
        wineStore.remove(id);
    }
    
    @Override
    public boolean existsById(String id) {
        return wineStore.containsKey(id);
    }
    
    @Override
    public Wine updateStock(String id, int newQuantity) {
        logger.debug("Actualizando stock del vino {} a: {}", id, newQuantity);
        Wine wine = wineStore.get(id);
        if (wine != null) {
            Wine updatedWine = wine.addStock(newQuantity - wine.getStock());
            wineStore.put(id, updatedWine);
            return updatedWine;
        }
        throw new IllegalArgumentException("Vino no encontrado: " + id);
    }
}
