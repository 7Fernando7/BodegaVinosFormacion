package bodegavininho;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import bodegavininho.application.usecase.ImportExternalWinesUseCase;
import bodegavininho.application.usecase.WineUseCase;
import bodegavininho.domain.port.ExternalWineProvider;
import bodegavininho.domain.port.WineRepository;
import bodegavininho.infrastructure.adapter.OpenFoodFactsWineAdapter;
import bodegavininho.infrastructure.repository.InMemoryWineRepository;

/**
 * Punto de entrada de la aplicación Spring Boot.
 */
@SpringBootApplication
@ComponentScan(basePackages = "bodegavininho")
public class VinoApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(VinoApplication.class, args);
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
}
