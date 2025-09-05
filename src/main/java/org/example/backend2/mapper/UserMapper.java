package org.example.backend2.mapper;

import org.example.backend2.dto.UserDTO;
import org.example.backend2.models.AppUser;
import org.example.backend2.models.Role;

import java.util.HashSet;
import java.util.Set;

public class UserMapper {
private UserMapper(){}


    //Converts AppUser to DTO
    //AKA doesn't bring password and only sends needed information
    public static UserDTO appUserToDto(AppUser e) {
       Set<String> roleNames = e.getRoles().stream()
                .map(Role::getName)
                .collect(java.util.stream.Collectors.toCollection(java.util.LinkedHashSet::new));

        return UserDTO.builder()
                .id(e.getId())
                .username(e.getUsername())
                .roles(roleNames)
                .build();
    }
}
