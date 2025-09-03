package org.example.backend2.service;

import lombok.RequiredArgsConstructor;
import org.example.backend2.dto.RegistrationRequest;
import org.example.backend2.models.AppUser;
import org.example.backend2.models.Role;
import org.example.backend2.repository.RoleRepository;
import org.example.backend2.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private PasswordEncoder passwordEncoder;

    //update role
    public void assignRoleToUser(String username, String roleName) {
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        user.getRoles().add(role);
        userRepository.save(user);
    }

    // register new user.
    public String registerUser(RegistrationRequest request) {
        //TODO: Kontrollera att username inte redan finns i DB?
        if(userRepository.findByUsername(request.getUsername()).isPresent()){
            return "Username already exists";
        }

        Role role = roleRepository.findByName(request.getRole())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        AppUser user = AppUser.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        user.getRoles().add(role);

        try {
            userRepository.save(user);
            return "Success";
        } catch (Exception e) {
            return "Registration failed";
        }

    }

    // verify log in
    public String authenticateUser(String username, String password) {
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            return "Username or password is not correct";
        }
        return "Login successful";
    }
}
