package bodegavininho.domain.port;

import bodegavininho.domain.model.Restaurante;

import java.util.Optional;

/**
 * Puerto para el repositorio de restaurantes.
 */
public interface RestauranteRepository {
        
        /**
         * Guarda un restaurante.
         */
        Restaurante save(Restaurante restaurante);
        
        /**
         * Busca un restaurante por su ID.
         */
        Optional<Restaurante> findById(String id);
        
        /**
         * Verifica si existe un restaurante por su ID.
         */
        boolean existsById(String id);
}
