package org.example.backend2.unit.service;

import org.example.backend2.models.Product;
import org.example.backend2.repository.ProductRepository;
import org.example.backend2.service.FakeStoreProductSyncService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FakeStoreProductSyncServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private FakeStoreProductSyncService fakeStoreProductSyncService;

    @Test
    void syncProducts() {
        List<Product> products = new ArrayList<>();
        products.add(new Product());
        products.add(new Product());
        products.add(new Product());
        products.add(new Product());

        fakeStoreProductSyncService.syncProducts(products.toArray(new Product[0]));
        verify(productRepository, times(1)).saveAll(any());

        verify(productRepository, never()).deleteAllById(any());
    }

    @Test
    void syncProductsDeleteOldProducts() {
        List<Product> products = new ArrayList<>();
        products.add(new Product());
        products.add(new Product());
        products.add(new Product());
        products.add(new Product());

        List<Product> oldProducts = new ArrayList<>();

        Product product = new Product();
        product.setId(1L);

        oldProducts.add(product);

        when(productRepository.findAll()).thenReturn(oldProducts);

        fakeStoreProductSyncService.syncProducts(products.toArray(new Product[0]));
        verify(productRepository, times(1)).saveAll(any());

        verify(productRepository, times(1)).deleteAllById(any());
    }

    @Test
    void getProducts() {
        List<Product> products = new ArrayList<>();
        products.add(new Product());
        products.add(new Product());


        when(productRepository.findAll()).thenReturn(products);
        assertEquals(products, fakeStoreProductSyncService.getProducts());
    }
}