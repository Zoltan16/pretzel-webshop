package com.pretzel.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type;
    private double price;
    private String imageUrl;

    @Column(length = 1000)
    private String description;

    public Product() {}

    // Konstruktor, amelyet a DataLoader használ (5 paraméter)
    public Product(String name, String type, double price, String imageUrl, String description) {
        this.name = name;
        this.type = type;
        this.price = price;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    // Getterek és setterek

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
