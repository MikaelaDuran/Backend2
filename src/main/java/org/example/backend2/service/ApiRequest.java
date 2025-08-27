package org.example.backend2.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.backend2.models.Product;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class ApiRequest {


    public void getProductsRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://fakestoreapi.com/products";
        String response = restTemplate.getForObject(url, String.class);

        //JSON string
        //System.out.println(response);

        //Array of products
        Product [] products = restTemplate.getForObject(url, Product[].class);
        if (products != null) {
            for (Product product : products) {
                System.out.println(product.getTitle());
                //REPOSITORY.SAVE(OBJECT);
                //Spara i DB. Kontroller om produkten finns redan finns och uppdatera i så fall.
                //Kontroller om produkten har raderats och i så fall ta bort den.
            }
        }
    }

}
