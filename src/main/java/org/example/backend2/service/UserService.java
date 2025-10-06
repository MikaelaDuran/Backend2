package org.example.backend2.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend2.dto.RegistrationRequest;
import org.example.backend2.dto.UserDTO;
import org.example.backend2.exceptions.*;
import org.example.backend2.mapper.UserMapper;
import org.example.backend2.models.AppUser;
import org.example.backend2.models.Role;
import org.example.backend2.repository.RoleRepository;
import org.example.backend2.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    private static final String DEFAULT_ROLE = "USER";

    public void assignRoleToUser(String username, String roleName) {
        AppUser user = findUser(username);
        Role role = findRole(roleName);
        if (!user.getRoles().contains(role)) {
            user.getRoles().add(role);
            userRepository.save(user);
        }
    }

    @Transactional
    public void removeRoleFromUser(String username, String roleName) {
        AppUser user = findUser(username);
        Role role = findRole(roleName);

        //If trying to remove admin role
        if("ADMIN".equalsIgnoreCase(role.getName())) {
            long adminCount = userRepository.countByRoles_Name("ADMIN");
            if(adminCount <= 1) {
                throw new CannotRemoveLastAdminException("Can not remove admin because its the last admin."
                );
            }

        }

        if (user.getRoles().remove(role)) {
            userRepository.save(user);
        }

        if(user.getRoles().isEmpty()) {
            Role defaultRole = roleRepository.findByName("USER")
                    .orElseThrow(() -> new RoleNotFoundException("Role not found: USER"));
            user.getRoles().add(defaultRole);
            userRepository.save(user);
        }
    }


    public AppUser findUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public Role findRole(String roleName) {
        return roleRepository.findByName(roleName)
                .orElseThrow(() -> new RoleNotFoundException("Role not found"));
    }
    
    
    public void registerUser(RegistrationRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new UsernameAlreadyExistsException("Username already taken");
        }

        Role role = findRole(DEFAULT_ROLE);
        AppUser user = AppUser.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Set.of(role))
                .build();

        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            log.error("Failed to save user: {}", e.getMessage());
            throw new RegistrationException("Registration failed", e);
        }
    }
    
    public boolean authenticateUser(String username, String password) {
        AppUser user = findUser(username);

        return passwordEncoder.matches(password, user.getPassword());
    }

    public List<AppUser> findAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(Long id) {
        AppUser user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        //Control if its last admin
        boolean isUserAdmin = user.getRoles().stream()
                .anyMatch(r -> "ADMIN".equalsIgnoreCase(r.getName()));
        if (isUserAdmin) {
            long adminCount = userRepository.countByRoles_Name("ADMIN");
            if(adminCount <= 1) {
                throw new RuntimeException("Can not remove admin because its the last admin.");
            }
        }

        userRepository.delete(user);
    }



    public List<UserDTO> findAllUsersDTO() {
        return userRepository.findAll().stream().map(userMapper::UserToDtoWithRole).toList();
    }
}
