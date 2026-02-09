package bodegavininho.application.dto;

import bodegavininho.domain.model.Wine;
import java.math.BigDecimal;

/**
 * DTO para transferencia de datos de Wine.
 */
public record WineDTO(
    String id,
    String name,
    int year,
    BigDecimal price,
    int stock,
    String country,
    String type,
    String typeDisplayName
) {
    public static WineDTO fromDomain(Wine wine) {
        return new WineDTO(
            wine.getId(),
            wine.getName(),
            wine.getYear(),
            wine.getPrice(),
            wine.getStock(),
            wine.getCountry(),
            wine.getType() != null ? wine.getType().name() : null,
            wine.getType() != null ? wine.getType().getDisplayName() : null
        );
    }
    
    public Wine toDomain() {
        Wine.WineType wineType = Wine.WineType.valueOf(type);
        return Wine.from(id, name, year, price.doubleValue(), stock, country, wineType);
    }
}
