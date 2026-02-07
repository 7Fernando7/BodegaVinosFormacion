package bodegavininho.application.usecase;

import bodegavininho.application.dto.CreateWineRequest;
import bodegavininho.application.dto.WineDTO;
import bodegavininho.domain.model.Wine;
import bodegavininho.domain.port.WineRepository;
import bodegavininho.infrastructure.repository.InMemoryWineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

/**
 * Tests unitarios para WineUseCase.
 */
class WineUseCaseTest {
    
    private WineRepository repository;
    private WineUseCase wineUseCase;
    
    @BeforeEach
    void setUp() {
        repository = new InMemoryWineRepository();
        wineUseCase = new WineUseCase(repository);
    }
    
    @Test
    void testCreateWine() {
        // Given
        CreateWineRequest request = new CreateWineRequest(
            "El Coto", 2020, 15.99, 50, "La Rioja", "RED"
        );
        
        // When
        WineDTO result = wineUseCase.createWine(request);
        
        // Then
        assertNotNull(result);
        assertNotNull(result.id());
        assertEquals("El Coto", result.name());
        assertEquals(2020, result.year());
        assertEquals(15.99, result.price().doubleValue(), 0.01);
        assertEquals(50, result.stock());
        assertEquals("La Rioja", result.country());
        assertEquals("RED", result.type());
    }
    
    @Test
    void testGetWineById() {
        // Given
        CreateWineRequest request = new CreateWineRequest(
            "El Coto", 2020, 15.99, 50, "La Rioja", "RED"
        );
        WineDTO created = wineUseCase.createWine(request);
        
        // When
        Optional<WineDTO> found = wineUseCase.getWineById(created.id());
        
        // Then
        assertTrue(found.isPresent());
        assertEquals(created.name(), found.get().name());
    }
    
    @Test
    void testGetWineById_NotFound() {
        // When
        Optional<WineDTO> found = wineUseCase.getWineById("non-existent-id");
        
        // Then
        assertFalse(found.isPresent());
    }
    
    @Test
    void testGetWineByName() {
        // Given
        wineUseCase.createWine(new CreateWineRequest("El Coto", 2020, 15.99, 50, "La Rioja", "RED"));
        
        // When
        Optional<WineDTO> found = wineUseCase.getWineByName("El Coto");
        
        // Then
        assertTrue(found.isPresent());
        assertEquals("El Coto", found.get().name());
    }
    
    @Test
    void testGetAllWines() {
        // Given
        wineUseCase.createWine(new CreateWineRequest("El Coto", 2020, 15.99, 50, "La Rioja", "RED"));
        wineUseCase.createWine(new CreateWineRequest("Chablis", 2021, 22.50, 30, "Borgoña", "WHITE"));
        
        // When
        List<WineDTO> allWines = wineUseCase.getAllWines();
        
        // Then
        assertEquals(2, allWines.size());
    }
    
    @Test
    void testUpdateStock() {
        // Given
        WineDTO created = wineUseCase.createWine(
            new CreateWineRequest("El Coto", 2020, 15.99, 50, "La Rioja", "RED")
        );
        
        // When
        WineDTO updated = wineUseCase.updateStock(created.id(), 75);
        
        // Then
        assertEquals(75, updated.stock());
    }
    
    @Test
    void testDeleteWine() {
        // Given
        WineDTO created = wineUseCase.createWine(
            new CreateWineRequest("El Coto", 2020, 15.99, 50, "La Rioja", "RED")
        );
        
        // When
        wineUseCase.deleteWine(created.id());
        
        // Then
        assertFalse(wineUseCase.getWineById(created.id()).isPresent());
        assertEquals(0, wineUseCase.getAllWines().size());
    }
}
