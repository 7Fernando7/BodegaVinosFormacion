package bodegavininho.domain.exception;

/**
 * Excepción personalizada cuando no se encuentra un vino.
 */
public class VinoNotFoundException extends RuntimeException {
        
        public VinoNotFoundException(String mensaje) {
                super(mensaje);
        }
        
        public VinoNotFoundException(String mensaje, Throwable causa) {
                super(mensaje, causa);
        }
}
