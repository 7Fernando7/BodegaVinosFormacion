package bodegavininho.domain.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para la entidad StockItem.
 */
class StockItemTest {
    
    @Test
    void testCreateStockItem() {
        // Given
        Wine wine = Wine.create("El Coto", 2020, 15.99, 50, "La Rioja", Wine.WineType.RED);
        int quantity = 100;
        int minQuantity = 20;
        String location = "Bodega A";
        
        // When
        StockItem stockItem = new StockItem(wine, quantity, minQuantity, location);
        
        // Then
        assertNotNull(stockItem);
        assertNotNull(stockItem.getId());
        assertEquals(wine, stockItem.getWine());
        assertEquals(quantity, stockItem.getQuantity());
        assertEquals(minQuantity, stockItem.getMinQuantity());
        assertEquals(location, stockItem.getLocation());
        assertNotNull(stockItem.getLastUpdated());
    }
    
    @Test
    void testIsLowStock_True() {
        // Given
        Wine wine = Wine.create("El Coto", 2020, 15.99, 50, "La Rioja", Wine.WineType.RED);
        StockItem stockItem = new StockItem(wine, 15, 20, "Bodega A");
        
        // When & Then
        assertTrue(stockItem.isLowStock());
    }
    
    @Test
    void testIsLowStock_False() {
        // Given
        Wine wine = Wine.create("El Coto", 2020, 15.99, 50, "La Rioja", Wine.WineType.RED);
        StockItem stockItem = new StockItem(wine, 50, 20, "Bodega A");
        
        // When & Then
        assertFalse(stockItem.isLowStock());
    }
    
    @Test
    void testIsLowStock_AtMinimum() {
        // Given
        Wine wine = Wine.create("El Coto", 2020, 15.99, 50, "La Rioja", Wine.WineType.RED);
        StockItem stockItem = new StockItem(wine, 20, 20, "Bodega A");
        
        // When & Then
        assertTrue(stockItem.isLowStock());
    }
    
    @Test
    void testGetTotalValue() {
        // Given
        Wine wine = Wine.create("El Coto", 2020, 15.99, 50, "La Rioja", Wine.WineType.RED);
        StockItem stockItem = new StockItem(wine, 10, 5, "Bodega A");
        
        // When
        double totalValue = stockItem.getTotalValue().doubleValue();
        
        // Then
        assertEquals(159.90, totalValue, 0.01);
    }
    
    @Test
    void testAddQuantity() {
        // Given
        Wine wine = Wine.create("El Coto", 2020, 15.99, 50, "La Rioja", Wine.WineType.RED);
        StockItem stockItem = new StockItem(wine, 50, 20, "Bodega A");
        
        // When
        StockItem updatedItem = stockItem.addQuantity(25);
        
        // Then
        assertEquals(75, updatedItem.getQuantity());
    }
    
    @Test
    void testReduceQuantity() {
        // Given
        Wine wine = Wine.create("El Coto", 2020, 15.99, 50, "La Rioja", Wine.WineType.RED);
        StockItem stockItem = new StockItem(wine, 50, 20, "Bodega A");
        
        // When
        StockItem updatedItem = stockItem.reduceQuantity(15);
        
        // Then
        assertEquals(35, updatedItem.getQuantity());
    }
}
