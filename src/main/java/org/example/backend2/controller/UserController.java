package org.example.backend2.controller;

import lombok.RequiredArgsConstructor;
import org.example.backend2.dto.RegistrationRequest;
import org.example.backend2.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    
    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    @PostMapping("/register")
    public String userRegistration(RegistrationRequest registrationRequest){
        return userService.registerUser(registrationRequest);
        }
        
    public String authenticateUser(String username, String password) {
        return userService.authenticateUser(username, password);
    }

}
