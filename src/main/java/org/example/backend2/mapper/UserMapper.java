package org.example.backend2.mapper;

import org.example.backend2.dto.UserDTO;
import org.example.backend2.models.AppUser;

import java.util.List;
import java.util.stream.Collectors;

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
}
