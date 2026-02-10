package bodegavininho.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Entidad Pedido - Representa un pedido realizado por un restaurante.
 * Un pedido contiene múltiples líneas de pedido y tiene un estado.
 */
public class Pedido {
    
    public enum EstadoPedido {
        PENDIENTE,
        CONFIRMADO,
        EN_PREPARACION,
        ENVIADO,
        ENTREGADO,
        CANCELADO
    }
    
    private final String id;
    private final String restauranteId;
    private final String restauranteNombre;
    private final LocalDateTime fechaPedido;
    private LocalDateTime fechaActualizacion;
    private final BigDecimal total;
    private final List<LineaPedido> lineas;
    private EstadoPedido estado;
    private final String notas;
    
    private Pedido(Builder builder) {
        this.id = builder.id != null ? builder.id : UUID.randomUUID().toString();
        this.restauranteId = builder.restauranteId;
        this.restauranteNombre = builder.restauranteNombre;
        this.fechaPedido = builder.fechaPedido != null ? builder.fechaPedido : LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
        this.total = builder.lineas.stream()
                .map(LineaPedido::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        this.lineas = new ArrayList<>(builder.lineas);
        this.estado = builder.estado != null ? builder.estado : EstadoPedido.PENDIENTE;
        this.notas = builder.notas != null ? builder.notas : "";
    }
    
    public static Pedido create(String restauranteId, String restauranteNombre, List<LineaPedido> lineas, String notas) {
        if (restauranteId == null || restauranteId.isBlank()) {
            throw new IllegalArgumentException("El ID del restaurante es obligatorio");
        }
        if (lineas == null || lineas.isEmpty()) {
            throw new IllegalArgumentException("El pedido debe tener al menos una línea");
        }
        return Builder.builder()
                .restauranteId(restauranteId)
                .restauranteNombre(restauranteNombre)
                .lineas(lineas)
                .notas(notas)
                .build();
    }
    
    public void confirmar() {
        this.estado = EstadoPedido.CONFIRMADO;
        this.fechaActualizacion = LocalDateTime.now();
    }
    
    public void cancelar() {
        this.estado = EstadoPedido.CANCELADO;
        this.fechaActualizacion = LocalDateTime.now();
    }
    
    public void addLinea(LineaPedido linea) {
        this.lineas.add(linea);
        this.fechaActualizacion = LocalDateTime.now();
    }
    
    // Getters
    public String getId() { return id; }
    public String getRestauranteId() { return restauranteId; }
    public String getRestauranteNombre() { return restauranteNombre; }
    public LocalDateTime getFechaPedido() { return fechaPedido; }
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public BigDecimal getTotal() { return total; }
    public List<LineaPedido> getLineas() { return new ArrayList<>(lineas); }
    public EstadoPedido getEstado() { return estado; }
    public String getNotas() { return notas; }
    
    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
        this.fechaActualizacion = LocalDateTime.now();
    }
    
    public static class Builder {
        private String id;
        private String restauranteId;
        private String restauranteNombre;
        private LocalDateTime fechaPedido;
        private List<LineaPedido> lineas = new ArrayList<>();
        private EstadoPedido estado;
        private String notas;
        
        public static Builder builder() { return new Builder(); }
        
        public Builder id(String id) { this.id = id; return this; }
        public Builder restauranteId(String restauranteId) { this.restauranteId = restauranteId; return this; }
        public Builder restauranteNombre(String restauranteNombre) { this.restauranteNombre = restauranteNombre; return this; }
        public Builder fechaPedido(LocalDateTime fechaPedido) { this.fechaPedido = fechaPedido; return this; }
        public Builder lineas(List<LineaPedido> lineas) { this.lineas = lineas; return this; }
        public Builder estado(EstadoPedido estado) { this.estado = estado; return this; }
        public Builder notas(String notas) { this.notas = notas; return this; }
        
        public Pedido build() { return new Pedido(this); }
    }
}
