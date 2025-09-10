package org.example.backend2.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend2.models.Product;
import org.example.backend2.repository.ProductRepository;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FakeStoreProductSyncService {

    private final ProductRepository productRepository;
    public static String FAKE_STORE_API_URL = "https://fakestoreapi.com/products";
    
    public Product[] getProductsFromApi() {

        RestTemplate restTemplate = new RestTemplate();
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(5000);
        restTemplate.setRequestFactory(factory);

        try {
            Product[] apiProducts = restTemplate.getForObject(FAKE_STORE_API_URL, Product[].class);
            return apiProducts != null ? apiProducts : new Product[0];
        } catch (RestClientException e) {
            log.error("Failed to fetch products from API", e);
            return new Product[0];
        }
    }

    @Transactional
    public void syncProducts(Product[] apiProducts) {
        Map<Long, Product> existingProducts = productRepository.findAll()
                .stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        Set<Long> apiProductIds = new HashSet<>();
        List<Product> productsToSave = new ArrayList<>();

        for (Product apiProduct : apiProducts) {
            apiProductIds.add(apiProduct.getId());

            Product existing = existingProducts.get(apiProduct.getId());
            if (existing != null) {
                existing.setTitle(apiProduct.getTitle());
                existing.setPrice(apiProduct.getPrice());
                existing.setDescription(apiProduct.getDescription());
                existing.setCategory(apiProduct.getCategory());
                existing.setImage(apiProduct.getImage());
                existing.setRating(apiProduct.getRating());
                productsToSave.add(existing);
            } else {
                productsToSave.add(apiProduct);
            }
        }

        productRepository.saveAll(productsToSave);

        existingProducts.keySet().removeAll(apiProductIds);
        if (!existingProducts.isEmpty()) {
            if (apiProducts.length == 0) {
                log.warn("API returned no products, skipping deletion of {} existing products", existingProducts.size());
                return;
            }
            log.info("Removing {} old products", existingProducts.size());
            productRepository.deleteAllById(existingProducts.keySet());
        }
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }
}