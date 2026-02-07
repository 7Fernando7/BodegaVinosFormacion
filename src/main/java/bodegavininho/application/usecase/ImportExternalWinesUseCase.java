package bodegavininho.application.usecase;

import bodegavininho.application.dto.WineDTO;
import bodegavininho.domain.model.Wine;
import bodegavininho.domain.port.ExternalWineProvider;
import bodegavininho.domain.port.WineRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Caso de uso para importar vinos desde fuentes externas.
 */
public class ImportExternalWinesUseCase {
    
    private static final Logger logger = LoggerFactory.getLogger(ImportExternalWinesUseCase.class);
    
    private final ExternalWineProvider externalWineProvider;
    private final WineRepository wineRepository;
    
    public ImportExternalWinesUseCase(ExternalWineProvider externalWineProvider, WineRepository wineRepository) {
        this.externalWineProvider = externalWineProvider;
        this.wineRepository = wineRepository;
    }
    
    public List<WineDTO> importAllWines() {
        logger.info("Importando vinos desde fuente externa...");
        List<Wine> externalWines = externalWineProvider.fetchWines();
        
        logger.info("Se encontraron {} vinos externos", externalWines.size());
        
        List<Wine> savedWines = externalWines.stream()
            .map(wineRepository::save)
            .collect(Collectors.toList());
        
        logger.info("Se guardaron {} vinos en el repositorio", savedWines.size());
        
        return savedWines.stream()
            .map(WineDTO::fromDomain)
            .collect(Collectors.toList());
    }
    
    public WineDTO importByBarcode(String barcode) {
        logger.info("Buscando vino por código de barras: {}", barcode);
        Wine wine = externalWineProvider.fetchByBarcode(barcode);
        
        if (wine != null) {
            Wine savedWine = wineRepository.save(wine);
            logger.info("Vino importado y guardado: {}", savedWine.getName());
            return WineDTO.fromDomain(savedWine);
        }
        
        logger.warn("No se encontró vino con código de barras: {}", barcode);
        return null;
    }
}
