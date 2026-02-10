package bodegavininho.application.dto;

import bodegavininho.domain.model.LineaPedido;
import bodegavininho.domain.model.Pedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO para representar un pedido en la respuesta.
 */
public record PedidoResponse(
        String id,
        String restauranteId,
        String restauranteNombre,
        LocalDateTime fechaPedido,
        LocalDateTime fechaActualizacion,
        String estado,
        BigDecimal total,
        List<LineaPedidoResponse> lineas,
        String notas
) {
        public static PedidoResponse fromDomain(Pedido pedido) {
                List<LineaPedidoResponse> lineasResponse = pedido.getLineas().stream()
                        .map(LineaPedidoResponse::fromDomain)
                        .collect(Collectors.toList());
                
                return new PedidoResponse(
                        pedido.getId(),
                        pedido.getRestauranteId(),
                        pedido.getRestauranteNombre(),
                        pedido.getFechaPedido(),
                        pedido.getFechaActualizacion(),
                        pedido.getEstado().name(),
                        pedido.getTotal(),
                        lineasResponse,
                        pedido.getNotas()
                );
        }
}
