package co.com.pragma.crediya.usecase.user;

import co.com.pragma.crediya.model.user.Email;
import co.com.pragma.crediya.model.user.Rol;
import co.com.pragma.crediya.model.user.Salary;
import co.com.pragma.crediya.model.user.User;
import co.com.pragma.crediya.model.user.gateways.RolRepository;
import co.com.pragma.crediya.model.user.gateways.UserRepository;
import co.com.pragma.crediya.usecase.exceptions.RolNotFoundException;
import co.com.pragma.crediya.usecase.exceptions.UserAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UserUseCaseTest {

    private UserRepository userRepository;
    private RolRepository rolRepository;
    private UserUseCase useCase;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        rolRepository = Mockito.mock(RolRepository.class);
        useCase = new UserUseCase(userRepository, rolRepository);
    }

    private User sampleUser() {
        return User.create(
                "Juan",
                "Perez",
                LocalDate.parse("1990-01-01"),
                "Calle 1",
                "3000000000",
                new Email("juan.perez@example.com"),
                new Salary(new BigDecimal("1000000")),
                "CC123",
                1L,
                "secretPass"
        );
    }

    @Test
    @DisplayName("Si el correo ya existe debe emitir UserAlreadyExistsException")
    void shouldEmitErrorWhenEmailExists() {
        User user = sampleUser();
        when(userRepository.existsByCorreoElectronico(user.getCorreoElectronico().email())).thenReturn(Mono.just(true));

        StepVerifier.create(useCase.saveUser(user))
                .expectErrorSatisfies(ex -> {
                    assert ex instanceof UserAlreadyExistsException;
                    assert ((UserAlreadyExistsException) ex).getEmail().equals("juan.perez@example.com");
                })
                .verify();
    }

    @Test
    @DisplayName("Si el correo no existe y el rol existe debe guardar el usuario")
    void shouldSaveWhenEmailNotExistsAndRoleExists() {
        User user = sampleUser();
        when(userRepository.existsByCorreoElectronico(user.getCorreoElectronico().email())).thenReturn(Mono.just(false));
        when(rolRepository.findById(user.getRolId())).thenReturn(Mono.just(Rol.builder().idRol(user.getRolId()).nombre("USER").build()));
        when(userRepository.saveUser(any(User.class))).thenAnswer(inv -> Mono.just(((User)inv.getArgument(0)).withId(10L)));

        StepVerifier.create(useCase.saveUser(user))
                .expectNextMatches(saved -> saved.getIdNumber() != null && saved.getIdNumber() == 10L)
                .verifyComplete();
    }

    @Test
    @DisplayName("Si el correo no existe pero el rol no se encuentra debe emitir RolNotFoundException")
    void shouldEmitErrorWhenRoleNotFound() {
        User user = sampleUser();
        when(userRepository.existsByCorreoElectronico(user.getCorreoElectronico().email())).thenReturn(Mono.just(false));
        when(rolRepository.findById(user.getRolId())).thenReturn(Mono.empty());

        StepVerifier.create(useCase.saveUser(user))
                .expectError(RolNotFoundException.class)
                .verify();
    }
}
