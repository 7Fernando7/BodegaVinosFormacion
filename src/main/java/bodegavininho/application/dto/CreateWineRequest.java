package bodegavininho.application.dto;

/**
 * DTO para crear un nuevo vino.
 */
public record CreateWineRequest(
    String name,
    Integer year,
    Double price,
    Integer stock,
    String country,
    String type
) {}
