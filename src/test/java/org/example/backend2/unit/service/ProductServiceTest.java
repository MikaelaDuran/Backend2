package org.example.backend2.unit.service;

import org.example.backend2.exceptions.ProductNotFoundException;
import org.example.backend2.models.Product;
import org.example.backend2.repository.ProductRepository;
import org.example.backend2.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        product1 = new Product();
        product1.setId(1L);
        product1.setTitle("Product 1");
        product1.setCategory("Electronics");
        product1.setPrice(10.99);

        product2 = new Product();
        product2.setId(2L);
        product2.setTitle("Product 2");
        product2.setCategory("Electronics");
        product2.setPrice(25.50);
    }

    @Test
    void testGetProductsByCategory_Success() {
        when(productRepository.findAllByCategory("Electronics"))
                .thenReturn(Arrays.asList(product1, product2));

        List<Product> result = productService.getProductsByCategory("Electronics");

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Product 1", result.get(0).getTitle());
        verify(productRepository).findAllByCategory("Electronics");
    }

    @Test
    void testGetProductsByCategory_Empty() {
        when(productRepository.findAllByCategory("Shoes")).thenReturn(Collections.emptyList());

        List<Product> result = productService.getProductsByCategory("Shoes");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(productRepository).findAllByCategory("Shoes");
    }

    @Test
    void testGetProductById_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));

        Product result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals("Product 1", result.getTitle());
        verify(productRepository).findById(1L);
    }

    @Test
    void testGetProductById_NotFound() {
        when(productRepository.findById(5L)).thenReturn(Optional.empty());

        ProductNotFoundException exception = assertThrows(
                ProductNotFoundException.class,
                () -> productService.getProductById(5L)
        );

        assertEquals("Product not found with id 5", exception.getMessage());
        verify(productRepository).findById(5L);
    }

    @Test
    void testGetAllProducts_Success() {
        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));

        List<Product> result = productService.getAllProducts();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(productRepository).findAll();
    }

    @Test
    void testGetAllProducts_EmptyList() {
        when(productRepository.findAll()).thenReturn(Collections.emptyList());

        List<Product> result = productService.getAllProducts();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(productRepository).findAll();
    }
}