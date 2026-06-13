package main.store.Entities;

import main.store.DTOs.UserRegistration;
import jakarta.persistence.*;
import main.store.Repositories.UserRole;

@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true ,name = "name")
    private String name;

    @Column(unique = true,name = "email")
    private String email;

    @Column(name = "password_hash")
    private char[] passwordHash;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    public User(){}
    public User(UserRegistration newUser){
        name = newUser.name();
        email = newUser.email();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public char[] getPasswordHash() {
        return passwordHash;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPasswordHash(char[] passwordHash) {
        this.passwordHash = passwordHash;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
