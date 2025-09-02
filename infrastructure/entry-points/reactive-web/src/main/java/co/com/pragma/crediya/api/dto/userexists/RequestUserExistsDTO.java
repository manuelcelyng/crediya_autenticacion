package co.com.pragma.crediya.api.dto.userexists;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record RequestUserExistsDTO(
        @Email
        @NotBlank
        String email,

        @NotBlank
        String documentoIdentidad

) {
}
