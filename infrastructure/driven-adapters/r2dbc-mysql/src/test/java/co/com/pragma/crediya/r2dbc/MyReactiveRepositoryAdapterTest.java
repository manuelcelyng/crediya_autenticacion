package co.com.pragma.crediya.r2dbc;

import co.com.pragma.crediya.model.user.User;
import co.com.pragma.crediya.r2dbc.entities.UserEntity;
import co.com.pragma.crediya.r2dbc.mappers.UserEntityMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.Example;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MyReactiveRepositoryAdapterTest {

    @InjectMocks
    MyReactiveRepositoryAdapter repositoryAdapter;

    @Mock
    MyReactiveRepository repository;

    @Mock
    ObjectMapper mapper;

    @Mock
    UserEntityMapper userEntityMapper;

    private User buildUserWithId(Long id, String nombre, String apellido) {
        return User.create(
                nombre,
                apellido,
                LocalDate.parse("1990-01-01"),
                null,
                null,
                null,
                null,
                "DOC",
                null
        ).withId(id);
    }

    private User buildUserExample(String nombre) {
        return User.create(
                nombre,
                "X",
                LocalDate.parse("1990-01-01"),
                null,
                null,
                null,
                null,
                "DOC",
                null
        );
    }

    @Test
    void mustFindValueById() {
        Long id = 1L;
        UserEntity ue = UserEntity.builder()
                .idUsuario(id)
                .nombre("John")
                .apellido("Doe")
                .build();
        User u = buildUserWithId(id, "John", "Doe");

        when(repository.findById(id)).thenReturn(Mono.just(ue));
        when(mapper.map(ue, User.class)).thenReturn(u);

        Mono<User> result = repositoryAdapter.findById(id);

        StepVerifier.create(result)
                .expectNext(u)
                .verifyComplete();
    }

    @Test
    void mustFindAllValues() {
        UserEntity ue = UserEntity.builder().idUsuario(1L).nombre("John").apellido("Doe").build();
        User u = buildUserWithId(1L, "John", "Doe");

        when(repository.findAll()).thenReturn(Flux.just(ue));
        when(mapper.map(ue, User.class)).thenReturn(u);

        Flux<User> result = repositoryAdapter.findAll();

        StepVerifier.create(result)
                .expectNext(u)
                .verifyComplete();
    }

    @Test
    void mustFindByExample() {
        User example = buildUserExample("John");
        UserEntity ueExample = UserEntity.builder().nombre("John").build();
        UserEntity ue = UserEntity.builder().idUsuario(1L).nombre("John").apellido("Doe").build();
        User u = buildUserWithId(1L, "John", "Doe");

        when(mapper.map(example, UserEntity.class)).thenReturn(ueExample);
        when(repository.findAll(any(Example.class))).thenReturn(Flux.just(ue));
        when(mapper.map(ue, User.class)).thenReturn(u);

        Flux<User> result = repositoryAdapter.findByExample(example);

        StepVerifier.create(result)
                .expectNext(u)
                .verifyComplete();
    }

    @Test
    void mustSaveValue() {
        User u = buildUserWithId(1L, "John", "Doe");
        UserEntity ue = UserEntity.builder().idUsuario(1L).nombre("John").apellido("Doe").build();

        when(mapper.map(u, UserEntity.class)).thenReturn(ue);
        when(repository.save(ue)).thenReturn(Mono.just(ue));
        when(mapper.map(ue, User.class)).thenReturn(u);

        Mono<User> result = repositoryAdapter.save(u);

        StepVerifier.create(result)
                .expectNext(u)
                .verifyComplete();
    }
}
