package co.com.pragma.crediya.model.user;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Rol {
    private Long idRol;
    private String nombre;
    private String descripcion;
}
