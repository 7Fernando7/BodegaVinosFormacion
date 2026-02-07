package bodegavininho.domain.port;

import bodegavininho.domain.model.Wine;
import java.util.List;

/**
 * Puerto - Interfaz para proveedores externos de vinos.
 */
public interface ExternalWineProvider {
    List<Wine> fetchWines();
    Wine fetchByBarcode(String barcode);
    List<Wine> fetchByCategory(String category);
}
