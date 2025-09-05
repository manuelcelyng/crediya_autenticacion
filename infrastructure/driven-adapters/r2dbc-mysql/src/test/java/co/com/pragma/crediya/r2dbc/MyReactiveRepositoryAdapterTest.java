package co.com.pragma.crediya.r2dbc;

import co.com.pragma.crediya.model.user.Email;
import co.com.pragma.crediya.model.user.Salary;
import co.com.pragma.crediya.model.user.User;
import co.com.pragma.crediya.r2dbc.entities.UserEntity;
import co.com.pragma.crediya.r2dbc.mappers.UserEntityMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.data.domain.Example;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MyReactiveRepositoryAdapterTest {

    @InjectMocks
    MyReactiveRepositoryAdapter repositoryAdapter;

    @Mock
    MyReactiveRepository repository;

    // ObjectMapper sigue siendo necesario como dependencia del constructor de la clase padre
    @Mock
    ObjectMapper mapper;

    @Mock
    UserEntityMapper userEntityMapper;

    @Mock
    PasswordEncoder passwordEncoder;

    private User buildUserWithId(Long id, String nombre, String apellido) {
        return User.create(
                nombre,
                apellido,
                LocalDate.parse("1990-01-01"),
                "Calle Falsa 123",
                "3001234567",
                new Email("test@pragma.com.co"),
                new Salary(new BigDecimal("2000000")),
                "DOC123",
                400000L,
                "secret"
        ).withId(id);
    }

    private User buildUserExample(String nombre) {
        return User.create(
                nombre,
                "Doe",
                LocalDate.parse("1990-01-01"),
                "Calle Falsa 123",
                "3001234567",
                new Email("test@pragma.com.co"),
                new Salary(new BigDecimal("2000000")),
                "DOC123",
                400000L,
                "secret"
        );
    }

    @Test
    void mustSaveUser() {
        // Arrange: Preparamos los datos y mocks correctos
        User userToSave = buildUserExample("John"); // Usuario de dominio (sin ID)
        UserEntity entityToSave = new UserEntity(); // Entidad mapeada (sin ID)
        UserEntity savedEntity = UserEntity.builder().idUsuario(1L).nombre("John").build(); // Entidad como la devuelve la BD
        User expectedUser = buildUserWithId(1L, "John", "Doe"); // Usuario de dominio final (con ID)

        // Simula el comportamiento del UserEntityMapper, que es lo que usa el método real
        when(userEntityMapper.toEntity(userToSave, passwordEncoder)).thenReturn(entityToSave);
        when(repository.save(entityToSave)).thenReturn(Mono.just(savedEntity));
        when(userEntityMapper.toDomain(savedEntity)).thenReturn(expectedUser);

        // Act: Llamamos al método en la instancia bajo prueba
        Mono<User> result = repositoryAdapter.saveUser(userToSave);

        // Assert: Verificamos que el flujo reactivo emita el usuario esperado
        StepVerifier.create(result)
                .expectNext(expectedUser)
                .verifyComplete();
    }

    @Test
    void existsByCorreoElectronico_shouldReturnTrue_whenEmailExists() {
        // Arrange
        String existingEmail = "test@pragma.com.co";
        when(repository.existsByCorreoElectronico(existingEmail)).thenReturn(Mono.just(true));

        // Act
        Mono<Boolean> result = repositoryAdapter.existsByCorreoElectronico(existingEmail);

        // Assert
        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void existsByCorreoElectronico_shouldReturnFalse_whenEmailDoesNotExist() {
        // Arrange
        String nonExistingEmail = "noexiste@pragma.com.co";
        when(repository.existsByCorreoElectronico(nonExistingEmail)).thenReturn(Mono.just(false));

        // Act
        Mono<Boolean> result = repositoryAdapter.existsByCorreoElectronico(nonExistingEmail);

        // Assert
        StepVerifier.create(result)
                .expectNext(false)
                .verifyComplete();
    }

    // --- OTROS TESTS (los he omitido por brevedad, pero deberían seguir aquí) ---
    @Test
    void mustFindValueById() {
        Long id = 1L;
        UserEntity ue = UserEntity.builder().idUsuario(id).nombre("John").apellido("Doe").build();
        User u = buildUserWithId(id, "John", "Doe");

        when(repository.findById(id)).thenReturn(Mono.just(ue));
        // El método findById es de la clase padre y sí usa el ObjectMapper genérico
        when(mapper.map(ue, User.class)).thenReturn(u);

        Mono<User> result = repositoryAdapter.findById(id);

        StepVerifier.create(result)
                .expectNext(u)
                .verifyComplete();
    }
}