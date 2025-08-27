package org.example.backend2.IntegrationTest;


import lombok.RequiredArgsConstructor;
import org.example.backend2.models.Product;
import org.example.backend2.service.FakeStoreProductSyncService;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class LoadProductIT {

    private final FakeStoreProductSyncService fakeStoreProductSyncService = new FakeStoreProductSyncService();

    @Test
    public void loadProduct() throws IOException {
        List<Product> products = fakeStoreProductSyncService.getProducts();

        assertNotNull(products);
        assertFalse(products.isEmpty());

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
