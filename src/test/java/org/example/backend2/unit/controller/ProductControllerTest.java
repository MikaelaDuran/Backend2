package org.example.backend2.unit.controller;

import org.example.backend2.controller.ProductController;
import org.example.backend2.models.Product;
import org.example.backend2.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @Mock
    private Model model;


    @InjectMocks
    private ProductController productController;

    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        product1 = new Product();
        product1.setId(1L);
        product1.setTitle("Laptop");
        product1.setCategory("Electronics");
        product1.setPrice(899.99);

        product2 = new Product();
        product2.setId(2L);
        product2.setTitle("Phone");
        product2.setCategory("Electronics");
        product2.setPrice(599.99);
    }

    @Test
    void testGetProductsByCategory_Success() {
        List<Product> products = Arrays.asList(product1, product2);
        when(productService.getProductsByCategory("Electronics")).thenReturn(products);

        String viewName = productController.getProductsByCategory("Electronics", model);
        assertEquals("products", viewName);
        verify(productService).getProductsByCategory("Electronics");
        verify(model).addAttribute("products", products);
    }

    @Test
    void testGetProductsByCategory_Empty() {
        when(productService.getProductsByCategory("Shoes")).thenReturn(Collections.emptyList());

        String viewName = productController.getProductsByCategory("Shoes", model);

        assertEquals("products", viewName);
        verify(productService).getProductsByCategory("Shoes");
        verify(model).addAttribute("products", Collections.emptyList());
    }

    @Test
    void testGetProductById_Success() {
        when(productService.getProductById(1L)).thenReturn(product1);

        String viewName = productController.getProductById(1L, model);

        assertEquals("", viewName);
        verify(productService).getProductById(1L);
        verify(model).addAttribute(product1);
    }

    @Test
    void testGetAllProducts_Success() {
        List<Product> products = Arrays.asList(product1, product2);
        when(productService.getAllProducts()).thenReturn(products);

        String viewName = productController.getAllProducts(model);

        assertEquals("products", viewName);
        verify(productService).getAllProducts();
        verify(model).addAttribute("products", products);
    }

    @Test
    void testLoadIndexProducts_Success() {
        List<Product> products = Arrays.asList(product1, product2);
        when(productService.getAllProducts()).thenReturn(products);

        String viewName = productController.loadIndexProducts(model);

        assertEquals("index", viewName);
        verify(productService).getAllProducts();
        verify(model).addAttribute("products", products);
    }

}




