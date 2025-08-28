package co.com.pragma.crediya.api.mapper;

import co.com.pragma.crediya.api.dto.ResponseUserDTO;
import co.com.pragma.crediya.model.user.Email;
import co.com.pragma.crediya.model.user.Salary;
import co.com.pragma.crediya.model.user.User;
import co.com.pragma.crediya.shared.mappers.DateMapperImpl;
import co.com.pragma.crediya.shared.mappers.EmailMapperImpl;
import co.com.pragma.crediya.shared.mappers.SalaryMapperImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {
        UserDtoMapperImpl.class,
        EmailMapperImpl.class,
        SalaryMapperImpl.class,
        DateMapperImpl.class
})
class UserDtoMapperSpringTest {

    @Autowired
    private UserDtoMapperImpl mapper;

    @Test
    void toResponse_shouldMapAllFields_withSpringContext() {
        User domain = User.create(
                "Ana","Gomez", LocalDate.parse("1985-05-20"),
                "Cra 7 # 1-2","3110000000", new Email("ana.gomez@example.com"), new Salary(new BigDecimal("2500000")),
                "CC999", new BigDecimal("3")
        ).withId(10L);

        ResponseUserDTO response = mapper.toResponse(domain);
        assertNotNull(response);
        assertEquals(10L, response.idNumber());
        assertEquals("Ana", response.nombre());
        assertEquals("Gomez", response.apellido());
        assertEquals("1985-05-20", response.fechaNacimiento());
        assertEquals("Cra 7 # 1-2", response.direccion());
        assertEquals("3110000000", response.telefono());
        assertEquals("ana.gomez@example.com", response.correoElectronico());
        assertEquals(new BigDecimal("2500000"), response.salarioBase());
        assertEquals("CC999", response.documentoIdentidad());
        assertEquals(new BigDecimal("3"), response.rolId());
    }
}