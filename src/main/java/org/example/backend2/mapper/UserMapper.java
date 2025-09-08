package org.example.backend2.mapper;

import org.example.backend2.dto.UserDTO;
import org.example.backend2.models.AppUser;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.example.backend2.models.Role;


public class UserMapper {
    
    public static UserDTO toDTO(AppUser user) {
        return UserDTO.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .build();
    }
    
    public static List<UserDTO> multipleToDTO(List<AppUser> appUsers) {
        return appUsers.stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }



    //Converts AppUser to DTO
    //AKA doesn't bring password and only sends needed information
    public static UserDTO UserToDtoWithRole(AppUser e) {
        Set<String> roleNames = e.getRoles().stream()
                .map(Role::getName)
                .collect(java.util.stream.Collectors.toCollection(java.util.LinkedHashSet::new));

        return UserDTO.builder()
                .userId(e.getId())
                .username(e.getUsername())
                .roles(roleNames)
                .build();
    }
}

