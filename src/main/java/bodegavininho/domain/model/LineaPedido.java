package bodegavininho.domain.model;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Entidad LineaPedido - Representa una línea individual dentro de un pedido.
 * Cada línea contiene un vino específico y su cantidad.
 */
public class LineaPedido {
    
    private final String id;
    private final String vinoId;
    private final String vinoNombre;
    private final int cantidad;
    private final BigDecimal precioUnitario;
    private final BigDecimal subtotal;
    
    private LineaPedido(Builder builder) {
        this.id = builder.id != null ? builder.id : UUID.randomUUID().toString();
        this.vinoId = builder.vinoId;
        this.vinoNombre = builder.vinoNombre;
        this.cantidad = builder.cantidad;
        this.precioUnitario = builder.precioUnitario;
        this.subtotal = builder.precioUnitario.multiply(BigDecimal.valueOf(builder.cantidad));
    }
    
    public static LineaPedido create(String vinoId, String vinoNombre, int cantidad, BigDecimal precioUnitario) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }
        if (precioUnitario == null || precioUnitario.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio unitario debe ser mayor a 0");
        }
        return Builder.builder()
                .vinoId(vinoId)
                .vinoNombre(vinoNombre)
                .cantidad(cantidad)
                .precioUnitario(precioUnitario)
                .build();
    }
    
    // Getters
    public String getId() { return id; }
    public String getVinoId() { return vinoId; }
    public String getVinoNombre() { return vinoNombre; }
    public int getCantidad() { return cantidad; }
    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public BigDecimal getSubtotal() { return subtotal; }
    
    public static class Builder {
        private String id;
        private String vinoId;
        private String vinoNombre;
        private int cantidad;
        private BigDecimal precioUnitario;
        
        public static Builder builder() { return new Builder(); }
        
        public Builder id(String id) { this.id = id; return this; }
        public Builder vinoId(String vinoId) { this.vinoId = vinoId; return this; }
        public Builder vinoNombre(String vinoNombre) { this.vinoNombre = vinoNombre; return this; }
        public Builder cantidad(int cantidad) { this.cantidad = cantidad; return this; }
        public Builder precioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; return this; }
        
        public LineaPedido build() { return new LineaPedido(this); }
    }
}
