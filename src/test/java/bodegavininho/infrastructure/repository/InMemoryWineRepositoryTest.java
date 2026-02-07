package bodegavininho.infrastructure.repository;

import bodegavininho.domain.model.Wine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

/**
 * Tests unitarios para InMemoryWineRepository.
 */
class InMemoryWineRepositoryTest {
    
    private InMemoryWineRepository repository;
    
    @BeforeEach
    void setUp() {
        repository = new InMemoryWineRepository();
    }
    
    @Test
    void testSaveWine() {
        // Given
        Wine wine = Wine.create("El Coto", 2020, 15.99, 50, "La Rioja", Wine.WineType.RED);
        
        // When
        Wine savedWine = repository.save(wine);
        
        // Then
        assertNotNull(savedWine);
        assertEquals(wine.getId(), savedWine.getId());
        assertEquals(wine.getName(), savedWine.getName());
    }
    
    @Test
    void testFindById_Found() {
        // Given
        Wine wine = Wine.create("El Coto", 2020, 15.99, 50, "La Rioja", Wine.WineType.RED);
        repository.save(wine);
        
        // When
        Optional<Wine> found = repository.findById(wine.getId());
        
        // Then
        assertTrue(found.isPresent());
        assertEquals(wine.getName(), found.get().getName());
    }
    
    @Test
    void testFindById_NotFound() {
        // When
        Optional<Wine> found = repository.findById("non-existent-id");
        
        // Then
        assertFalse(found.isPresent());
    }
    
    @Test
    void testFindByName_Found() {
        // Given
        Wine wine = Wine.create("El Coto", 2020, 15.99, 50, "La Rioja", Wine.WineType.RED);
        repository.save(wine);
        
        // When
        Optional<Wine> found = repository.findByName("El Coto");
        
        // Then
        assertTrue(found.isPresent());
        assertEquals(wine.getId(), found.get().getId());
    }
    
    @Test
    void testFindByName_NotFound() {
        // When
        Optional<Wine> found = repository.findByName("Non Existent Wine");
        
        // Then
        assertFalse(found.isPresent());
    }
    
    @Test
    void testFindAll() {
        // Given
        repository.save(Wine.create("El Coto", 2020, 15.99, 50, "La Rioja", Wine.WineType.RED));
        repository.save(Wine.create("Chablis", 2021, 22.50, 30, "Borgoña", Wine.WineType.WHITE));
        repository.save(Wine.create("Whispering Angel", 2022, 12.99, 100, "Provenza", Wine.WineType.ROSE));
        
        // When
        List<Wine> allWines = repository.findAll();
        
        // Then
        assertEquals(3, allWines.size());
    }
    
    @Test
    void testExistsById() {
        // Given
        Wine wine = Wine.create("El Coto", 2020, 15.99, 50, "La Rioja", Wine.WineType.RED);
        repository.save(wine);
        
        // When & Then
        assertTrue(repository.existsById(wine.getId()));
        assertFalse(repository.existsById("non-existent-id"));
    }
    
    @Test
    void testDeleteById() {
        // Given
        Wine wine = Wine.create("El Coto", 2020, 15.99, 50, "La Rioja", Wine.WineType.RED);
        repository.save(wine);
        
        // When
        repository.deleteById(wine.getId());
        
        // Then
        assertFalse(repository.existsById(wine.getId()));
        assertTrue(repository.findAll().isEmpty());
    }
    
    @Test
    void testUpdateStock() {
        // Given
        Wine wine = Wine.create("El Coto", 2020, 15.99, 50, "La Rioja", Wine.WineType.RED);
        repository.save(wine);
        
        // When
        Wine updatedWine = repository.updateStock(wine.getId(), 75);
        
        // Then
        assertEquals(75, updatedWine.getStock());
        
        // Verify in repository
        Optional<Wine> fromRepo = repository.findById(wine.getId());
        assertTrue(fromRepo.isPresent());
        assertEquals(75, fromRepo.get().getStock());
    }
    
    @Test
    void testUpdateStock_ThrowsExceptionForNonExistent() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            repository.updateStock("non-existent-id", 100);
        });
    }
}
