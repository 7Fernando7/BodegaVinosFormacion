package bodegavininho.domain.exception;

/**
 * Excepción personalizada cuando el stock es insuficiente.
 */
public class StockInsuficienteException extends RuntimeException {
        
        public StockInsuficienteException(String mensaje) {
                super(mensaje);
        }
        
        public StockInsuficienteException(String mensaje, Throwable causa) {
                super(mensaje, causa);
        }
}
