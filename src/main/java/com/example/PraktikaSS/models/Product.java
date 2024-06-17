package com.example.PraktikaSS.models;
import jakarta.persistence.*;
import lombok.Data;
@Entity
@Data
@Table(name = "Product", schema = "public", catalog = "postgres")
public class Product implements IProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "articul")
    private Long articul;
    @Column(name = "Name")
    private String name;
    @Column(name = "Company")
    private String company;
    @Column(name = "Price")
    private double price;
    @Column(name = "Description")
    private String description;
}