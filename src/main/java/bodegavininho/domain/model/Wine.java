package bodegavininho.domain.model;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Entidad de dominio Wine - Representa un vino en la bodega.
 */
public class Wine {
    
    private final String id;
    private final String name;
    private final int year;
    private final BigDecimal price;
    private final int stock;
    private final String country;
    private final WineType type;
    
    public enum WineType {
        RED("Vino Tinto"),
        WHITE("Vino Blanco"),
        ROSE("Vino Rosado");
        
        private final String displayName;
        
        WineType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    private Wine(String id, String name, int year, BigDecimal price, int stock, String country, WineType type) {
        this.id = id != null ? id : UUID.randomUUID().toString();
        this.name = name;
        this.year = year;
        this.price = price;
        this.stock = stock;
        this.country = country;
        this.type = type;
    }
    
    public static Wine create(String name, int year, double price, int stock, String country, WineType type) {
        return new Wine(null, name, year, BigDecimal.valueOf(price), stock, country, type);
    }
    
    public static Wine from(String id, String name, int year, double price, int stock, String country, WineType type) {
        return new Wine(id, name, year, BigDecimal.valueOf(price), stock, country, type);
    }
    
    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public int getYear() { return year; }
    public BigDecimal getPrice() { return price; }
    public int getStock() { return stock; }
    public String getCountry() { return country; }
    public WineType getType() { return type; }
    
    public boolean hasStock(int quantity) {
        return this.stock >= quantity;
    }
    
    public Wine reduceStock(int quantity) {
        return new Wine(id, name, year, price, stock - quantity, country, type);
    }
    
    public Wine addStock(int quantity) {
        return new Wine(id, name, year, price, stock + quantity, country, type);
    }
}
