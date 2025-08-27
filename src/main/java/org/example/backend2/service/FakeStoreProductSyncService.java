package org.example.backend2.service;

import lombok.RequiredArgsConstructor;
import org.example.backend2.models.Product;
import org.example.backend2.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FakeStoreProductSyncService {

    private final ProductRepository productRepository;

    public void syncProductsFromApi() {

        RestTemplate restTemplate = new RestTemplate();
        String url = "https://fakestoreapi.com/products";
        Product[] apiProducts = restTemplate.getForObject(url, Product[].class);

        if (apiProducts == null) {
            System.out.println(("API returned null response"));
            return;
        }
        syncProducts(apiProducts);
    }

    private void syncProducts(Product[] apiProducts) {
        Map<Long, Product> existingProducts = productRepository.findAll()
                .stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        Set<Long> apiProductIds = new HashSet<>();
        List<Product> productsToSave = new ArrayList<>();

        for (Product apiProduct : apiProducts) {
            apiProductIds.add(apiProduct.getId());
            productsToSave.add(apiProduct);
        }

        productRepository.saveAll(productsToSave);

        existingProducts.keySet().removeAll(apiProductIds);
        if (!existingProducts.isEmpty()) {
            productRepository.deleteAllById(existingProducts.keySet());
        }
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }
}