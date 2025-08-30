package co.com.pragma.crediya.model.user;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter

public class User {
    // getters

    private final Long idNumber;
    private final String nombre;
    private final String apellido;
    private final LocalDate fechaNacimiento;
    private final String direccion;
    private final String telefono;
    private final Email correoElectronico;
    private final Salary salarioBase;
    private final String documentoIdentidad;
    private final Long rolId;
    private final String password;

    private User(Long idNumber, String nombre, String apellido, LocalDate fechaNacimiento,
                 String direccion, String telefono, Email correoElectronico, Salary salarioBase,
                 String documentoIdentidad, Long rolId, String password) {



        // Invariantes de negocio (no depender de anotaciones aquí)
        if (nombre == null || nombre.isBlank()) throw new IllegalArgumentException("nombre obligatorio");
        if (apellido == null || apellido.isBlank()) throw new IllegalArgumentException("apellido obligatorio");
        if (fechaNacimiento == null) throw new IllegalArgumentException("fecha_nacimiento obligatorio");
        if (documentoIdentidad == null || documentoIdentidad.isBlank())
            throw new IllegalArgumentException("documento_identidad obligatorio");
        if(password == null || password.isBlank()) throw new IllegalArgumentException("Contraseña obligatoria");

        this.idNumber = idNumber;
        this.nombre = nombre.trim();
        this.apellido = apellido.trim();
        this.fechaNacimiento = fechaNacimiento;
        this.direccion = direccion == null ? null : direccion.trim();
        this.telefono = telefono == null ? null : telefono.trim();
        this.correoElectronico = correoElectronico;
        this.salarioBase = salarioBase;
        this.documentoIdentidad = documentoIdentidad.trim();
        this.rolId = rolId;
        this.password = password;
    }


    public static User create(String nombre, String apellido, LocalDate fechaNacimiento,
                              String direccion, String telefono, Email correo, Salary salario,
                              String documentoIdentidad, Long rolId, String password) {
        return new User(null, nombre, apellido, fechaNacimiento, direccion, telefono, correo, salario, documentoIdentidad, rolId, password );
    }


    public User withId(Long idNumber) { return new User(idNumber, nombre, apellido, fechaNacimiento, direccion, telefono, correoElectronico, salarioBase, documentoIdentidad, rolId, password ); }

}
