package org.example.backend2;

import org.example.backend2.service.FakeStoreApiRequestService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Backend2Application implements CommandLineRunner {

    //TODO : För att testa skriva ut produkterna i konsolen

    private final FakeStoreApiRequestService fakeStoreApiRequestService;

    public Backend2Application(FakeStoreApiRequestService fakeStoreApiRequestService) {
        this.fakeStoreApiRequestService = fakeStoreApiRequestService;
    }


    public static void main(String[] args) {
        SpringApplication.run(Backend2Application.class, args);


        }


    //TODO : För att testa skriva ut produkterna i konsolen
    @Override
    public void run(String... args) throws Exception {
        fakeStoreApiRequestService.getProductsRestTemplate();
    }

}
