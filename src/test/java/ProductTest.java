
import com.pretzel.backend.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    private Product underTest;

    @BeforeEach
    void setUp() {
        underTest = new Product();
    }

    @Test
    void testSetIdShouldStoreValueWhenValidIdGiven() {
        Long id = 10L;

        underTest.setId(id);

        assertEquals(id, underTest.getId());
    }

    @Test
    void testSetNameShouldStoreValueWhenValidNameGiven() {
        String name = "Test Pretzel";

        underTest.setName(name);

        assertEquals(name, underTest.getName());
    }

    @Test
    void testSetTypeShouldStoreValueWhenValidTypeGiven() {
        String type = "sweet";

        underTest.setType(type);

        assertEquals(type, underTest.getType());
    }

    @Test
    void testSetPriceShouldStoreValueWhenValidPriceGiven() {
        double price = 9.99;

        underTest.setPrice(price);

        assertEquals(price, underTest.getPrice());
    }

    @Test
    void testSetImageUrlShouldStoreValueWhenValidUrlGiven() {
        String url = "http://example.com/image.png";

        underTest.setImageUrl(url);

        assertEquals(url, underTest.getImageUrl());
    }

    @Test
    void testSetDescriptionShouldStoreValueWhenValidDescriptionGiven() {
        String description = "Tasty pretzel";
        underTest.setDescription(description);

        assertEquals(description, underTest.getDescription());
    }

    @Test
    void testFiveArgConstructorShouldInitializeFieldsWhenValidValuesGiven() {
        String name = "Pretzel Classic";
        String type = "classic";
        double price = 12.5;
        String imageUrl = "http://image.png";
        String description = "Classic and tasty pretzel.";

        Product product = new Product(name, type, price, imageUrl, description);

        assertNull(product.getId()); // JPA ID nincs inicializálva konstruktorban
        assertEquals(name, product.getName());
        assertEquals(type, product.getType());
        assertEquals(price, product.getPrice());
        assertEquals(imageUrl, product.getImageUrl());
        assertEquals(description, product.getDescription());
    }
}
