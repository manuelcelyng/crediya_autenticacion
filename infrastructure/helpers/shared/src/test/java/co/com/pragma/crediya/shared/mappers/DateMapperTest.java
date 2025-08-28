package co.com.pragma.crediya.shared.mappers;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DateMapperTest {

    private final DateMapper mapper = new DateMapper() {};

    @Test
    void toLocalDate_shouldParseIsoString() {
        LocalDate d = mapper.toLocalDate("1990-01-02");
        assertEquals(LocalDate.of(1990, 1, 2), d);
    }

    @Test
    void fromLocalDate_shouldFormatAndHandleNull() {
        assertNull(mapper.fromLocalDate(null));
        assertEquals("2024-12-31", mapper.fromLocalDate(LocalDate.of(2024, 12, 31)));
    }
}