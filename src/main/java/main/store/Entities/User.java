package main.store.Entities;

import lombok.Getter;
import lombok.Setter;
import main.store.DTO.DTOin.UserRegistration;
import jakarta.persistence.*;
import main.store.Enums.UserRole;

@Entity
@Getter
@Setter
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
}
