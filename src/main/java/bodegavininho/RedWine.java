package bodegavininho;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedWine extends Wine {

    private static final Logger logger = LoggerFactory.getLogger(RedWine.class);

    private String grapeType;
    private boolean hasBarrel;
    
    public RedWine() {
        super();
        this.grapeType = "Tempranillo";
        this.hasBarrel = true;
        logger.info("Vino Tinto creado con valores por defecto");
    }
    
    public RedWine(String name, int year, double price, int stock, String grapeType, boolean hasBarrel) {
        super(name, year, price, stock, Wine.WineType.RED);
        this.grapeType = grapeType;
        this.hasBarrel = hasBarrel;
        logger.info("Vino Tinto creado: {} - Uva: {} - Tiene barrica: {}", name, grapeType, hasBarrel);
    }
    
    // Getters
    public String getGrapeType() {
        return grapeType;
    }
    
    public boolean isHasBarrel() {
        return hasBarrel;
    }
    
    // Setters
    public void setGrapeType(String typeOfWine) {
        this.grapeType = typeOfWine;
        logger.debug("Tipo de uva del vino tinto establecido a: {}", typeOfWine);
    }
    
    public void setHasBarrel(boolean hasBarrel) {
        this.hasBarrel = hasBarrel;
        logger.debug("Envejecimiento en barrica establecido a: {}", hasBarrel);
    }
    
    @Override
    public void showInformation() {
        logger.info("=== VINO TINTO ===");
        logger.info("Nombre: {}", getName());
        logger.info("Precio: {} €", getPrice());
        logger.info("Stock: {} unidades", getStock());
        logger.info("Año: {}", getYear());
        logger.info("Tipo de uva: {}", grapeType);
        logger.info("Tiene barrica: {}", hasBarrel ? "Sí" : "No");
    }

    public void showAgingInfo() {
        if (hasBarrel) {
            logger.info("Este vino tinto ha sido envejecido en barrica");
        } else {
            logger.info("Este vino tinto no ha sido envejecido en barrica");
        }
    }

}
