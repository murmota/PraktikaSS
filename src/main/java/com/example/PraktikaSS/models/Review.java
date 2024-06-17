package com.example.PraktikaSS.models;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "Review", schema = "public", catalog = "postgres")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    @Column(name = "Content")
    private String content;
    @Column(name = "Mark")
    private int mark;
    @Column(name = "Date")
    private String date;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}