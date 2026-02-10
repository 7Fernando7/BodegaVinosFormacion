package bodegavininho.domain.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Entidad Restaurante - Representa un cliente B2B del sistema.
 * Un restaurante puede realizar múltiples pedidos.
 */
public class Restaurante {
    
    private final String id;
    private final String nombre;
    private final String direccion;
    private final String telefono;
    private final String email;
    private final boolean activo;
    private final LocalDateTime fechaRegistro;
    private final List<Pedido> pedidos;
    
    private Restaurante(Builder builder) {
        this.id = builder.id != null ? builder.id : UUID.randomUUID().toString();
        this.nombre = builder.nombre;
        this.direccion = builder.direccion;
        this.telefono = builder.telefono;
        this.email = builder.email;
        this.activo = builder.activo;
        this.fechaRegistro = builder.fechaRegistro;
        this.pedidos = new ArrayList<>();
    }
    
    public static Restaurante create(String nombre, String direccion, String telefono, String email) {
        return Builder.builder()
                .nombre(nombre)
                .direccion(direccion)
                .telefono(telefono)
                .email(email)
                .activo(true)
                .fechaRegistro(LocalDateTime.now())
                .build();
    }
    
    public void addPedido(Pedido pedido) {
        this.pedidos.add(pedido);
    }
    
    // Getters
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDireccion() { return direccion; }
    public String getTelefono() { return telefono; }
    public String getEmail() { return email; }
    public boolean isActivo() { return activo; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public List<Pedido> getPedidos() { return new ArrayList<>(pedidos); }
    
    public static class Builder {
        private String id;
        private String nombre;
        private String direccion;
        private String telefono;
        private String email;
        private boolean activo = true;
        private LocalDateTime fechaRegistro = LocalDateTime.now();
        
        public static Builder builder() { return new Builder(); }
        
        public Builder id(String id) { this.id = id; return this; }
        public Builder nombre(String nombre) { this.nombre = nombre; return this; }
        public Builder direccion(String direccion) { this.direccion = direccion; return this; }
        public Builder telefono(String telefono) { this.telefono = telefono; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder activo(boolean activo) { this.activo = activo; return this; }
        public Builder fechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; return this; }
        
        public Restaurante build() { return new Restaurante(this); }
    }
}
