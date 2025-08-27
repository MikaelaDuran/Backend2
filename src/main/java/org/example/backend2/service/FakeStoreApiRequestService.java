package org.example.backend2.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.backend2.models.Product;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class FakeStoreApiRequestService {

    public Product[] getProducts() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        URL url = new URL("https://fakestoreapi.com/products");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.connect();

        //InputStreamReader reads bytes and decodes them into text
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            //All the JSON data will be stored in this StringBuilder (en sträng
            StringBuilder jsonResponse = new StringBuilder();

            //A variable to hold one line of the JSON data
            String line;

            //in.readLine reads one line at a time
            while ((line = in.readLine()) != null) {
                jsonResponse.append(line);
            }

            Product[] products = mapper.readValue(jsonResponse.toString(), Product[].class);

            return products;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
