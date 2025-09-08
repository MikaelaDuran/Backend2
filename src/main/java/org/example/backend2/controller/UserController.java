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

@Controller
@RequiredArgsConstructor

public class UserController {

    private final UserService userService;

   // @GetMapping("/login")
  //  public String showLogin() {
   //     return "login";
    //}

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

    //Kierans metod
    /*
    @GetMapping("/assign-role")
    public String showAssignRole(Model model) {
     //   model.addAttribute("roleUpdate", new RoleUpdate());
        return "assign-role";
    }*/




    //TODO: NU LIGGER ALLT PÅ PERMIT ALL. ANNARS KAN VI INTE TESTA
    ///all-users","/all/{id}/delete","/all/{id}/role").permitAll()
    //Hämta alla användarna till user-role.html vyn
    @GetMapping("/all")
    public String allUsers(Model model) {
        List<UserDTO> appUsers = userService.findAllUsersDTO(); // hämta DTO:et
        model.addAttribute("appUsers", appUsers);               // nyckel som vyn använder
        return "update-role";
    }

    //Delete user från vyn user-role.html
    @PostMapping("/all/{id}/delete")
    public String deleteAppUser(@PathVariable Long id, RedirectAttributes redirectAttributes, Model model) {
        try{
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("message", "Customer #" + id + " deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete customer #" + id + ": " + e.getMessage());
        }
        return "redirect:/all";
    }

    /*
    // UPDATE ROLES

    @PostMapping("/all/{id}/role")
    public String updateRole(@PathVariable Long id,  @RequestParam String role, RedirectAttributes redirectAttributes) {
        try {
            userService.updateRolesMetod(id, role);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update customer #" + id + ": " + e.getMessage());
        }
        return "redirect:/all";
    }*/

    @PostMapping("/all/{username}/role/add")
    public String assignRole(@PathVariable String username,  @RequestParam String role, RedirectAttributes redirectAttributes) {
        try {
            userService.assignRoleToUser(username, role);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to assign role to customer #" + username+ ": " + e.getMessage());
        }
        return "redirect:/all";
    }

    @PostMapping("/all/{username}/role/remove")
    public String removeRoleFromUser(@PathVariable String username,  @RequestParam String role, RedirectAttributes redirectAttributes) {
        try {
            userService.removeRoleFromUser(username, role);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to remove role from customer #" + username + ": " + e.getMessage());
        }
        return "redirect:/all";
    }
}
