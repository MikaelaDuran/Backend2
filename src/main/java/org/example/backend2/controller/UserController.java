package org.example.backend2.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.backend2.dto.LoginRequest;
import org.example.backend2.dto.RegistrationRequest;
import org.example.backend2.dto.UserDTO;
import org.example.backend2.exceptions.CannotRemoveLastAdminException;
import org.example.backend2.exceptions.UsernameAlreadyExistsException;
import org.example.backend2.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor

public class UserController {

    private final UserService userService;

    @GetMapping("/login")
    public String showLogin(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }

    @PostMapping("/login")
    public String authenticateUser(LoginRequest loginRequest) {
        if (userService.authenticateUser(loginRequest.getUsername(), loginRequest.getPassword())) {
            return "redirect:/index";
        } else {
            return "/login";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/";
    }

    @PostMapping("/register")
    public String userRegistration(RegistrationRequest registrationRequest,
                                   Model model) {
        try {
            userService.registerUser(registrationRequest);
            return "redirect:/login";
        } catch (UsernameAlreadyExistsException e) {
            model.addAttribute("error", "Username already taken");
            model.addAttribute("registrationRequest", new RegistrationRequest()); 
            return "register";
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed. Please try again.");
            return "register";
        }
    }


    @GetMapping("/register")
    public String showRegister(Model model) {
        model.addAttribute("registrationRequest", new RegistrationRequest());
        return "register";
    }


    //Hämta alla användarna till user-role.html vyn
    @GetMapping("/all")
    public String allUsers(Model model) {
        List<UserDTO> appUsers = userService.findAllUsersDTO(); // hämta DTO:et
        model.addAttribute("appUsers", appUsers);               // nyckel som vyn använder
        return "update-role";
    }

    //Delete user från vyn user-role.html
    @PostMapping("/all/{id}/delete")
    public String deleteAppUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try{
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("message", "User #" + id + " has been deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete user #" + id + ": " + e.getMessage());
        }
        return "redirect:/all";
    }

    // UPDATE ROLES
    @PostMapping("/all/{username}/role/add")
    public String assignRole(@PathVariable String username,  @RequestParam String role, RedirectAttributes redirectAttributes) {
        try {
            userService.assignRoleToUser(username, role);
            redirectAttributes.addFlashAttribute("message", "User " + username + " has been assigned role successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to assign role to user " + username+ ": " + e.getMessage());
        }
        return "redirect:/all";
    }

    @PostMapping("/all/{username}/role/remove")
    public String removeRoleFromUser(@PathVariable String username,  @RequestParam String role, RedirectAttributes redirectAttributes) {
        try {
            userService.removeRoleFromUser(username, role);
            redirectAttributes.addFlashAttribute("message", "The role " + role + " has been removed from " + username +" successfully!"
                    +" If that was the user's last role, a default 'USER' role was assigned automatically.");
        } catch (CannotRemoveLastAdminException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to remove role from user " + username + " : " + e.getMessage());
        }
        return "redirect:/all";
    }


}
