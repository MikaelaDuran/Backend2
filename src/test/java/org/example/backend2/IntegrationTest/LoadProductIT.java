package org.example.backend2.IntegrationTest;


import org.example.backend2.models.Product;
import org.example.backend2.repository.ProductRepository;
import org.example.backend2.service.FakeStoreProductSyncService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class LoadProductIT {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private FakeStoreProductSyncService fakeStoreProductSyncService;

    @Test
    public void loadProduct() throws IOException {
        Product[] products = fakeStoreProductSyncService.getProductsFromApi();

        assertNotNull(products);
        assertNotEquals(0, products.length);

        for (Product product : products) {
            assertTrue(product.getId() > 0);
            assertNotNull(product.getTitle());
            assertTrue(product.getPrice() > 0);
            assertNotNull(product.getDescription());
            assertNotNull(product.getCategory());
            assertNotNull(product.getImage());
        }
    }
}
