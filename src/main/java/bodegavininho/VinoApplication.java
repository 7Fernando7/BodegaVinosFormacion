package bodegavininho;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import bodegavininho.application.usecase.ImportExternalWinesUseCase;
import bodegavininho.application.usecase.PedidoService;
import bodegavininho.application.usecase.WineUseCase;
import bodegavininho.domain.port.ExternalWineProvider;
import bodegavininho.domain.port.PedidoRepository;
import bodegavininho.domain.port.RestauranteRepository;
import bodegavininho.domain.port.WineRepository;
import bodegavininho.infrastructure.adapter.OpenFoodFactsWineAdapter;
import bodegavininho.infrastructure.repository.InMemoryPedidoRepository;
import bodegavininho.infrastructure.repository.InMemoryRestauranteRepository;
import bodegavininho.infrastructure.repository.InMemoryWineRepository;

/**
 * Punto de entrada de la aplicación Spring Boot.
 */
@SpringBootApplication
@ComponentScan(basePackages = "bodegavininho")
public class VinoApplication {
    
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(VinoApplication.class);
        logger.info("🚀 Iniciando aplicación VinoApplication...");
        SpringApplication.run(VinoApplication.class, args);
        logger.info("✅ VinoApplication iniciada correctamente");
    }
    
    @Bean
    public WineRepository wineRepository() {
        return new InMemoryWineRepository();
    }
    
    @Bean
    public ExternalWineProvider externalWineProvider() {
        return new OpenFoodFactsWineAdapter();
    }
    
    @Bean
    public WineUseCase wineUseCase(WineRepository wineRepository) {
        return new WineUseCase(wineRepository);
    }
    
    @Bean
    public ImportExternalWinesUseCase importExternalWinesUseCase(
            ExternalWineProvider externalWineProvider,
            WineRepository wineRepository) {
        return new ImportExternalWinesUseCase(externalWineProvider, wineRepository);
    }
    
    // ==============================
    // BEANS PARA SISTEMA DE PEDIDOS
    // ==============================
    
    @Bean
    public RestauranteRepository restauranteRepository() {
        return new InMemoryRestauranteRepository();
    }
    
    @Bean
    public PedidoRepository pedidoRepository() {
        return new InMemoryPedidoRepository();
    }
    
    @Bean
    public PedidoService pedidoService(
            PedidoRepository pedidoRepository,
            RestauranteRepository restauranteRepository,
            WineRepository wineRepository) {
        return new PedidoService(pedidoRepository, restauranteRepository, wineRepository);
    }
}
