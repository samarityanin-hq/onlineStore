package main.store.DTO.Response;


public record JwtAuthentication(
        String accessToken,
        String refreshToken
) {
}
