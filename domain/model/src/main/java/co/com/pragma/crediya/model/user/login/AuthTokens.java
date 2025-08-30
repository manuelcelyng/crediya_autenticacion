package co.com.pragma.crediya.model.user.login;

import java.time.Instant;

public record AuthTokens(
        String accessToken,
        Instant expiresAt,
        Long ttlMinutes
) {
}