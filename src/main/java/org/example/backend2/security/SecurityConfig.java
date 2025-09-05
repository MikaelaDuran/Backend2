package org.example.backend2.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf((crsf -> crsf.disable()));
        http.authorizeHttpRequests((auth) -> auth
                .requestMatchers("/login", "/register","/all","/all-users").permitAll()
                .requestMatchers("/user").hasRole("USER")
                .requestMatchers("/admin").hasRole("ADMIN")
                .anyRequest().authenticated()
        );

        http.formLogin((auth) -> auth.loginPage("/login")
                .loginProcessingUrl("/loginProc")
                //.defaultSuccessUrl("/welcomePage")
                .permitAll()
        );

        http.logout((auth) -> auth.logoutUrl("/logout")
                .logoutSuccessUrl("/"));

        return http.build();
    }
    
}
