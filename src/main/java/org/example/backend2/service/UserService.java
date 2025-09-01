package org.example.backend2.service;

import lombok.RequiredArgsConstructor;
import org.example.backend2.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService{

    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    @Bean
    public UserDetailsService userDetailsService(){
        return username -> userRepository.findByUsername(username)
                .map(user -> User.builder()
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .roles(user.getRole().replace("ROLE_","")) //Spring Security lägger automatiskt till "ROLE_" prefixet
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
