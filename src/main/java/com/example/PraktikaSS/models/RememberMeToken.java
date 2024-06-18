package com.example.PraktikaSS.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "remember_me_tokens", schema = "public", catalog = "PraktikaSSDB")
public class RememberMeToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String series;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime lastUsed;
}
