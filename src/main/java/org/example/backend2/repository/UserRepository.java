package org.example.backend2.repository;

import org.example.backend2.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);

    boolean existsByUsername(String username);

    // Count all users with has a role that matches roleName
    long countByRoles_Name(String roleName);
}
