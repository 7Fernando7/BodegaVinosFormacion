package bodegavininho.application.dto;

import bodegavininho.domain.model.LineaPedido;

import java.math.BigDecimal;

/**
 * DTO para representar una línea de pedido en la respuesta.
 */
public record LineaPedidoResponse(
        String id,
        String vinoId,
        String vinoNombre,
        int cantidad,
        BigDecimal precioUnitario,
        BigDecimal subtotal
) {
        public static LineaPedidoResponse fromDomain(LineaPedido linea) {
                return new LineaPedidoResponse(
                        linea.getId(),
                        linea.getVinoId(),
                        linea.getVinoNombre(),
                        linea.getCantidad(),
                        linea.getPrecioUnitario(),
                        linea.getSubtotal()
                );
        }
}
