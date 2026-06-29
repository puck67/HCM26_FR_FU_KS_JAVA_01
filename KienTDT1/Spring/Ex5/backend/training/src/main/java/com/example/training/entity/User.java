package com.example.training.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;
    private String fullName;
    private String email;
    private String status;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;
}