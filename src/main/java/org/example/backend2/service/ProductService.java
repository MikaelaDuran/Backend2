package org.example.backend2.service;

import lombok.RequiredArgsConstructor;
import org.example.backend2.exceptions.ProductNotFoundException;
import org.example.backend2.models.Product;
import org.example.backend2.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> getProductsByCategory(String category) {
        return productRepository.findAllByCategory(category);
    }

    public Product getProductById(long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(String.format("Product not found with id %d", id)));
    }
    
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}
