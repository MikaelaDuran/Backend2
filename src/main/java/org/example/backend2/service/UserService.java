package org.example.backend2.service;

import lombok.RequiredArgsConstructor;
import org.example.backend2.dto.RegistrationRequest;
import org.example.backend2.dto.RoleUpdate;
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
    private final PasswordEncoder passwordEncoder;

    //update role
    public void assignRoleToUser(RoleUpdate roleUpdate) {
        AppUser user = userRepository.findByUsername(roleUpdate.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Role role = roleRepository.findByName(roleUpdate.getRole())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        if (roleUpdate.isShouldAssign()) {
            user.getRoles().add(role);
        } else {
            user.getRoles().remove(role);
        }
        userRepository.save(user);
    }
    

    // register new user.
    public boolean registerUser(RegistrationRequest request) {
        if(userRepository.findByUsername(request.getUsername()).isPresent()){
            return false;
        }

        Role role = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Role not found"));

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
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            return "Username or password is not correct";
        }
        return "Login successful";
    }
}
