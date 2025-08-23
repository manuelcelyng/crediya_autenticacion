package co.com.pragma.crediya.r2dbc;

import co.com.pragma.crediya.model.user.User;
import co.com.pragma.crediya.r2dbc.entities.UserEntity;
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

    @Test
    void mustFindValueById() {
        Long id = 1L;
        UserEntity ue = UserEntity.builder()
                .idUsuario(id)
                .nombre("John")
                .apellido("Doe")
                .build();
        User u = User.builder()
                .idNumber(id)
                .nombre("John")
                .apellido("Doe")
                .build();

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
        User u = User.builder().idNumber(1L).nombre("John").apellido("Doe").build();

        when(repository.findAll()).thenReturn(Flux.just(ue));
        when(mapper.map(ue, User.class)).thenReturn(u);

        Flux<User> result = repositoryAdapter.findAll();

        StepVerifier.create(result)
                .expectNext(u)
                .verifyComplete();
    }

    @Test
    void mustFindByExample() {
        User example = User.builder().nombre("John").build();
        UserEntity ueExample = UserEntity.builder().nombre("John").build();
        UserEntity ue = UserEntity.builder().idUsuario(1L).nombre("John").apellido("Doe").build();
        User u = User.builder().idNumber(1L).nombre("John").apellido("Doe").build();

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
        User u = User.builder().idNumber(1L).nombre("John").apellido("Doe").build();
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
