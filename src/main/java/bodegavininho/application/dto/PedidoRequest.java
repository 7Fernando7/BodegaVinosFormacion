package bodegavininho.application.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

/**
 * DTO para crear un nuevo pedido.
 */
public record PedidoRequest(
        @NotBlank(message = "El ID del restaurante es obligatorio")
        String restauranteId,
        
        @NotEmpty(message = "El pedido debe contener al menos una línea")
        @Valid
        List<LineaPedidoRequest> lineas,
        
        String notas
) {
        public PedidoRequest {
                if (lineas != null && lineas.isEmpty()) {
                        throw new IllegalArgumentException("El pedido debe contener al menos una línea");
                }
        }
}
