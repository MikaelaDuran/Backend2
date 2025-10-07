package org.example.backend2.unit.controller;

import org.example.backend2.controller.UserController;
import org.example.backend2.models.Role;
import org.example.backend2.repository.RoleRepository;
import org.example.backend2.repository.UserRepository;
import org.example.backend2.service.FakeStoreProductSyncService;
import org.example.backend2.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @MockitoBean Model model;
    @MockitoBean UserRepository userRepository;
    @MockitoBean RoleRepository roleRepository;
    @MockitoBean PasswordEncoder passwordEncoder;
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
    void postLogin() throws Exception {

        when(userService.authenticateUser("user1","secret2")).thenReturn(true);

        mockMvc.perform(post("/login")
                .param("username","user1")
                .param("password","secret2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/index"));

        verify(userService).authenticateUser("user1", "secret2");
    }
}
