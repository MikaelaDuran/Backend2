package org.example.backend2.repository;

import org.example.backend2.models.AppUser;
import org.example.backend2.models.ProductOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<ProductOrder, Long> {
    List<ProductOrder> findByAppUser(AppUser appUser);
}
