package co.com.pragma.crediya.shared.mappers;

import co.com.pragma.crediya.model.user.Email;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailMapperTest {

    private final EmailMapper mapper = new EmailMapper() {};

    @Test
    void toEmail_shouldCreateDomainAndNormalize() {
        Email email = mapper.toEmail("TEST@Example.COM");
        assertNotNull(email);
        assertEquals("test@example.com", email.email());
    }

    @Test
    void fromEmail_shouldReturnStringOrNull() {
        assertNull(mapper.fromEmail(null));
        assertEquals("john@doe.com", mapper.fromEmail(new Email("john@doe.com")));
    }
}