package co.com.pragma.crediya.shared.mappers;

import co.com.pragma.crediya.model.user.Salary;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.math.BigDecimal;


@Mapper(componentModel = "spring")
public interface SalaryMapper {
    @Named("toSalary")
    default Salary toSalary(BigDecimal amount) { return new Salary(amount); }

    @Named("fromSalary")
    default BigDecimal fromSalary(Salary salary) { return salary == null ? null : salary.cantidad(); }
}
