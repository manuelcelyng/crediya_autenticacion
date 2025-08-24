package co.com.pragma.crediya.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;

public record CreateUserDTO(
        @NotBlank
        String nombre,

        @NotBlank
        String apellido,

        @Email
        @NotBlank
        String correoElectronico,

        // ISO-8601 "YYYY-MM-DD". Si luego cambia a LocalDate, mejor. //TODO -> cambiarlo
        @NotBlank @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$",
                message = "fechaNacimiento debe tener formato YYYY-MM-DD")
        String fechaNacimiento,

        @NotBlank
        String direccion,

        @NotBlank
        String telefono,


        @NotNull
        BigDecimal salarioBase,

        @NotBlank
        String documentoIdentidad,

        @NotNull
        Long rolId
) {
}
