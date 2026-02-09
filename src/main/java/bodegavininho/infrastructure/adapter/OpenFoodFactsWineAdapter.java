package bodegavininho.infrastructure.adapter;

import bodegavininho.domain.model.Wine;
import bodegavininho.domain.port.ExternalWineProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Adaptador para consumir la API de Open Food Facts.
 * https://world.openfoodfacts.org/
 * Usa HttpURLConnection con timeout básico.
 */
public class OpenFoodFactsWineAdapter implements ExternalWineProvider {
    
    private static final Logger logger = LoggerFactory.getLogger(OpenFoodFactsWineAdapter.class);
    private static final String BASE_URL = "https://world.openfoodfacts.org/cgi/search.pl";
    private static final String PRODUCT_URL = "https://world.openfoodfacts.org/api/v0/product/";
    
    // Timeout básico: 5 segundos de conexión y lectura
    private static final int CONNECT_TIMEOUT = 5000; // 5 segundos
    private static final int READ_TIMEOUT = 5000;    // 5 segundos
    
    @Override
    public List<Wine> fetchWines() {
        logger.info("Consumiendo API de Open Food Facts para vinos...");
        List<Wine> wines = new ArrayList<>();
        
        try {
            String urlStr = BASE_URL + 
                "?search_terms=wine&search_simple=1&action=process&json=1&page_size=20&fields=product_name,brands,code,quantity,countries";
            
            String jsonResponse = makeRequest(urlStr);
            wines = parseWinesFromJson(jsonResponse);
            
            logger.info("Se parsearon {} vinos desde Open Food Facts", wines.size());
        } catch (Exception e) {
            logger.error("Error al consumir Open Food Facts API", e);
        }
        
        return wines;
    }
    
    @Override
    public Wine fetchByBarcode(String barcode) {
        logger.info("Buscando vino por código de barras: {}", barcode);
        
        try {
            String urlStr = PRODUCT_URL + barcode + ".json";
            String jsonResponse = makeRequest(urlStr);
            
            if (jsonResponse.contains("\"status\":1")) {
                return parseSingleWineFromJson(jsonResponse);
            }
            
            logger.warn("Producto no encontrado en Open Food Facts: {}", barcode);
        } catch (Exception e) {
            logger.error("Error al buscar producto por código de barras", e);
        }
        
        return null;
    }
    
    @Override
    public List<Wine> fetchByCategory(String category) {
        logger.info("Buscando vinos por categoría: {}", category);
        List<Wine> wines = new ArrayList<>();
        
        try {
            String urlStr = BASE_URL + 
                "?search_terms=" + category + 
                "&search_simple=1&action=process&json=1&page_size=10&fields=product_name,brands,code,quantity,countries";
            
            String jsonResponse = makeRequest(urlStr);
            wines = parseWinesFromJson(jsonResponse);
            
            logger.info("Se encontraron {} vinos en categoría {}", wines.size(), category);
        } catch (Exception e) {
            logger.error("Error al buscar vinos por categoría", e);
        }
        
        return wines;
    }
    
    /**
     * Hace una petición HTTP GET con timeout.
     * @param urlStr URL a consultar
     * @return Respuesta como String
     */
    private String makeRequest(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("User-Agent", "VinoApp/1.0");
        
        // Configurar timeout (solo disponible en Java 13+)
        // Para versiones anteriores, usar: conn.setConnectTimeout(CONNECT_TIMEOUT);
        conn.setConnectTimeout(CONNECT_TIMEOUT);
        conn.setReadTimeout(READ_TIMEOUT);
        
        // try-with-resources: cierra el BufferedReader automáticamente
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        } finally {
            conn.disconnect(); // Siempre cerramos la conexión
        }
    }
    
    private List<Wine> parseWinesFromJson(String json) {
        List<Wine> wines = new ArrayList<>();
        
        Pattern productPattern = Pattern.compile("\"product_name\":\"([^\"]+)\"");
        Matcher productMatcher = productPattern.matcher(json);
        
        while (productMatcher.find()) {
            String name = productMatcher.group(1);
            if (!name.isEmpty() && !wines.stream().anyMatch(w -> w.getName().equals(name))) {
                Wine wine = Wine.create(name, 2020, 15.99, 50, "Francia", Wine.WineType.RED);
                wines.add(wine);
            }
        }
        
        return wines;
    }
    
    private Wine parseSingleWineFromJson(String json) {
        Pattern namePattern = Pattern.compile("\"product_name\":\"([^\"]+)\"");
        Matcher nameMatcher = namePattern.matcher(json);
        
        if (nameMatcher.find()) {
            String name = nameMatcher.group(1);
            return Wine.create(name, 2020, 15.99, 50, "Francia", Wine.WineType.RED);
        }
        
        return null;
    }
}
