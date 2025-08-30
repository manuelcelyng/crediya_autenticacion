package co.com.pragma.crediya.r2dbc.mappers;

import co.com.pragma.crediya.model.user.Rol;
import co.com.pragma.crediya.r2dbc.entities.RolEntity;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RolEntityMapper {

    RolEntity toEntity(Rol role);


    Rol toDomain(RolEntity entity);


}
