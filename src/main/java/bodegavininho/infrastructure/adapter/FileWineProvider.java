package bodegavininho.infrastructure.adapter;

import bodegavininho.domain.model.Wine;
import bodegavininho.domain.port.ExternalWineProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Adaptador para leer vinos desde un archivo local (CSV/TXT).
 * Ejemplo didáctico de acceso a archivos en Java.
 * 
 * Formato esperado del archivo (CSV):
 *   nombre,año,precio,stock,país,tipo
 *   Ejemplo: Cabernet Sauvignon,2020,25.99,100,Chile,RED
 */
public class FileWineProvider implements ExternalWineProvider {
    
    private static final Logger logger = LoggerFactory.getLogger(FileWineProvider.class);
    
    private final String filePath;
    
    public FileWineProvider(String filePath) {
        this.filePath = filePath;
    }
    
    @Override
    public List<Wine> fetchWines() {
        logger.info("Leyendo vinos desde archivo: {}", filePath);
        List<Wine> wines = new ArrayList<>();
        
        // Verificar que el archivo existe
        if (!Files.exists(Paths.get(filePath))) {
            logger.warn("Archivo no encontrado: {}", filePath);
            return wines;
        }
        
        // try-with-resources: cierra el BufferedReader automáticamente
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true; // Para saltar la cabecera
            
            while ((line = reader.readLine()) != null) {
                // Saltar la primera línea (cabecera)
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                
                Wine wine = parseLine(line);
                if (wine != null) {
                    wines.add(wine);
                }
            }
            
            logger.info("Se leyeron {} vinos desde el archivo", wines.size());
            
        } catch (IOException e) {
            logger.error("Error al leer el archivo: {}", filePath, e);
        }
        
        return wines;
    }
    
    @Override
    public Wine fetchByBarcode(String barcode) {
        // Este proveedor no soporta códigos de barras
        logger.debug("Busca por barcode no implementada en FileWineProvider");
        return null;
    }
    
    @Override
    public List<Wine> fetchByCategory(String category) {
        // Este proveedor no soporta categorías
        logger.debug("Búsqueda por categoría no implementada en FileWineProvider");
        return List.of();
    }
    
    /**
     * Convierte una línea CSV a objeto Wine.
     * Formato: nombre,año,precio,stock,país,tipo
     */
    private Wine parseLine(String line) {
        try {
            // Separar por coma
            String[] parts = line.split(\",\");
            
            if (parts.length < 6) {
                logger.warn(\"Línea inválida (menos de 6 campos): {}\", line);
                return null;
            }
            
            String name = parts[0].trim();
            int year = Integer.parseInt(parts[1].trim());
            double price = Double.parseDouble(parts[2].trim());
            int stock = Integer.parseInt(parts[3].trim());
            String country = parts[4].trim();
            Wine.WineType type = Wine.WineType.valueOf(parts[5].trim().toUpperCase());
            
            return Wine.create(name, year, price, stock, country, type);
            
        } catch (NumberFormatException e) {
            logger.warn(\"Error al parsear línea (número inválido): {}\", line);
            return null;
        } catch (IllegalArgumentException e) {
            logger.warn(\"Error al parsear línea (tipo inválido): {}\", line);
            return null;
        }
    }
}
