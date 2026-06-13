package main.store.DTOs;

public record UserAuth(String email,
                       char[] password){}
