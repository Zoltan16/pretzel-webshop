package com.pretzel.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    private String password; // Most csak sima string, majd lesz titkosítva

    private boolean isGuest;

    private LocalDateTime createdAt;

    private int kupons;

    public User() {

        this.createdAt = LocalDateTime.now();
        this.kupons = 10;
    }

    public User(String email, String password, boolean isGuest) {
        this.email = email;
        this.password = password;
        this.isGuest = isGuest;
        this.createdAt = LocalDateTime.now();
        this.kupons = isGuest ? 0 : 10;
    }

    // getter/ setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public boolean isGuest() { return isGuest; }
    public void setGuest(boolean guest) { isGuest = guest; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public int getKupons() {return kupons; }
    public void setKupons(int kupons) {this.kupons = kupons;}

    public void addKupons(int amount){
        this.kupons += amount;
    }
    public boolean useKupons(int amount)
    {
        if(this.kupons >= amount){
            this.kupons -= amount;
            return true;
        }
        return false;
    }

}
