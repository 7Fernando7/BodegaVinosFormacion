package bodegavininho.domain.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para la entidad WineOrder.
 */
class WineOrderTest {
    
    @Test
    void testCreateEmptyOrder() {
        // Given
        String customerName = "Carlos Martínez";
        
        // When
        WineOrder order = WineOrder.create(customerName);
        
        // Then
        assertNotNull(order);
        assertNotNull(order.getId());
        assertEquals(customerName, order.getCustomerName());
        assertTrue(order.getItems().isEmpty());
        assertEquals(WineOrder.OrderStatus.PENDING, order.getStatus());
        assertNotNull(order.getOrderDate());
    }
    
    @Test
    void testCreateOrderWithItems() {
        // Given
        String customerName = "Carlos Martínez";
        WineOrder.OrderItem item1 = new WineOrder.OrderItem("1", "El Coto", 3, java.math.BigDecimal.valueOf(15.99));
        WineOrder.OrderItem item2 = new WineOrder.OrderItem("2", "Chablis", 2, java.math.BigDecimal.valueOf(22.50));
        
        // When
        WineOrder order = WineOrder.create(customerName, java.util.Arrays.asList(item1, item2));
        
        // Then
        assertEquals(2, order.getItems().size());
        assertEquals(3, order.getItems().get(0).getQuantity());
        assertEquals(2, order.getItems().get(1).getQuantity());
    }
    
    @Test
    void testOrderTotalAmount() {
        // Given
        String customerName = "Carlos Martínez";
        WineOrder.OrderItem item1 = new WineOrder.OrderItem("1", "El Coto", 3, java.math.BigDecimal.valueOf(15.99));
        WineOrder.OrderItem item2 = new WineOrder.OrderItem("2", "Chablis", 2, java.math.BigDecimal.valueOf(22.50));
        
        // When
        WineOrder order = WineOrder.create(customerName, java.util.Arrays.asList(item1, item2));
        
        // Then
        // 3 * 15.99 + 2 * 22.50 = 47.97 + 45.00 = 92.97
        assertEquals(92.97, order.getTotalAmount().doubleValue(), 0.01);
    }
    
    @Test
    void testOrderItemSubtotal() {
        // Given
        WineOrder.OrderItem item = new WineOrder.OrderItem("1", "El Coto", 5, java.math.BigDecimal.valueOf(15.00));
        
        // When & Then
        assertEquals(75.00, item.getSubtotal().doubleValue(), 0.01);
    }
    
    @Test
    void testAddItem() {
        // Given
        WineOrder order = WineOrder.create("Carlos Martínez");
        WineOrder.OrderItem newItem = new WineOrder.OrderItem("1", "El Coto", 3, java.math.BigDecimal.valueOf(15.99));
        
        // When
        WineOrder updatedOrder = order.addItem(newItem);
        
        // Then
        assertEquals(1, updatedOrder.getItems().size());
        assertEquals(3, updatedOrder.getItems().get(0).getQuantity());
    }
    
    @Test
    void testCompleteOrder() {
        // Given
        WineOrder order = WineOrder.create("Carlos Martínez");
        
        // When
        WineOrder completedOrder = order.complete();
        
        // Then
        assertEquals(WineOrder.OrderStatus.COMPLETED, completedOrder.getStatus());
        assertNotNull(completedOrder.getProcessedDate());
    }
    
    @Test
    void testOrderStatus() {
        // Then
        assertEquals(4, WineOrder.OrderStatus.values().length);
        assertEquals("PENDING", WineOrder.OrderStatus.PENDING.name());
        assertEquals("PROCESSING", WineOrder.OrderStatus.PROCESSING.name());
        assertEquals("COMPLETED", WineOrder.OrderStatus.COMPLETED.name());
        assertEquals("CANCELLED", WineOrder.OrderStatus.CANCELLED.name());
    }
}
