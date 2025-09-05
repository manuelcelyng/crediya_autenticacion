package co.com.pragma.crediya.r2dbc.mappers;

import co.com.pragma.crediya.model.user.Email;
import co.com.pragma.crediya.model.user.Salary;
import co.com.pragma.crediya.model.user.User;
import co.com.pragma.crediya.r2dbc.entities.UserEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserEntityMapperTest {

    private final UserEntityMapper mapper = new UserEntityMapper() {};

    @Test
    void toEntity_shouldMapAllFields() {
        User domain = User.create(
                "Mario", "Lopez", LocalDate.of(2000, 2, 2),
                "Av 10", "3000000001", new Email("mario@example.com"), new Salary(new BigDecimal("123456")),
                "DOC-1", 5L, "plainPass"
        ).withId(77L);

        org.springframework.security.crypto.password.PasswordEncoder pe = new org.springframework.security.crypto.password.PasswordEncoder() {
            @Override public String encode(CharSequence rawPassword) { return "ENC(" + rawPassword + ")"; }
            @Override public boolean matches(CharSequence rawPassword, String encodedPassword) { return encodedPassword.equals(encode(rawPassword)); }
        };

        UserEntity e = mapper.toEntity(domain, pe);
        assertNotNull(e);
        assertEquals(77L, e.getIdUsuario());
        assertEquals("Mario", e.getNombre());
        assertEquals("Lopez", e.getApellido());
        assertEquals(LocalDate.of(2000,2,2), e.getFechaNacimiento());
        assertEquals("Av 10", e.getDireccion());
        assertEquals("3000000001", e.getTelefono());
        assertEquals("mario@example.com", e.getCorreoElectronico());
        assertEquals(new BigDecimal("123456"), e.getSalarioBase());
        assertEquals("DOC-1", e.getDocumentoIdentidad());
        assertEquals(5L, e.getRolId());
    }

    @Test
    void toDomain_shouldMapAllFieldsAndId() {
        UserEntity entity = UserEntity.builder()
                .idUsuario(88L)
                .nombre("Laura")
                .apellido("Perez")
                .fechaNacimiento(LocalDate.of(1999, 9, 9))
                .direccion("Calle 9")
                .telefono("3111111111")
                .correoElectronico("laura@example.com")
                .salarioBase(new BigDecimal("999999"))
                .documentoIdentidad("CC-XYZ")
                .rolId(7L)
                .password("ENC(secret)")
                .build();

        User u = mapper.toDomain(entity);
        assertNotNull(u);
        assertEquals(88L, u.getIdNumber());
        assertEquals("Laura", u.getNombre());
        assertEquals("Perez", u.getApellido());
        assertEquals(LocalDate.of(1999,9,9), u.getFechaNacimiento());
        assertEquals("Calle 9", u.getDireccion());
        assertEquals("3111111111", u.getTelefono());
        assertEquals("laura@example.com", u.getCorreoElectronico().email());
        assertEquals(new BigDecimal("999999"), u.getSalarioBase().cantidad());
        assertEquals("CC-XYZ", u.getDocumentoIdentidad());
        assertEquals(7L, u.getRolId());
    }

    @Test
    void toDomain_shouldHandleNullId() {
        UserEntity entity = UserEntity.builder()
                .idUsuario(null)
                .nombre("Nora")
                .apellido("Diaz")
                .fechaNacimiento(LocalDate.of(1995, 1, 1))
                .documentoIdentidad("CC-1")
                .build();

        User u = mapper.toDomain(entity);
        assertNull(u.getIdNumber());
    }
}