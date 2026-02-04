package bodegavininho;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WhiteWine extends Wine {

    private static final Logger logger = LoggerFactory.getLogger(WhiteWine.class);

    private String grapeType;
    private boolean isSparkling;
    private String region;
    
    public WhiteWine() {
        super();
        this.grapeType = "Chardonnay";
        this.isSparkling = false;
        this.region = "Borgoña";
        logger.info("Vino Blanco creado con valores por defecto");
    }
    
    public WhiteWine(String name, int year, double price, int stock, String grapeType, boolean isSparkling, String region) {
        super(name, year, price, stock, Wine.WineType.WHITE);
        this.grapeType = grapeType;
        this.isSparkling = isSparkling;
        this.region = region;
        logger.info("Vino Blanco creado: {} - Uva: {} - Espumoso: {} - Región: {}", 
                   name, grapeType, isSparkling, region);
    }
    
    // Getters
    public String getGrapeType() {
        return grapeType;
    }
    
    public boolean isSparkling() {
        return isSparkling;
    }
    
    public String getRegion() {
        return region;
    }
    
    // Setters
    public void setGrapeType(String grapeType) {
        this.grapeType = grapeType;
        logger.debug("Tipo de uva del vino blanco establecido a: {}", grapeType);
    }
    
    public void setSparkling(boolean isSparkling) {
        this.isSparkling = isSparkling;
        logger.debug("Vino espumoso establecido a: {}", isSparkling);
    }
    
    public void setRegion(String region) {
        this.region = region;
        logger.debug("Región del vino blanco establecida a: {}", region);
    }
    
    @Override
    public void showInformation() {
        logger.info("=== VINO BLANCO ===");
        logger.info("Nombre: {}", getName());
        logger.info("Precio: {} €", getPrice());
        logger.info("Stock: {} unidades", getStock());
        logger.info("Año: {}", getYear());
        logger.info("Tipo de uva: {}", grapeType);
        logger.info("Región: {}", region);
        logger.info("Es espumoso: {}", isSparkling ? "Sí" : "No");
    }

    public void showServingTip() {
        if (isSparkling) {
            logger.info("Este vino blanco espumoso se sirve muy frío");
        } else {
            logger.info("Este vino blanco se sirve frío (8-10°C)");
        }
    }

}
