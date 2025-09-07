package org.example.backend2;

import org.example.backend2.dto.OrderRequest;
import org.example.backend2.dto.ProductDTO;
import org.example.backend2.models.AppUser;
import org.example.backend2.models.Product;
import org.example.backend2.models.Role;
import org.example.backend2.repository.RoleRepository;
import org.example.backend2.repository.UserRepository;
import org.example.backend2.service.FakeStoreProductSyncService;
import org.example.backend2.service.OrderService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootApplication
public class Backend2Application implements CommandLineRunner {

    //TODO : För att testa skriva ut produkterna i konsolen
    //TEMP to send branch

    private final FakeStoreProductSyncService fakeStoreProductSyncService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final OrderService orderService;

    public Backend2Application(FakeStoreProductSyncService fakeStoreProductSyncService, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, OrderService orderService) {
        this.fakeStoreProductSyncService = fakeStoreProductSyncService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.orderService = orderService;
    }

    public static void main(String[] args) {
        SpringApplication.run(Backend2Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        ensureRolesExist();
        ensureAdminExists();
        
        Product[] products = fakeStoreProductSyncService.getProductsFromApi();
        fakeStoreProductSyncService.syncProducts(products);
    }

    private void ensureRolesExist() {
        if (!roleRepository.existsRoleByName("USER")) {
            roleRepository.save(new Role("USER"));
        }
        if (!roleRepository.existsRoleByName("ADMIN")) {
            roleRepository.save(new Role("ADMIN"));
        }
    }

    private void ensureAdminExists() {
        if (!userRepository.existsByUsername("Hani")) {
            Role role = roleRepository.findByName("ADMIN")
                    .orElseThrow(() -> new RuntimeException("Role not found"));

            AppUser user = new AppUser("Hani", passwordEncoder.encode("Hyoju"));
            user.getRoles().add(role);
            userRepository.save(user);
        }
    }
}
