package bodegavininho.infrastructure.repository;

import bodegavininho.domain.model.Pedido;
import bodegavininho.domain.port.PedidoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Implementación en memoria del repositorio de pedidos.
 */
public class InMemoryPedidoRepository implements PedidoRepository {
        
        private static final Logger logger = LoggerFactory.getLogger(InMemoryPedidoRepository.class);
        
        private final Map<String, Pedido> pedidoStore;
        
        public InMemoryPedidoRepository() {
                this.pedidoStore = new HashMap<>();
                logger.info("Repositorio de pedidos en memoria inicializado");
        }
        
        @Override
        public Pedido save(Pedido pedido) {
                logger.debug("Guardando pedido: {}", pedido.getId());
                pedidoStore.put(pedido.getId(), pedido);
                return pedido;
        }
        
        @Override
        public Optional<Pedido> findById(String id) {
                logger.debug("Buscando pedido por ID: {}", id);
                return Optional.ofNullable(pedidoStore.get(id));
        }
        
        @Override
        public List<Pedido> findAll() {
                logger.debug("Obteniendo todos los pedidos");
                return new ArrayList<>(pedidoStore.values());
        }
        
        @Override
        public List<Pedido> findByRestauranteId(String restauranteId) {
                logger.debug("Buscando pedidos por restaurante: {}", restauranteId);
                return pedidoStore.values().stream()
                        .filter(pedido -> pedido.getRestauranteId().equals(restauranteId))
                        .toList();
        }
        
        @Override
        public void deleteById(String id) {
                logger.debug("Eliminando pedido: {}", id);
                pedidoStore.remove(id);
        }
        
        @Override
        public boolean existsById(String id) {
                return pedidoStore.containsKey(id);
        }
}
