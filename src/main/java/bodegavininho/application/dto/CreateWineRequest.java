package bodegavininho.application.dto;

import bodegavininho.domain.model.Wine;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * DTO para crear un nuevo vino.
 * Usa Bean Validation para validar los datos de entrada.
 */
public record CreateWineRequest(
    @NotBlank(message = "El nombre no puede estar vacío")
    String name,
    
    @Min(value = 1900, message = "El año debe ser >= 1900")
    Integer year,
    
    @Positive(message = "El precio debe ser positivo")
    Double price,
    
    @Min(value = 0, message = "El stock no puede ser negativo")
    Integer stock,
    
    String country,
    
    @NotNull(message = "El tipo es obligatorio")
    Wine.WineType type
) {}
