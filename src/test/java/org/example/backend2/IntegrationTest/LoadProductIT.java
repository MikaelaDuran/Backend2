package org.example.backend2.IntegrationTest;


import org.example.backend2.models.Product;
import org.example.backend2.service.FakeStoreApiRequestService;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class LoadProductIT {

    private final FakeStoreApiRequestService fakeStoreApiRequestService = new FakeStoreApiRequestService();

    @Test
    public void loadProduct() throws IOException {
        Product[] products = fakeStoreApiRequestService.getProductsRestTemplate();

        assertNotNull(products);
        assertTrue(products.length > 0);

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
