package org.example.backend2;

import org.example.backend2.service.ApiRequest;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Backend2Application /*implements CommandLineRunner*/ {

    //TODO 1 av 2: För att testa skriva ut produkterna i konsolen
    /*
    private final ApiRequest apiRequest;

    public Backend2Application(ApiRequest apiRequest) {
        this.apiRequest = apiRequest;
    }*/

    public static void main(String[] args) {
        SpringApplication.run(Backend2Application.class, args);
    }

    /*TODO 2 av 2: För att testa skriva ut produkterna i konsolen
    @Override
    public void run(String... args) throws Exception {
        apiRequest.getProducts();
    }*/

}
