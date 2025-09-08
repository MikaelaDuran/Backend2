package org.example.backend2.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.backend2.dto.RegistrationRequest;
import org.example.backend2.exceptions.RoleNotFoundException;
import org.example.backend2.exceptions.UserNotFoundException;
import org.example.backend2.models.AppUser;
import org.example.backend2.models.Role;
import org.example.backend2.repository.RoleRepository;
import org.example.backend2.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String DEFAULT_ROLE = "USER";

    
    public void assignRoleToUser(String username, String roleName) {
        AppUser user = findUser(username);
        Role role = findRole(roleName);
        if (!user.getRoles().contains(role)) {
            user.getRoles().add(role);
            userRepository.save(user);
        }
    }

    public void removeRoleFromUser(String username, String roleName) {
        AppUser user = findUser(username);
        Role role = findRole(roleName);

        if (user.getRoles().remove(role)) {
            userRepository.save(user);
        }
    }

    private AppUser findUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    private Role findRole(String roleName) {
        return roleRepository.findByName(roleName)
                .orElseThrow(() -> new RoleNotFoundException("Role not found"));
    }

    // register new user.
    public boolean registerUser(RegistrationRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return false;
        }

        Role role = findRole(DEFAULT_ROLE);
        AppUser user = AppUser.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        user.getRoles().add(role);

        try {
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Registration failed");
        }
    }

    // verify log in
    public String authenticateUser(String username, String password) {
        AppUser user = findUser(username);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            return "Username or password is not correct";
        }
        return "Login successful";
    }

    public List<AppUser> findAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(Long id) {
        AppUser user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        userRepository.delete(user);
    }

    @Transactional
    public void updateRolesMetod(Long id, String role) {
        AppUser user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.getRoles().clear();

        String newInput = role.replace(" ", "").toUpperCase();

        if(newInput.contains("USER")) {
            Role userRole = roleRepository.findByName("USER")
                    .orElseThrow(() -> new RuntimeException("Role USER not found"));
            user.getRoles().add(userRole);
        }
        if(newInput.contains("ADMIN")) {
            Role adminRole = roleRepository.findByName("ADMIN")
                    .orElseThrow(() -> new RuntimeException("Role ADMIN not found"));
            user.getRoles().add(adminRole);
        }
        userRepository.save(user);
    }
}
