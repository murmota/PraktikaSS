package com.example.PraktikaSS.models;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "Notes", schema = "public", catalog = "PraktikaSSDB")
public class Notes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "Student_id", nullable = false)
    private Student student;
    @Column(name = "noteContent")
    private String noteContent;
}