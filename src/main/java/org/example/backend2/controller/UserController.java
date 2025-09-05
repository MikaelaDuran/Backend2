package org.example.backend2.controller;

import lombok.RequiredArgsConstructor;
import org.example.backend2.dto.RegistrationRequest;
import org.example.backend2.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    @PostMapping("/register")
    public String userRegistration(RegistrationRequest registrationRequest) {
        boolean success = userService.registerUser(registrationRequest);
        if (success) {
            return "redirect:/login";
        } else {
            return "register";
        }
    }

    public String authenticateUser(String username, String password) {
        return userService.authenticateUser(username, password);
    }

    @GetMapping("/register")
    public String showRegister(Model model) {
        model.addAttribute("registrationRequest", new RegistrationRequest());
        return "register";
    }

    @GetMapping("/assign-role")
    public String showAssignRole(@RequestParam String username, @RequestParam String role) {
        return "assign-role";
    }
}
