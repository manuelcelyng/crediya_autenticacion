package co.com.pragma.crediya.r2dbc.roles;

import co.com.pragma.crediya.model.user.Rol;
import co.com.pragma.crediya.model.user.gateways.RolRepository;
import co.com.pragma.crediya.r2dbc.entities.RolEntity;
import co.com.pragma.crediya.r2dbc.helper.ReactiveAdapterOperations;
import co.com.pragma.crediya.r2dbc.mappers.RolEntityMapper;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class RolReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Rol,
        RolEntity,
        Long,
        RolReactiveRepository
> implements RolRepository {

    private final RolEntityMapper roleEntityMapper;

    public RolReactiveRepositoryAdapter(RolReactiveRepository repository, ObjectMapper mapper, RolEntityMapper roleEntityMapper) {
        super(repository, mapper, d -> mapper.map(d, Rol.class));
        this.roleEntityMapper = roleEntityMapper;
    }

    /*  //TODO: Probas si no funciona con el metodo que trae por defecto en el helper :D
    @Override
    Mono<Rol> findById(Long idRole){
        return super.repository.findById(idRole)
                .map(roleEntityMapper::toDomain);

    }
        */

}
