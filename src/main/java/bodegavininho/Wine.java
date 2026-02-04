package bodegavininho;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Wine {
    
    private static final Logger logger = LoggerFactory.getLogger(Wine.class);
    
    private String name;
    private Integer year;
    private Float price;
    private Integer stock;
    private String country;
    private WineType type;
    
    public enum WineType {
        RED,
        WHITE,
        ROSE,
    }
    
    protected Wine() {
        this.name = "Vino sin nombre";
        this.price = 0.0f;
        this.stock = 0;
        this.year = 0;
        this.country = "Sin región";
        logger.info("Vino creado con valores por defecto: {}", this.name);
    }
    
    protected Wine(String name, int year, double price, int stock, WineType type) {
        this.name = name;
        this.year = year;
        this.price = (float) price;
        this.stock = stock;
        this.type = type;
        this.country = "Sin región";
        logger.info("Vino creado: {} - Año: {} - Precio: {} - Stock: {} - Tipo: {}", 
                    name, year, price, stock, type);
    }    
    
    // Getters
    public String getName() {
        return name;
    }
    
    public Integer getYear() {
        return year;
    }

    public float getPrice() {
        return price;
    }
    
    public int getStock() {
        return stock;
    }
    
    public String getCountry() {
        return country;
    }
    
    // Setters
    public void setName(String name) {
        this.name = name;
        logger.debug("Nombre del vino establecido a: {}", name);
    }

    public void setYear(Integer year) {
        this.year = year;
        logger.debug("Año del vino establecido a: {}", year);
    }
    
    public void setPrice(double price) {
        this.price = (float) price;
        logger.debug("Precio del vino establecido a: {}", price);
    }
    
    public void setStock(int stock) {
        this.stock = stock;
        logger.debug("Stock del vino establecido a: {}", stock);
    }
    
    public void setCountry(String country) {
        this.country = country;
        logger.debug("País/Región del vino establecido a: {}", country);
    }
    
    public void showInformation() {
        logger.info("=== VINO ===");
        logger.info("Nombre: {}", name);
        logger.info("Precio: {} €", price);
        logger.info("Stock: {} unidades", stock);
        logger.info("Región: {}", country);
    }
}
