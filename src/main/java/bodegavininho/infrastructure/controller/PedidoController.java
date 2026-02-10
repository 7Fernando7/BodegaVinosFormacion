package bodegavininho.infrastructure.controller;

import bodegavininho.application.dto.*;
import bodegavininho.application.usecase.PedidoService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de pedidos B2B.
 * Los pedidos son realizados por restaurantes.
 */
@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {
        
        private static final Logger logger = LoggerFactory.getLogger(PedidoController.class);
        
        private final PedidoService pedidoService;
        
        public PedidoController(PedidoService pedidoService) {
                this.pedidoService = pedidoService;
        }
        
        // ================================
        // ENDPOINTS DE LECTURA (READ)
        // ================================
        
        /**
         * Lista todos los pedidos.
         * Ruta: GET /api/pedidos
         */
        @GetMapping
        public ResponseEntity<List<PedidoResponse>> getAllPedidos() {
                logger.info("GET /api/pedidos - Listando todos los pedidos");
                List<PedidoResponse> pedidos = pedidoService.getAllPedidos();
                logger.info("Se encontraron {} pedidos", pedidos.size());
                return ResponseEntity.ok(pedidos);
        }
        
        /**
         * Busca un pedido por su ID.
         * Ruta: GET /api/pedidos/{id}
         */
        @GetMapping("/{id}")
        public ResponseEntity<PedidoResponse> getPedidoById(@PathVariable String id) {
                logger.info("GET /api/pedidos/{}", id);
                return ResponseEntity.ok(pedidoService.getPedidoById(id));
        }
        
        /**
         * Lista los pedidos de un restaurante específico.
         * Ruta: GET /api/pedidos/restaurante/{restauranteId}
         */
        @GetMapping("/restaurante/{restauranteId}")
        public ResponseEntity<List<PedidoResponse>> getPedidosByRestaurante(
                @PathVariable String restauranteId
        ) {
                logger.info("GET /api/pedidos/restaurante/{}", restauranteId);
                List<PedidoResponse> pedidos = pedidoService.getPedidosByRestaurante(restauranteId);
                logger.info("Se encontraron {} pedidos para restaurante {}", 
                        pedidos.size(), restauranteId);
                return ResponseEntity.ok(pedidos);
        }
        
        // ================================
        // ENDPOINTS DE ESCRITURA (WRITE)
        // ================================
        
        /**
         * Crea un nuevo pedido.
         * Ruta: POST /api/pedidos
         *
         * @param request DTO con los datos del pedido
         * @return 201 Created con el pedido creado
         */
        @PostMapping
        public ResponseEntity<PedidoResponse> createPedido(
                @Valid @RequestBody PedidoRequest request
        ) {
                logger.info("POST /api/pedidos - Creando pedido para restaurante: {}", 
                        request.restauranteId());
                logger.debug("Request: {} líneas de pedido", request.lineas().size());
                
                PedidoResponse createdPedido = pedidoService.createPedido(request);
                
                logger.info("Pedido {} creado exitosamente. Total: {} €", 
                        createdPedido.id(), createdPedido.total());
                
                return ResponseEntity.status(HttpStatus.CREATED).body(createdPedido);
        }
        
        /**
         * Cancela un pedido.
         * Ruta: DELETE /api/pedidos/{id}
         *
         * @param id ID del pedido a cancelar
         * @return 200 OK con el pedido cancelado
         */
        @DeleteMapping("/{id}")
        public ResponseEntity<PedidoResponse> cancelarPedido(@PathVariable String id) {
                logger.info("DELETE /api/pedidos/{} - Cancelando pedido", id);
                PedidoResponse canceledPedido = pedidoService.cancelarPedido(id);
                logger.info("Pedido {} cancelado. Estado: {}", id, canceledPedido.estado());
                return ResponseEntity.ok(canceledPedido);
        }
}
