package co.com.pragma.crediya.r2dbc;

import co.com.pragma.crediya.model.user.User;
import co.com.pragma.crediya.model.user.gateways.UserRepository;
import co.com.pragma.crediya.r2dbc.entities.UserEntity;
import co.com.pragma.crediya.r2dbc.exception.UserNotFoundException;
import co.com.pragma.crediya.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class MyReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        User,
        UserEntity,
        Long,
        MyReactiveRepository
> implements UserRepository
{
    public MyReactiveRepositoryAdapter(MyReactiveRepository repository, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, User.class));
    }

    @Override
    public Mono<User> saveUser(User user) {
        return super.save(user);
    }

    @Override
    public Flux<User> getAllUsers() {
        return super.findAll();
    }

    @Override
    public Mono<User> getUserByIdNumber(Long Number) {
        return super.findById(Number);
    }

    @Override
    public Mono<User> editUser(User user) {
        return super.findById(user.getIdNumber())
                .flatMap(existingUser -> super.save(user))
                .switchIfEmpty(Mono.error(new UserNotFoundException()));
    }


    @Override
    public Mono<Void> deleteUser(Long idNumber) {
        return repository.deleteById(idNumber);
    }
}
