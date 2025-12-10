

import com.pretzel.backend.controller.ProductController;
import com.pretzel.backend.model.Product;
import com.pretzel.backend.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductControllerTest {

    private ProductRepository repo;
    private ProductController underTest;

    @BeforeEach
    void setUp() {
        repo = mock(ProductRepository.class);
        underTest = new ProductController(repo);
    }

    @Test
    void testAllShouldReturnAllProductsWhenRepositoryReturnsList() {
        Product p1 = new Product("A", "t", 1, "img", "desc");
        Product p2 = new Product("B", "t", 2, "img", "desc");
        when(repo.findAll()).thenReturn(List.of(p1, p2));

        List<Product> result = underTest.all();

        assertEquals(2, result.size());
        assertEquals("A", result.get(0).getName());
        verify(repo, times(1)).findAll();
    }

    @Test
    void testGetByIdShouldReturnProductWhenIdExists() {
        Product p = new Product("A", "t", 1, "img", "desc");
        when(repo.findById(1L)).thenReturn(Optional.of(p));

        Optional<Product> result = underTest.getById(1L);

        assertTrue(result.isPresent());
        assertEquals("A", result.get().getName());
        verify(repo, times(1)).findById(1L);
    }

    @Test
    void testGetByIdShouldReturnEmptyOptionalWhenIdDoesNotExist() {
        when(repo.findById(999L)).thenReturn(Optional.empty());

        Optional<Product> result = underTest.getById(999L);

        assertTrue(result.isEmpty());
        verify(repo, times(1)).findById(999L);
    }

    @Test
    void testGetByTypeShouldReturnProductsWhenTypeExists() {
        String type = "dessert";
        Product p1 = new Product("A", type, 10, "img", "desc");
        Product p2 = new Product("B", type, 12, "img", "desc");
        when(repo.findByType(type)).thenReturn(List.of(p1, p2));

        List<Product> result = underTest.getByType(type);

        assertEquals(2, result.size());
        assertEquals(type, result.get(0).getType());
        verify(repo, times(1)).findByType(type);
    }

    @Test
    void testGetByTypeShouldReturnEmptyListWhenTypeNotFound() {
        when(repo.findByType("unknown")).thenReturn(List.of());

        List<Product> result = underTest.getByType("unknown");

        assertTrue(result.isEmpty());
        verify(repo, times(1)).findByType("unknown");
    }
}
