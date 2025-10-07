package org.example.backend2.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

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
                .requestMatchers("/","/login", "/register",
                        "/css/**","/images/**","/js/**",
                        "/products","/products/**").permitAll()

                .requestMatchers("/user",
                        "/order-confirmation", "/order-form").hasAuthority("USER")

                .requestMatchers("/admin",
                        "/all","/all/{id}/delete",
                        "/all/*/role/add","/all/*/role/remove",
                        "/orders").hasAuthority("ADMIN")

                .anyRequest().authenticated()
        );

        http.formLogin((auth) -> auth.loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
        );

        http.logout((auth) -> auth.logoutUrl("/logout")
                .logoutSuccessUrl("/")
        );

        http.exceptionHandling(exception -> exception
                        .accessDeniedHandler((request,
                                              response,
                                              accessDeniedException) ->
                                response.sendRedirect("/"))  // redirect istället för 403
                );

        return http.build();
    }
    
}
