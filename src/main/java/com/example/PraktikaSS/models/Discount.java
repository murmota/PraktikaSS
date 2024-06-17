package com.example.PraktikaSS.models;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name ="Discount", schema = "public", catalog = "postgres")
public class    Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "percent")
    private int percent;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}