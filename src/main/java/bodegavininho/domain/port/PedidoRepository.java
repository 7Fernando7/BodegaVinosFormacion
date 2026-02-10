package bodegavininho.domain.port;

import bodegavininho.domain.model.Pedido;

import java.util.List;
import java.util.Optional;

/**
 * Puerto para el repositorio de pedidos.
 */
public interface PedidoRepository {
        
        /**
         * Guarda un pedido.
         */
        Pedido save(Pedido pedido);
        
        /**
         * Busca un pedido por su ID.
         */
        Optional<Pedido> findById(String id);
        
        /**
         * Busca todos los pedidos.
         */
        List<Pedido> findAll();
        
        /**
         * Busca todos los pedidos de un restaurante.
         */
        List<Pedido> findByRestauranteId(String restauranteId);
        
        /**
         * Elimina un pedido por su ID.
         */
        void deleteById(String id);
        
        /**
         * Verifica si existe un pedido por su ID.
         */
        boolean existsById(String id);
}
