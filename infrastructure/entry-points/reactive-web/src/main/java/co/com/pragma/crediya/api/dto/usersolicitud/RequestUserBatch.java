package co.com.pragma.crediya.api.dto.usersolicitud;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;


// Solicitamos un batch con todos los correos de la "Pagina"
public record RequestUserBatch (
        @NotEmpty(message = "emails no puede estar vacio")
        List<String> emails
){
}
