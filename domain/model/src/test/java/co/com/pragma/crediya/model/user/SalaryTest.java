package co.com.pragma.crediya.model.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class SalaryTest {

    @Nested
    class Validation {
        @Test
        @DisplayName("Debe rechazar null")
        void shouldRejectNull() {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new Salary(null));
            assertTrue(ex.getMessage().toLowerCase().contains("no puede ser nulo"));
        }

        @Test
        @DisplayName("Debe rechazar valores negativos y mayores al máximo")
        void shouldRejectOutOfRange() {
            assertThrows(IllegalArgumentException.class, () -> new Salary(new BigDecimal("-0.01")));
            assertThrows(IllegalArgumentException.class, () -> new Salary(new BigDecimal("15000000.01")));
        }

        @Test
        @DisplayName("Debe aceptar valores en el rango [0, 15000000]")
        void shouldAcceptRange() {
            assertEquals(new BigDecimal("0"), new Salary(new BigDecimal("0")).cantidad());
            assertEquals(new BigDecimal("15000000"), new Salary(new BigDecimal("15000000")).cantidad());
            assertEquals(new BigDecimal("123.45"), new Salary(new BigDecimal("123.45")).cantidad());
        }
    }
}
