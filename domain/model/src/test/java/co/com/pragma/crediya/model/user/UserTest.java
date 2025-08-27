package co.com.pragma.crediya.model.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Nested
    class Invariants {
        @Test
        @DisplayName("Debe requerir nombre, apellido, fechaNacimiento y documentoIdentidad")
        void shouldRequireMandatoryFields() {
            IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class,
                    () -> User.create(null, "Perez", LocalDate.now(), "dir", "tel", new Email("a@b.com"), new Salary(new BigDecimal("1")), "CC1", BigDecimal.ONE));
            assertTrue(ex1.getMessage().contains("nombre obligatorio"));

            IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class,
                    () -> User.create("Juan", "", LocalDate.now(), "dir", "tel", new Email("a@b.com"), new Salary(new BigDecimal("1")), "CC1", BigDecimal.ONE));
            assertTrue(ex2.getMessage().contains("apellido obligatorio"));

            IllegalArgumentException ex3 = assertThrows(IllegalArgumentException.class,
                    () -> User.create("Juan", "Perez", null, "dir", "tel", new Email("a@b.com"), new Salary(new BigDecimal("1")), "CC1", BigDecimal.ONE));
            assertTrue(ex3.getMessage().contains("fecha_nacimiento obligatorio"));

            IllegalArgumentException ex4 = assertThrows(IllegalArgumentException.class,
                    () -> User.create("Juan", "Perez", LocalDate.now(), "dir", "tel", new Email("a@b.com"), new Salary(new BigDecimal("1")), " ", BigDecimal.ONE));
            assertTrue(ex4.getMessage().contains("documento_identidad obligatorio"));
        }

        @Test
        @DisplayName("Debe recortar cadenas y permitir withId")
        void shouldTrimAndWithId() {
            User u = User.create("  Juan  ", " Perez ", LocalDate.parse("1990-01-01"),
                    "  Calle 1  ", " 3000 ", new Email("USER@MAIL.COM"), new Salary(new BigDecimal("1000")),
                    "  CC123  ", BigDecimal.valueOf(1));

            assertEquals("Juan", u.getNombre());
            assertEquals("Perez", u.getApellido());
            assertEquals("Calle 1", u.getDireccion());
            assertEquals("3000", u.getTelefono());
            assertEquals("cc123".toUpperCase(), u.getDocumentoIdentidad()); // Not uppercasing in code, but trimmed
            assertEquals("user@mail.com", u.getCorreoElectronico().email());

            User withId = u.withId(42L);
            assertNull(u.getIdNumber());
            assertEquals(42L, withId.getIdNumber());
            assertEquals(u.getNombre(), withId.getNombre());
        }
    }
}
