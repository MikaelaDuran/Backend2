package org.example.backend2.unit.controller;

import org.example.backend2.controller.UserController;
import org.example.backend2.dto.UserDTO;
import org.example.backend2.models.Role;
import org.example.backend2.repository.RoleRepository;
import org.example.backend2.repository.UserRepository;
import org.example.backend2.service.FakeStoreProductSyncService;
import org.example.backend2.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(properties = {
        "startup.enabled=false",
        "adminpassword=test123"
})
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean UserService userService;

    @MockitoBean UserDTO userDTO;
    @MockitoBean Model model;
    @MockitoBean UserRepository userRepository;
    @MockitoBean RoleRepository roleRepository;
    @MockitoBean PasswordEncoder passwordEncoder;
    @MockitoBean CommandLineRunner commandLineRunner;
    @MockitoBean FakeStoreProductSyncService  fakeStoreProductSyncService;

    @BeforeEach
    void beforeEach() {
        Role admin = new Role();
        admin.setName("ADMIN");
        when(roleRepository.findByName("ADMIN")).thenReturn(java.util.Optional.of(admin));

        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(userRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
    }

    @Test
    void postLoginTest() throws Exception {

        when(userService.authenticateUser("user1","secret2")).thenReturn(true);

        mockMvc.perform(post("/login")
                .param("username","user1")
                .param("password","secret2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/index"));

        verify(userService).authenticateUser("user1", "secret2");
    }

    @Test
    void getAllUsersTest() throws Exception {
        List<UserDTO> allUsers = List.of(
                new UserDTO(1L, "user1", Set.of("USER")),
                new UserDTO(2L, "user2", Set.of("ADMIN"))
        );
        when(userService.findAllUsersDTO()).thenReturn(allUsers);

        mockMvc.perform(get("/all"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("appUsers"))
                .andExpect(model().attribute("appUsers", hasSize(2)))
                .andExpect(model().attribute("appUsers", contains(
                        allOf(hasProperty("username", equalTo("user1"))),
                        allOf(hasProperty("username", equalTo("user2")))
                )));
    }

    @Test
    void deleteAppUserTest() throws Exception {

        long id = 1L;

        mockMvc.perform(post("/all/{id}/delete", id))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/all"))
                .andExpect(flash().attribute("message", "User #1 has been deleted successfully"));

        verify(userService).deleteUser(id);
    }

    @Test
    void assignRoleTest() throws Exception {
        mockMvc.perform(post("/all/{username}/role/add", "user1")
                        .param("role", "ADMIN"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/all"))
                .andExpect(flash().attribute("message",
                        "User user1 has been assigned role successfully"));

        verify(userService).assignRoleToUser("user1", "ADMIN");
    }

    @Test
    void assignRoleTest_fail() throws Exception {
        doThrow(new RuntimeException("no such role"))
                .when(userService).assignRoleToUser("user1", "ADMIN");

        mockMvc.perform(post("/all/{username}/role/add", "user1").param("role", "ADMIN"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/all"))
                .andExpect(flash().attribute("error",
                        startsWith("Failed to assign role to user user1")));

        verify(userService).assignRoleToUser("user1", "ADMIN");
    }
}
