package bodegavininho.domain.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para la entidad Wine.
 */
class WineTest {
    
    @Test
    void testCreateWine() {
        // Given
        String name = "El Coto";
        int year = 2020;
        double price = 15.99;
        int stock = 50;
        String country = "La Rioja";
        Wine.WineType type = Wine.WineType.RED;
        
        // When
        Wine wine = Wine.create(name, year, price, stock, country, type);
        
        // Then
        assertNotNull(wine);
        assertNotNull(wine.getId());
        assertEquals(name, wine.getName());
        assertEquals(year, wine.getYear());
        assertEquals(price, wine.getPrice().doubleValue(), 0.01);
        assertEquals(stock, wine.getStock());
        assertEquals(country, wine.getCountry());
        assertEquals(type, wine.getType());
    }
    
    @Test
    void testHasStock_SufficientStock() {
        // Given
        Wine wine = Wine.create("El Coto", 2020, 15.99, 50, "La Rioja", Wine.WineType.RED);
        
        // When & Then
        assertTrue(wine.hasStock(30));
        assertTrue(wine.hasStock(50));
    }
    
    @Test
    void testHasStock_InsufficientStock() {
        // Given
        Wine wine = Wine.create("El Coto", 2020, 15.99, 50, "La Rioja", Wine.WineType.RED);
        
        // When & Then
        assertFalse(wine.hasStock(51));
        assertFalse(wine.hasStock(100));
    }
    
    @Test
    void testReduceStock() {
        // Given
        Wine wine = Wine.create("El Coto", 2020, 15.99, 50, "La Rioja", Wine.WineType.RED);
        
        // When
        Wine reducedWine = wine.reduceStock(10);
        
        // Then
        assertEquals(40, reducedWine.getStock());
    }
    
    @Test
    void testAddStock() {
        // Given
        Wine wine = Wine.create("El Coto", 2020, 15.99, 50, "La Rioja", Wine.WineType.RED);
        
        // When
        Wine addedWine = wine.addStock(20);
        
        // Then
        assertEquals(70, addedWine.getStock());
    }
    
    @Test
    void testWineTypes() {
        // Then
        assertEquals(3, Wine.WineType.values().length);
        assertEquals("Vino Tinto", Wine.WineType.RED.getDisplayName());
        assertEquals("Vino Blanco", Wine.WineType.WHITE.getDisplayName());
        assertEquals("Vino Rosado", Wine.WineType.ROSE.getDisplayName());
    }
}
