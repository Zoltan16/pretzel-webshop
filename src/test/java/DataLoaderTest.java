
import com.pretzel.backend.config.DataLoader;
import com.pretzel.backend.model.Product;
import com.pretzel.backend.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.CommandLineRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataLoaderTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private DataLoader underTest;

    private CommandLineRunner runner;

    @BeforeEach
    void setUp() {
        // initDatabase létrehozza a CommandLineRunner-t, ezt futtatjuk később
        runner = underTest.initDatabase(productRepository);
    }

    @Test
    void testInitDatabaseShouldSaveProductsWhenRepositoryIsEmpty() throws Exception {
        when(productRepository.count()).thenReturn(0L);

        runner.run();

        verify(productRepository, atLeastOnce()).save(any(Product.class));

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository, atLeast(1)).save(captor.capture());

        List<Product> savedProducts = captor.getAllValues();

        assertFalse(savedProducts.isEmpty());
        assertTrue(savedProducts.size() >= 1); // valójában kb. 50 termék mentés történik
    }

    @Test
    void testInitDatabaseShouldNotSaveProductsWhenRepositoryIsNotEmpty() throws Exception {
        when(productRepository.count()).thenReturn(10L);

        runner.run();

        verify(productRepository, never()).save(any(Product.class));
    }
}
