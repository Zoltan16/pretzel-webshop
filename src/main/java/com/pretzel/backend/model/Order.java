package com.pretzel.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String customerName;
    private String address;
    private String city;
    private String zip;
    private String phone;
    private String paymentMethod;

    private double totalAmount;
    private double originalAmount;
    private int kuponsUsed=0;
    private int kuponsEarned=0;


    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    private LocalDateTime createdAt;

    @Column(length = 50)
    private String status; // "pending", "confirmed", "shipped", etc.

    public Order() {
        this.createdAt = LocalDateTime.now();
        this.status = "pending";
        this.kuponsUsed =0;
        this.kuponsEarned=0;
    }

    // Getterek és szetterek:
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getZip() { return zip; }
    public void setZip(String zip) { this.zip = zip; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public double getOriginalAmount() { return originalAmount; }
    public void setOriginalAmount(double originalAmount) { this.originalAmount = originalAmount; }

    public int getKuponsUsed() { return kuponsUsed; }
    public void setKuponsUsed(int kuponsUsed) { this.kuponsUsed = kuponsUsed; }

    public int getKuponsEarned() { return kuponsEarned; }
    public void setKuponsEarned(int kuponsEarned) { this.kuponsEarned = kuponsEarned; }


    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }
}
