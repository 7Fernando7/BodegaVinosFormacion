package bodegavininho;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        logger.info("=== BODEGA VININHO ===");

        // Crear diferentes vinos usando constructores con parámetros
        RedWine redWine = new RedWine("El Coto", 2020, 15.99, 50, "Tempranillo", true);
        redWine.setCountry("La Rioja");

        WhiteWine whiteWine = new WhiteWine("Chablis", 2021, 22.50, 30, "Chardonnay", true, "Borgoña");
        whiteWine.setCountry("Francia");

        RoseWine roseWine = new RoseWine("Whispering Angel", 2022, 12.99, 100, "Grenache", "Seco", 0);
        roseWine.setCountry("Provenza");

        // Mostrar todos los vinos
        logger.info("--- VINOS TINTOS ---");
        redWine.showInformation();
        redWine.showAgingInfo();

        logger.info("--- VINOS BLANCOS ---");
        whiteWine.showInformation();
        whiteWine.showServingTip();

        logger.info("--- VINOS ROSADOS ---");
        roseWine.showInformation();
        roseWine.showStyleInfo();

        // Demostrar el uso de setters con constructor por defecto
        logger.info("--- CREANDO VINO CON CONSTRUCTOR POR DEFECTO ---");
        RedWine defaultRed = new RedWine();
        defaultRed.setName("Ribera del Duero");
        defaultRed.setYear(2018);
        defaultRed.setPrice(45.00);
        defaultRed.setStock(15);
        defaultRed.setGrapeType("Cabernet Sauvignon");
        defaultRed.setHasBarrel(true);
        defaultRed.showInformation();
        defaultRed.showAgingInfo();
    }
}
