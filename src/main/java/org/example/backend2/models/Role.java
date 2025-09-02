package org.example.backend2.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

import java.util.List;

@Entity
public class Role {

    @Id
    @GeneratedValue
    private Long id;
    private String roleName;

    @ManyToMany
    private List<AppUser> appUsers;

}
