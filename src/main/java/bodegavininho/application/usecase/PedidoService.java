package bodegavininho.application.usecase;

import bodegavininho.application.dto.*;
import bodegavininho.domain.exception.*;
import bodegavininho.domain.model.*;
import bodegavininho.domain.port.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio de negocio para gestionar pedidos de restaurantes.
 * Maneja la lógica de creación, consulta y gestión de pedidos.
 */
public class PedidoService {
        
        private static final Logger logger = LoggerFactory.getLogger(PedidoService.class);
        
        private final PedidoRepository pedidoRepository;
        private final RestauranteRepository restauranteRepository;
        private final WineRepository wineRepository;
        
        public PedidoService(
                PedidoRepository pedidoRepository,
                RestauranteRepository restauranteRepository,
                WineRepository wineRepository
        ) {
                this.pedidoRepository = pedidoRepository;
                this.restauranteRepository = restauranteRepository;
                this.wineRepository = wineRepository;
        }
        
        /**
         * Crea un nuevo pedido para un restaurante.
         * Valida que el restaurante exista y descuenta el stock.
         */
        public PedidoResponse createPedido(PedidoRequest request) {
                logger.info("Recibida solicitud de pedido para restaurante: {}", request.restauranteId());
                
                // 1. Validar que el restaurante existe
                logger.debug("Validando restaurante: {}", request.restauranteId());
                Restaurante restaurante = restauranteRepository.findById(request.restauranteId())
                        .orElseThrow(() -> {
                                logger.warn("Restaurante no encontrado: {}", request.restauranteId());
                                return new RestauranteNotFoundException(
                                        "No se encontró el restaurante con ID: " + request.restauranteId()
                                );
                        });
                
                logger.info("Restaurante validado: {} ({})", restaurante.getNombre(), restaurante.getId());
                
                // 2. Procesar líneas de pedido y verificar stock
                logger.debug("Procesando {} líneas de pedido", request.lineas().size());
                List<LineaPedido> lineasPedido = request.lineas().stream()
                        .map(lineaRequest -> processLineaPedido(lineaRequest))
                        .collect(Collectors.toList());
                
                // 3. Crear el pedido
                logger.info("Creando pedido para restaurante: {}", restaurante.getNombre());
                Pedido pedido = Pedido.create(
                        restaurante.getId(),
                        restaurante.getNombre(),
                        lineasPedido,
                        request.notas()
                );
                
                // 4. Descontar stock de cada vino
                logger.debug("Descontando stock de {} vinos", lineasPedido.size());
                descontarStock(lineasPedido);
                
                // 5. Guardar el pedido
                Pedido savedPedido = pedidoRepository.save(pedido);
                logger.info("Pedido {} creado exitosamente. Total: {} €", 
                        savedPedido.getId(), savedPedido.getTotal());
                
                return PedidoResponse.fromDomain(savedPedido);
        }
        
        /**
         * Procesa una línea de pedido y verifica disponibilidad.
         */
        private LineaPedido processLineaPedido(LineaPedidoRequest request) {
                logger.debug("Procesando línea: vinoId={}, cantidad={}", 
                        request.vinoId(), request.cantidad());
                
                // Buscar el vino
                Wine vino = wineRepository.findById(request.vinoId())
                        .orElseThrow(() -> {
                                logger.warn("Vino no encontrado: {}", request.vinoId());
                                return new VinoNotFoundException(
                                        "No se encontró el vino con ID: " + request.vinoId()
                                );
                        });
                
                // Verificar stock
                if (vino.getStock() < request.cantidad()) {
                        logger.warn("Stock insuficiente para vino {}: disponible={}, solicitado={}",
                                vino.getName(), vino.getStock(), request.cantidad());
                        throw new StockInsuficienteException(
                                String.format("Stock insuficiente para '%s': disponible=%d, solicitado=%d",
                                        vino.getName(), vino.getStock(), request.cantidad())
                        );
                }
                
                logger.debug("Vino {} disponible. Precio: {} €", vino.getName(), vino.getPrice());
                
                return LineaPedido.create(
                        vino.getId(),
                        vino.getName(),
                        request.cantidad(),
                        vino.getPrice()
                );
        }
        
        /**
         * Descuenta el stock de cada vino en las líneas de pedido.
         */
        private void descontarStock(List<LineaPedido> lineas) {
                for (LineaPedido linea : lineas) {
                        logger.debug("Descontando {} unidades del vino {}", 
                                linea.getCantidad(), linea.getVinoId());
                        wineRepository.updateStock(linea.getVinoId(), linea.getCantidad());
                        logger.info("Stock actualizado: vino={}, cantidad={}", 
                                linea.getVinoNombre(), linea.getCantidad());
                }
        }
        
        /**
         * Obtiene un pedido por su ID.
         */
        public PedidoResponse getPedidoById(String id) {
                logger.info("Consultando pedido: {}", id);
                Pedido pedido = pedidoRepository.findById(id)
                        .orElseThrow(() -> {
                                logger.warn("Pedido no encontrado: {}", id);
                                return new RuntimeException("No se encontró el pedido con ID: " + id);
                        });
                return PedidoResponse.fromDomain(pedido);
        }
        
        /**
         * Obtiene todos los pedidos.
         */
        public List<PedidoResponse> getAllPedidos() {
                logger.debug("Consultando todos los pedidos");
                return pedidoRepository.findAll().stream()
                        .map(PedidoResponse::fromDomain)
                        .collect(Collectors.toList());
        }
        
        /**
         * Obtiene todos los pedidos de un restaurante.
         */
        public List<PedidoResponse> getPedidosByRestaurante(String restauranteId) {
                logger.info("Consultando pedidos del restaurante: {}", restauranteId);
                return pedidoRepository.findByRestauranteId(restauranteId).stream()
                        .map(PedidoResponse::fromDomain)
                        .collect(Collectors.toList());
        }
        
        /**
         * Cancela un pedido.
         */
        public PedidoResponse cancelarPedido(String pedidoId) {
                logger.info("Cancelando pedido: {}", pedidoId);
                Pedido pedido = pedidoRepository.findById(pedidoId)
                        .orElseThrow(() -> {
                                logger.warn("Pedido no encontrado para cancelación: {}", pedidoId);
                                return new RuntimeException("No se encontró el pedido con ID: " + pedidoId);
                        });
                
                if (pedido.getEstado() == Pedido.EstadoPedido.ENTREGADO) {
                        logger.error("No se puede cancelar un pedido ya entregado: {}", pedidoId);
                        throw new RuntimeException("No se puede cancelar un pedido ya entregado");
                }
                
                pedido.cancelar();
                Pedido savedPedido = pedidoRepository.save(pedido);
                logger.info("Pedido {} cancelado exitosamente", pedidoId);
                
                return PedidoResponse.fromDomain(savedPedido);
        }
}
