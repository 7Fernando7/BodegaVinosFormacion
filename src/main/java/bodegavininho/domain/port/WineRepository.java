package bodegavininho.domain.port;

import bodegavininho.domain.model.Wine;
import java.util.List;
import java.util.Optional;

/**
 * Puerto - Interfaz de repositorio para Wine.
 */
public interface WineRepository {
    Wine save(Wine wine);
    Optional<Wine> findById(String id);
    Optional<Wine> findByName(String name);
    List<Wine> findAll();
    void deleteById(String id);
    boolean existsById(String id);
    Wine updateStock(String id, int newQuantity);
}
