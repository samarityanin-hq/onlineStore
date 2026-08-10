package main.store.DTO.Request;


public record JwtAuthentication(
        String token,
        String refreshToken
) {
}
