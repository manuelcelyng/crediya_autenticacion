package co.com.pragma.crediya.shared.mappers;

import co.com.pragma.crediya.model.user.Salary;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class SalaryMapperTest {

    private final SalaryMapper mapper = new SalaryMapper() {};

    @Test
    void toSalary_shouldWrapAmount() {
        Salary s = mapper.toSalary(new BigDecimal("123.45"));
        assertNotNull(s);
        assertEquals(new BigDecimal("123.45"), s.cantidad());
    }

    @Test
    void fromSalary_shouldReturnBigDecimalOrNull() {
        assertNull(mapper.fromSalary(null));
        assertEquals(new BigDecimal("1000"), mapper.fromSalary(new Salary(new BigDecimal("1000"))));
    }
}