package org.example.backend2.controller;

import lombok.RequiredArgsConstructor;
import org.example.backend2.dto.UserDTO;
import org.example.backend2.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController2 {
    private final UserService userService;



    @GetMapping("/all")
    public String allUsers(Model model) {
        List<UserDTO> appUsers = userService.findAllUsersDTO(); // hämta DTO:et
        System.out.println("APPUSERS : " + appUsers);
        model.addAttribute("appUsers", appUsers);               // nyckel som vyn använder
        return "update-role";
    }
}
