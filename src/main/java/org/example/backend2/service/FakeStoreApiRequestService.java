package org.example.backend2.service;

import org.example.backend2.models.Product;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FakeStoreApiRequestService {

    public Product[] getProductsRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://fakestoreapi.com/products";
        String response = restTemplate.getForObject(url, String.class);

        //JSON string
        //System.out.println(response);

        //Array of products
        return restTemplate.getForObject(url, Product[].class);

//        if (products != null) {
//            for (Product product : products) {
//                System.out.println(product.getTitle());
//                //REPOSITORY.SAVE(OBJECT);
//                //Spara i DB. Kontroller om produkten finns redan finns och uppdatera i så fall.
//                //Kontroller om produkten har raderats och i så fall ta bort den.
//            }
        }
    }