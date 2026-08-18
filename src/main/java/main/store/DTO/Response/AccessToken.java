package main.store.DTO.Response;

import org.springframework.beans.factory.annotation.Value;

public record AccessToken(
        String token
) {
}
