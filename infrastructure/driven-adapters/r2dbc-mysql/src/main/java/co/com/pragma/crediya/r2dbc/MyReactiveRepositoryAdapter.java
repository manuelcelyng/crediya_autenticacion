package co.com.pragma.crediya.r2dbc;

import co.com.pragma.crediya.model.user.User;
import co.com.pragma.crediya.model.user.gateways.UserRepository;
import co.com.pragma.crediya.model.user.solicitudes.UserLiteView;
import co.com.pragma.crediya.r2dbc.entities.UserEntity;
import co.com.pragma.crediya.r2dbc.helper.ReactiveAdapterOperations;
import co.com.pragma.crediya.r2dbc.mappers.UserEntityMapper;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public class MyReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        User,
        UserEntity,
        Long,
        MyReactiveRepository
> implements UserRepository {

    private final UserEntityMapper userEntityMapper;
    private final PasswordEncoder passwordEncoder;

    public MyReactiveRepositoryAdapter(MyReactiveRepository repository, ObjectMapper mapper, UserEntityMapper userEntityMapper, PasswordEncoder passwordEncoder) {
        super(repository, mapper, d -> mapper.map(d, User.class));
        this.userEntityMapper = userEntityMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Mono<User> saveUser(User user) {

        UserEntity toSave = userEntityMapper.toEntity(user, passwordEncoder);

        return super.repository.save(toSave)
                .map(userEntityMapper::toDomain);
    }

    @Override
    public Mono<Boolean> existsByCorreoElectronico(String correoElectronico) {
        return super.repository.existsByCorreoElectronico(correoElectronico);
    }

    @Override
    public Mono<User> findByCorreoElectronico(String correoElectronico) {
        return super.repository.findByCorreoElectronico(correoElectronico)
                .map(userEntityMapper::toDomain);
    }

    @Override
    public Flux<UserLiteView> findLiteByCorreoElectronicoIn(List<String> correosElectronicos) {
        return super.repository.findLiteByCorreoElectronicoIn(correosElectronicos);
    }


}
