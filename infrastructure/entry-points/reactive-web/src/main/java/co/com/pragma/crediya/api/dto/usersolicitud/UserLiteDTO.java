package co.com.pragma.crediya.api.dto.usersolicitud;


// Estos son los datos extra que necesita la solicitud de obtener solicitudes :D
public record UserLiteDTO(
        String nombre,
        Long salarioBase,
        String correoElectronico

) {
}
