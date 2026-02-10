package bodegavininho.domain.exception;

/**
 * Excepción personalizada cuando no se encuentra un restaurante.
 */
public class RestauranteNotFoundException extends RuntimeException {
        
        public RestauranteNotFoundException(String mensaje) {
                super(mensaje);
        }
        
        public RestauranteNotFoundException(String mensaje, Throwable causa) {
                super(mensaje, causa);
        }
}
