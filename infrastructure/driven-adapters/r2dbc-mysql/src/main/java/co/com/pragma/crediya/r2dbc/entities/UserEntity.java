package co.com.pragma.crediya.r2dbc.entities;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Table(name = "usuarios")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserEntity {
    @Id
    @Column("id_usuario")
    private Long idUsuario;

    private String nombre;

    private String apellido;

    @Column("fecha_nacimiento")
    private LocalDate fechaNacimiento;

    private String direccion;

    private String telefono;

    @Column("correo_electronico")
    private String correoElectronico;

    @Column("salario_base")
    private BigDecimal salarioBase;

    @Column("documento_identidad")
    private String documentoIdentidad;

    @Column("rol_id")
    private Long rolId;

    @Column("password")
    private String password;
}
