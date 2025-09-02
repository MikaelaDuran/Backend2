package org.example.backend2;

import org.example.backend2.models.Product;
import org.example.backend2.service.FakeStoreProductSyncService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Backend2Application implements CommandLineRunner {

    //TODO : För att testa skriva ut produkterna i konsolen
    //TEMP to send branch

    private final FakeStoreProductSyncService fakeStoreProductSyncService;

    public Backend2Application(FakeStoreProductSyncService fakeStoreProductSyncService) {
        this.fakeStoreProductSyncService = fakeStoreProductSyncService;
    }


    public static void main(String[] args) {
        SpringApplication.run(Backend2Application.class, args);


        }


    //TODO : För att testa skriva ut produkterna i konsolen
    @Override
    public void run(String... args) throws Exception {
        Product[] products = fakeStoreProductSyncService.syncProductsFromApi();
        fakeStoreProductSyncService.syncProducts(products);
    }

}
