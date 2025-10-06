package org.example.backend2.unit.service;

import org.example.backend2.dto.RegistrationRequest;
import org.example.backend2.exceptions.CannotRemoveLastAdminException;
import org.example.backend2.exceptions.UserNotFoundException;
import org.example.backend2.models.AppUser;
import org.example.backend2.models.Role;
import org.example.backend2.repository.RoleRepository;
import org.example.backend2.repository.UserRepository;
import org.example.backend2.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {


    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Spy
    @InjectMocks
    private UserService userService;


    @Test
    void findUserTest_whenExists() {
        AppUser user = new AppUser();
        user.setId(1L);
        user.setUsername("testuser");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        AppUser result = userService.findUser("testuser");

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(userRepository).findByUsername("testuser");
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void findUserTest_whenNotExists() {

        when(userRepository.findByUsername("missing")).thenReturn(Optional.empty());

        UserNotFoundException exception =
                assertThrows(UserNotFoundException.class, ()
                        -> userService.findUser("missing"));

        assertEquals("User not found", exception.getMessage());

        verify(userRepository).findByUsername("missing");
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void registerUserTest_save(){

        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setUsername("testuser");
        registrationRequest.setPassword("password");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        Role role = new Role();
        role.setName("USER");
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(role));
        when(passwordEncoder.encode(registrationRequest.getPassword())).thenReturn(registrationRequest.getPassword());

        ArgumentCaptor<AppUser> userCaptor = ArgumentCaptor.forClass(AppUser.class);

        userService.registerUser(registrationRequest);
        verify(userRepository).findByUsername("testuser");
        verify(roleRepository).findByName("USER");
        verify(passwordEncoder).encode("password");
        verify(userRepository).save(userCaptor.capture());

        AppUser saved = userCaptor.getValue();
        assertEquals("testuser", saved.getUsername());
        assertEquals("password", saved.getPassword());
        assertTrue(saved.getRoles().stream().anyMatch(r -> "USER".equals(r.getName())));

        verifyNoMoreInteractions(userRepository, roleRepository, passwordEncoder);
    }

    @Test
    void assignRoleTest_newRole(){
        AppUser user = new AppUser();
        user.setUsername("testuser");
        user.setRoles(new HashSet<>());

        Role role = new Role();
        role.setName("ADMIN");

        doReturn(user).when(userService).findUser("testuser");
        doReturn(role).when(userService).findRole("ADMIN");

        userService.assignRoleToUser("testuser", "ADMIN");

        verify(userService).findUser("testuser");
        verify(userService).findRole("ADMIN");

        assertTrue(user.getRoles().stream().anyMatch(r -> "ADMIN".equals(r.getName())));
        verify(userRepository).save(user);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void removeRoleTest_moreThanOneAdmin(){
        AppUser user = new AppUser();
        user.setUsername("testuser");
        user.setRoles(new HashSet<>());

        Role role = new Role();
        role.setName("ADMIN");
        user.getRoles().add(role);

        Role userRole = new Role(); userRole.setName("USER");

        doReturn(user).when(userService).findUser("testuser");
        doReturn(role).when(userService).findRole("ADMIN");

        when(userRepository.countByRoles_Name("ADMIN")).thenReturn(2L);
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(userRole));

        userService.removeRoleFromUser("testuser", "ADMIN");

        assertFalse(user.getRoles().stream().anyMatch(r -> "ADMIN".equals(r.getName())));
        assertTrue(user.getRoles().stream().anyMatch(r -> "USER".equals(r.getName())));

        verify(userRepository).countByRoles_Name("ADMIN");
        verify(userRepository, times(2)).save(user);
        verify(roleRepository).findByName("USER");
    }


    @Test
    void removeRoleTest_lastAdmin() {

        AppUser user = new AppUser();
        user.setUsername("onlyAdmin");
        user.setRoles(new HashSet<>());

        Role admin = new Role(); admin.setName("ADMIN");
        user.getRoles().add(admin);

        doReturn(user).when(userService).findUser("onlyAdmin");
        doReturn(admin).when(userService).findRole("ADMIN");

        when(userRepository.countByRoles_Name("ADMIN")).thenReturn(1L);


        CannotRemoveLastAdminException ex = assertThrows(
                CannotRemoveLastAdminException.class,
                () -> userService.removeRoleFromUser("onlyAdmin", "ADMIN")
        );
        assertTrue(ex.getMessage().toLowerCase().contains("last admin"));

        assertTrue(user.getRoles().stream().anyMatch(r -> "ADMIN".equals(r.getName())));
        verify(userRepository).countByRoles_Name("ADMIN");
        verify(userRepository, never()).save(any());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void deleteUserTest_lastAdmin() {
        AppUser lastAdmin = new AppUser();
        lastAdmin.setId(3L);
        lastAdmin.setUsername("testuser");

        Role admin = new Role();
        admin.setName("ADMIN");
        lastAdmin.setRoles(new HashSet<>(Set.of(admin)));

        when(userRepository.findById(3L)).thenReturn(Optional.of(lastAdmin));
        when(userRepository.countByRoles_Name("ADMIN")).thenReturn(1L);

        assertThrows(RuntimeException.class, () -> userService.deleteUser(3L));
        verify(userRepository, never()).delete(any());
    }
}