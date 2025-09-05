package org.example.backend2.controller;

import lombok.RequiredArgsConstructor;
import org.example.backend2.dto.RegistrationRequest;
//import org.example.backend2.dto.RoleUpdate;
import org.example.backend2.dto.UserDTO;
import org.example.backend2.models.AppUser;
import org.example.backend2.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RestController
@RequestMapping("app-users")
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

    /*
    @GetMapping("/assign-role")
    public String showAssignRole(Model model) {
     //   model.addAttribute("roleUpdate", new RoleUpdate());
        return "assign-role";
    }*/


    @GetMapping("/get-all-users")
    public List<UserDTO> getAllUsers() {
        return userService.findAllUsersDTO();

    }



    @PostMapping("/all-users/{id}/delete")
    public String deleteAppUser(@PathVariable Long id, RedirectAttributes redirectAttributes,Model model) {
        try{
        userService.deleteUser(id);
        redirectAttributes.addFlashAttribute("message", "Customer #" + id + " deleted successfully");
        } catch (Exception e) {
        redirectAttributes.addFlashAttribute("error", "Failed to delete customer #" + id + ": " + e.getMessage());
    }
        return "redirect:/update-role";
    }

    /*
    @PostMapping("/all-users/{id}/role")
    public String updateRole(@PathVariable Long id, @ModelAttribute RoleUpdate roleUpdate, RedirectAttributes redirectAttributes) {
        try{
            userService.updateUserRole(id, roleUpdate);
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("error", "Failed to update customer #" + id + ": " + e.getMessage());
        }return "update-role";
    }*/
}
