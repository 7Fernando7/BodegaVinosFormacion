package bodegavininho.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad de dominio StockItem - Representa un item en el inventario de la bodega.
 */
public class StockItem {
    
    private final String id;
    private final Wine wine;
    private final int quantity;
    private final int minQuantity;
    private final String location;
    private final LocalDateTime lastUpdated;
    
    public StockItem(Wine wine, int quantity, int minQuantity, String location) {
        this.id = java.util.UUID.randomUUID().toString();
        this.wine = wine;
        this.quantity = quantity;
        this.minQuantity = minQuantity;
        this.location = location;
        this.lastUpdated = LocalDateTime.now();
    }
    
    // Getters
    public String getId() { return id; }
    public Wine getWine() { return wine; }
    public int getQuantity() { return quantity; }
    public int getMinQuantity() { return minQuantity; }
    public String getLocation() { return location; }
    public LocalDateTime getLastUpdated() { return lastUpdated; }
    
    public boolean isLowStock() {
        return quantity <= minQuantity;
    }
    
    public BigDecimal getTotalValue() {
        return wine.getPrice().multiply(BigDecimal.valueOf(quantity));
    }
    
    public StockItem addQuantity(int additionalQuantity) {
        return new StockItem(wine, quantity + additionalQuantity, minQuantity, location);
    }
    
    public StockItem reduceQuantity(int reduceQuantity) {
        return new StockItem(wine, quantity - reduceQuantity, minQuantity, location);
    }
}
