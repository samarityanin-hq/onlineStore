package main.store.CustomExceptions;

import lombok.Getter;

@Getter
public class EmptyCartException extends RuntimeException{
    private final String email;
    private final String name;


    public EmptyCartException(String email, String name) {
        super(String.format("User %s, email: %s, has empty cart", name, email));
        this.email = email;
        this.name = name;
    }
}
