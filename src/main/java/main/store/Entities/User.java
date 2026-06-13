package Entities;

import DTOs.UserRegistration;
import jakarta.persistence.*;

@Entity()
@Table(name = "Users")
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "password_hash")
    private char[] passwordHash;

    public User(){}
    public User(UserRegistration newUser){
        name = newUser.name();
        email = newUser.email();
        passwordHash = newUser.password();
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

    public void setId(long id) {
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
}
