package main.store.DTO.Response;


public record JwtAuthentication(
        String token,
        String refreshToken
) {
}
