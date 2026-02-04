package bodegavininho;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RoseWine extends Wine {

    private static final Logger logger = LoggerFactory.getLogger(RoseWine.class);

    private String grapeType;
    private String sweetness;
    private int agingMonths;
    
    public RoseWine() {
        super();
        this.grapeType = "Grenache";
        this.sweetness = "Seco";
        this.agingMonths = 0;
        logger.info("Vino Rosado creado con valores por defecto");
    }
    
    public RoseWine(String name, int year, double price, int stock, String grapeType, String sweetness, int agingMonths) {
        super(name, year, price, stock, Wine.WineType.ROSE);
        this.grapeType = grapeType;
        this.sweetness = sweetness;
        this.agingMonths = agingMonths;
        logger.info("Vino Rosado creado: {} - Uva: {} - Dulzura: {} - Envejecimiento: {} meses", 
                   name, grapeType, sweetness, agingMonths);
    }
    
    // Getters
    public String getGrapeType() {
        return grapeType;
    }
    
    public String getSweetness() {
        return sweetness;
    }
    
    public int getAgingMonths() {
        return agingMonths;
    }
    
    // Setters
    public void setGrapeType(String grapeType) {
        this.grapeType = grapeType;
        logger.debug("Tipo de uva del vino rosado establecido a: {}", grapeType);
    }
    
    public void setSweetness(String sweetness) {
        this.sweetness = sweetness;
        logger.debug("Dulzura del vino rosado establecida a: {}", sweetness);
    }
    
    public void setAgingMonths(int agingMonths) {
        this.agingMonths = agingMonths;
        logger.debug("Meses de envejecimiento establecidos a: {}", agingMonths);
    }
    
    @Override
    public void showInformation() {
        logger.info("=== VINO ROSADO ===");
        logger.info("Nombre: {}", getName());
        logger.info("Precio: {} €", getPrice());
        logger.info("Stock: {} unidades", getStock());
        logger.info("Año: {}", getYear());
        logger.info("Tipo de uva: {}", grapeType);
        logger.info("Dulzura: {}", sweetness);
        logger.info("Meses en barrica: {}", agingMonths);
    }

    public void showStyleInfo() {
        logger.info("Este vino rosado estilo {} es perfecto para...", sweetness);
        if (agingMonths > 0) {
            logger.info("Ha sido envejecido {} meses, aportando complejidad", agingMonths);
        } else {
            logger.info("Es un rosado fresco y joven, ideal para consumir pronto");
        }
    }

}
