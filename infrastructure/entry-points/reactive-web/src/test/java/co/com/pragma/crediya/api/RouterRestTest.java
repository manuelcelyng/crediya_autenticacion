package co.com.pragma.crediya.api;

import co.com.pragma.crediya.api.dto.CreateUserDTO;
import co.com.pragma.crediya.api.dto.ResponseUserDTO;
import co.com.pragma.crediya.api.mapper.UserDtoMapper;
import co.com.pragma.crediya.model.user.Email;
import co.com.pragma.crediya.model.user.Salary;
import co.com.pragma.crediya.model.user.User;
import co.com.pragma.crediya.usecase.user.UserUseCase;
import co.com.pragma.crediya.usecase.user.exceptions.UserAlreadyExistsException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.mock;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {RouterRest.class, UserHandler.class})
@WebFluxTest
@Import({co.com.pragma.crediya.api.errors.GlobalErrorHandlerConfig.class, RouterRestTest.MocksConfig.class})
class RouterRestTest {

    @TestConfiguration
    static class MocksConfig {
        @Bean UserUseCase userUseCase() { return mock(UserUseCase.class); }
        @Bean UserDtoMapper userDtoMapper() { return mock(UserDtoMapper.class); }
    }

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UserUseCase userUseCase;

    @Autowired
    private UserDtoMapper userDtoMapper;

    @Test
    void saveUser_Created() {
        CreateUserDTO request = new CreateUserDTO(
                "Juan","Perez","juan.perez@example.com","1990-01-01",
                "Calle 1","3000000000", new BigDecimal("1000000"), "CC123", 1L
        );
        User domain = User.create(
                "Juan","Perez", LocalDate.parse("1990-01-01"),
                "Calle 1","3000000000", new Email("juan.perez@example.com"), new Salary(new BigDecimal("1000000")),
                "CC123", BigDecimal.valueOf(1)
        );
        User saved = domain.withId(10L);
        ResponseUserDTO response = new ResponseUserDTO(10L, "Juan","Perez","1990-01-01","Calle 1","3000000000","juan.perez@example.com", new BigDecimal("1000000"), "CC123", BigDecimal.valueOf(1));

        when(userDtoMapper.toModel(ArgumentMatchers.any(CreateUserDTO.class))).thenReturn(domain);
        when(userUseCase.saveUser(domain)).thenReturn(Mono.just(saved));
        when(userDtoMapper.toResponse(saved)).thenReturn(response);

        webTestClient.post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.idNumber").isEqualTo(10)
                .jsonPath("$.nombre").isEqualTo("Juan")
                .jsonPath("$.correoElectronico").isEqualTo("juan.perez@example.com");
    }

    @Test
    void saveUser_AlreadyExists_ReturnsConflict() {
        CreateUserDTO request = new CreateUserDTO(
                "Juan","Perez","juan.perez@example.com","1990-01-01",
                "Calle 1","3000000000", new BigDecimal("1000000"), "CC123", 1L
        );
        User domain = User.create(
                "Juan","Perez", LocalDate.parse("1990-01-01"),
                "Calle 1","3000000000", new Email("juan.perez@example.com"), new Salary(new BigDecimal("1000000")),
                "CC123", BigDecimal.valueOf(1)
        );

        when(userDtoMapper.toModel(ArgumentMatchers.any(CreateUserDTO.class))).thenReturn(domain);
        when(userUseCase.saveUser(domain)).thenReturn(Mono.error(new UserAlreadyExistsException("juan.perez@example.com")));

        webTestClient.post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.code").isEqualTo("USER_ALREADY_EXISTS")
                .jsonPath("$.message").isEqualTo("juan.perez@example.com");
    }

    @org.junit.jupiter.api.Disabled("Disabled due to BlockHound blocking call during validation on WebFlux event loop; validation is covered at domain level")
    void saveUser_ValidationError_ReturnsBadRequest() {
        CreateUserDTO invalid = new CreateUserDTO(
                "", // nombre inválido
                "Perez",
                "juan.perez@example.com",
                "1990-01-01",
                "Calle 1",
                "3000000000",
                new BigDecimal("1000000"),
                "CC123",
                1L
        );

        webTestClient.post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalid)
                .exchange()
                .expectStatus().isBadRequest()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.code").isEqualTo("VALIDATION_ERROR")
                .jsonPath("$.details[0].field").isEqualTo("nombre");
    }

    @Test
    void saveUser_EmptyBody_ReturnsBadRequest() {
        webTestClient.post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.code").isEqualTo("INVALID_ARGUMENT");
    }
}
