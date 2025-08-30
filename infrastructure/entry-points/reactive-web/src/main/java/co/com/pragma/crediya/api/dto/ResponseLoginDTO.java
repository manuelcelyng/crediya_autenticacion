package co.com.pragma.crediya.api.dto;

public record ResponseLoginDTO(
        String accessToken,
        String tokenType,
        Long expiresInMinutes
) {
}
