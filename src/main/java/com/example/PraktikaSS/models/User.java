package com.example.PraktikaSS.models;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "User", schema = "public", catalog = "PraktikaSSDB")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "UserName")
    private String userName;
    @Column(name = "UserSurName")
    private String userSurName;
    @Column(name = "UserMiddleName")
    private String userMiddleName;
    @Column(name = "Email")
    private String email;
    @Column(name = "Password")
    private String password;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "User_Role", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<String> roles = new HashSet<>();
}