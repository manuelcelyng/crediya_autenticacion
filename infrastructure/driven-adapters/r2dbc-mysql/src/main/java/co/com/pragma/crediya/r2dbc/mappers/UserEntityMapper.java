package co.com.pragma.crediya.r2dbc.mappers;

import co.com.pragma.crediya.model.user.Email;
import co.com.pragma.crediya.model.user.Salary;
import co.com.pragma.crediya.model.user.User;
import co.com.pragma.crediya.r2dbc.entities.UserEntity;
import org.mapstruct.Mapper;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface UserEntityMapper {

    default UserEntity toEntity(User domain) {
        if (domain == null) return null;
        return UserEntity.builder()
                .idUsuario(domain.getIdNumber())
                .nombre(domain.getNombre())
                .apellido(domain.getApellido())
                .fechaNacimiento(domain.getFechaNacimiento())
                .direccion(domain.getDireccion())
                .telefono(domain.getTelefono())
                .correoElectronico(domain.getCorreoElectronico() == null ? null : domain.getCorreoElectronico().email())
                .salarioBase(domain.getSalarioBase() == null ? null : domain.getSalarioBase().cantidad())
                .documentoIdentidad(domain.getDocumentoIdentidad())
                .rolId(domain.getRolId() == null ? null : domain.getRolId().longValue())
                .build();
    }

    default User toDomain(UserEntity entity) {
        if (entity == null) return null;
        User created = User.create(
                entity.getNombre(),
                entity.getApellido(),
                entity.getFechaNacimiento(),
                entity.getDireccion(),
                entity.getTelefono(),
                entity.getCorreoElectronico() == null ? null : new Email(entity.getCorreoElectronico()),
                entity.getSalarioBase() == null ? null : new Salary(entity.getSalarioBase()),
                entity.getDocumentoIdentidad(),
                entity.getRolId() == null ? null : BigDecimal.valueOf(entity.getRolId())
        );
        return entity.getIdUsuario() == null ? created : created.withId(entity.getIdUsuario());
    }
}
