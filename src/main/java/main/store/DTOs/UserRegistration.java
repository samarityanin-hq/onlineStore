package main.store.DTOs;

public record UserRegistration(String name,
                               String email,
                               char[] password) {
}
