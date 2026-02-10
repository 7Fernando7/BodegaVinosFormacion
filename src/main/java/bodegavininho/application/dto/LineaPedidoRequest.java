package bodegavininho.application.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

/**
 * DTO para solicitar una línea de pedido.
 */
public record LineaPedidoRequest(
        @NotBlank(message = "El ID del vino es obligatorio")
        String vinoId,
        
        @NotNull(message = "La cantidad es obligatoria")
        @Min(value = 1, message = "La cantidad debe ser al menos 1")
        Integer cantidad
) {
        public LineaPedidoRequest {
                if (cantidad != null && cantidad < 1) {
                        throw new IllegalArgumentException("La cantidad debe ser al menos 1");
                }
        }
}
