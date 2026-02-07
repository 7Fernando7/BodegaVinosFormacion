package bodegavininho.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Entidad de dominio WineOrder - Representa un pedido de vinos.
 */
public class WineOrder {
    
    private final String id;
    private final String customerName;
    private final List<OrderItem> items;
    private OrderStatus status;
    private final LocalDateTime orderDate;
    private LocalDateTime processedDate;
    private final BigDecimal totalAmount;
    
    public enum OrderStatus {
        PENDING,
        PROCESSING,
        COMPLETED,
        CANCELLED
    }
    
    public static class OrderItem {
        private final String wineId;
        private final String wineName;
        private final int quantity;
        private final BigDecimal unitPrice;
        
        public OrderItem(String wineId, String wineName, int quantity, BigDecimal unitPrice) {
            this.wineId = wineId;
            this.wineName = wineName;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
        }
        
        public String getWineId() { return wineId; }
        public String getWineName() { return wineName; }
        public int getQuantity() { return quantity; }
        public BigDecimal getUnitPrice() { return unitPrice; }
        
        public BigDecimal getSubtotal() {
            return unitPrice.multiply(BigDecimal.valueOf(quantity));
        }
    }
    
    private WineOrder(String customerName, List<OrderItem> items, OrderStatus status) {
        this.id = UUID.randomUUID().toString();
        this.customerName = customerName;
        this.items = new ArrayList<>(items);
        this.status = status;
        this.orderDate = LocalDateTime.now();
        this.totalAmount = calculateTotal();
    }
    
    public static WineOrder create(String customerName) {
        return new WineOrder(customerName, new ArrayList<>(), OrderStatus.PENDING);
    }
    
    public static WineOrder create(String customerName, List<OrderItem> items) {
        return new WineOrder(customerName, items, OrderStatus.PENDING);
    }
    
    private BigDecimal calculateTotal() {
        return items.stream()
            .map(OrderItem::getSubtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    // Getters
    public String getId() { return id; }
    public String getCustomerName() { return customerName; }
    public List<OrderItem> getItems() { return new ArrayList<>(items); }
    public OrderStatus getStatus() { return status; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public LocalDateTime getProcessedDate() { return processedDate; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    
    public WineOrder addItem(OrderItem item) {
        List<OrderItem> newItems = new ArrayList<>(this.items);
        newItems.add(item);
        return new WineOrder(customerName, newItems, status);
    }
    
    public WineOrder complete() {
        WineOrder order = new WineOrder(customerName, items, OrderStatus.COMPLETED);
        order.processedDate = LocalDateTime.now();
        return order;
    }
}
