package co.com.pragma.crediya.api.mapper;

import co.com.pragma.crediya.api.dto.CreateUserDTO;
import co.com.pragma.crediya.api.dto.ResponseUserDTO;
import co.com.pragma.crediya.model.user.Email;
import co.com.pragma.crediya.model.user.Salary;
import co.com.pragma.crediya.model.user.User;
import co.com.pragma.crediya.shared.mappers.DateMapperImpl;
import co.com.pragma.crediya.shared.mappers.EmailMapperImpl;
import co.com.pragma.crediya.shared.mappers.SalaryMapperImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


class UserDtoMapperTest {

    private final UserDtoMapper mapper = new UserDtoMapperImpl();


    @Test
    void toModel_shouldMapAllFields() {
        CreateUserDTO dto = new CreateUserDTO(
                "Juan","Perez","juan.perez@example.com","1990-01-01",
                "Calle 1","3000000000", new BigDecimal("1000000"), "CC123", 2L
        );

        User user = mapper.toModel(dto);
        assertNotNull(user);
        assertNull(user.getIdNumber());
        assertEquals("Juan", user.getNombre());
        assertEquals("Perez", user.getApellido());
        assertEquals(LocalDate.of(1990,1,1), user.getFechaNacimiento());
        assertEquals("Calle 1", user.getDireccion());
        assertEquals("3000000000", user.getTelefono());
        assertEquals("juan.perez@example.com", user.getCorreoElectronico().email());
        assertEquals(new BigDecimal("1000000"), user.getSalarioBase().cantidad());
        assertEquals("CC123", user.getDocumentoIdentidad());
        assertEquals(new BigDecimal("2"), user.getRolId());
    }

}