package com.example.PraktikaSS.models;
import jakarta.persistence.*;
import lombok.Data;
@Entity
@Data
@Table(name = "Student", schema = "public", catalog = "PraktikaSSDB")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "UserName")
    private String userName;
    @Column(name = "UserSurName")
    private String userSurName;
    @Column(name = "UserMiddleName")
    private String userMiddleName;
    @Column(name = "ClassName")
    private String className;

}