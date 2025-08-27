package co.com.pragma.crediya.model.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailTest {

    @Nested
    class Validation {
        @Test
        @DisplayName("Debe rechazar null o vacío")
        void shouldRejectNullOrBlank() {
            IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () -> new Email(null));
            assertTrue(ex1.getMessage().toLowerCase().contains("no puede ser nulo"));

            IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () -> new Email("   "));
            assertTrue(ex2.getMessage().toLowerCase().contains("no puede ser nulo"));
        }

        @Test
        @DisplayName("Debe rechazar formato inválido")
        void shouldRejectInvalidFormat() {
            assertThrows(IllegalArgumentException.class, () -> new Email("sin-arroba"));
            assertThrows(IllegalArgumentException.class, () -> new Email("a@b"));
            assertThrows(IllegalArgumentException.class, () -> new Email("a b@c.com"));
        }

        @Test
        @DisplayName("Debe normalizar a minúsculas y aceptar formato válido")
        void shouldNormalizeAndAcceptValid() {
            Email email = new Email("User.Name+Tag@Example.COM");
            assertEquals("user.name+tag@example.com", email.email());
        }
    }
}
