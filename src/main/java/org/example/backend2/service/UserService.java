package org.example.backend2.service;

import lombok.RequiredArgsConstructor;
import org.example.backend2.dto.RegistrationRequest;
import org.example.backend2.models.AppUser;
import org.example.backend2.models.Role;
import org.example.backend2.repository.RoleRepository;
import org.example.backend2.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private Role findRole(String roleName) {
        return roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found"));
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
}
