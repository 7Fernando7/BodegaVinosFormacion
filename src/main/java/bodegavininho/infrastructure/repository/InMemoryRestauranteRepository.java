package bodegavininho.infrastructure.repository;

import bodegavininho.domain.model.Restaurante;
import bodegavininho.domain.port.RestauranteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Implementación en memoria del repositorio de restaurantes.
 */
public class InMemoryRestauranteRepository implements RestauranteRepository {
        
        private static final Logger logger = LoggerFactory.getLogger(InMemoryRestauranteRepository.class);
        
        private final Map<String, Restaurante> restauranteStore;
        
        public InMemoryRestauranteRepository() {
                this.restauranteStore = new HashMap<>();
                logger.info("Repositorio de restaurantes en memoria inicializado");
        }
        
        @Override
        public Restaurante save(Restaurante restaurante) {
                logger.debug("Guardando restaurante: {}", restaurante.getId());
                restauranteStore.put(restaurante.getId(), restaurante);
                return restaurante;
        }
        
        @Override
        public Optional<Restaurante> findById(String id) {
                logger.debug("Buscando restaurante por ID: {}", id);
                return Optional.ofNullable(restauranteStore.get(id));
        }
        
        @Override
        public boolean existsById(String id) {
                return restauranteStore.containsKey(id);
        }
}
