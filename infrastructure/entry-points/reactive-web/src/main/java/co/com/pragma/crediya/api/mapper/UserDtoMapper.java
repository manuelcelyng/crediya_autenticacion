package co.com.pragma.crediya.api.mapper;

import co.com.pragma.crediya.api.dto.CreateUserDTO;
import co.com.pragma.crediya.api.dto.ResponseUserDTO;
import co.com.pragma.crediya.model.user.Email;
import co.com.pragma.crediya.model.user.Salary;
import co.com.pragma.crediya.model.user.User;
import co.com.pragma.crediya.shared.mappers.DateMapper;
import co.com.pragma.crediya.shared.mappers.EmailMapper;
import co.com.pragma.crediya.shared.mappers.SalaryMapper;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Mapper(
        componentModel = "spring",
        uses = {
                EmailMapper.class,
                SalaryMapper.class,
                DateMapper.class
        },
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface UserDtoMapper {

    // Manual factory-based mapping due to domain factory method
    default User toModel(CreateUserDTO dto) {
        if (dto == null) return null;
        return User.create(
                dto.nombre(),
                dto.apellido(),
                LocalDate.parse(dto.fechaNacimiento()),
                dto.direccion(),
                dto.telefono(),
                dto.correoElectronico() == null ? null : new Email(dto.correoElectronico()),
                dto.salarioBase() == null ? null : new Salary(dto.salarioBase()),
                dto.documentoIdentidad(),
                dto.rolId(),
                dto.password()
        );
    }

    @Mappings({
            @Mapping(target = "rolId", expression = "java(user.getRolId() == null ? null : java.math.BigDecimal.valueOf(user.getRolId()))"),
            @Mapping(target = "fechaNacimiento", source = "fechaNacimiento", qualifiedByName = "fromLocalDate"),
            @Mapping(target = "correoElectronico", source = "correoElectronico", qualifiedByName = "fromEmail"),
            @Mapping(target = "salarioBase", source = "salarioBase", qualifiedByName = "fromSalary")
    })
    ResponseUserDTO toResponse(User user);
}
