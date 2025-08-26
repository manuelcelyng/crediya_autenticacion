package co.com.pragma.crediya.api.config;

import co.com.pragma.crediya.api.UserHandler;
import co.com.pragma.crediya.api.RouterRest;
import co.com.pragma.crediya.api.UserHandler;
import co.com.pragma.crediya.api.dto.CreateUserDTO;
import co.com.pragma.crediya.api.dto.ResponseUserDTO;
import co.com.pragma.crediya.api.mapper.UserDtoMapper;
import co.com.pragma.crediya.model.user.Email;
import co.com.pragma.crediya.model.user.Salary;
import co.com.pragma.crediya.model.user.User;
import co.com.pragma.crediya.usecase.user.UserUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {RouterRest.class, UserHandler.class})
@WebFluxTest
@Import({CorsConfig.class, SecurityHeadersConfig.class})
class ConfigTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private UserUseCase userUseCase;

    @MockBean
    private UserDtoMapper userDtoMapper;

    @Test
    void corsConfigurationShouldAllowOrigins() {
        // Arrange
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

        when(userDtoMapper.toModel(any(CreateUserDTO.class))).thenReturn(domain);
        when(userUseCase.saveUser(domain)).thenReturn(Mono.just(saved));
        when(userDtoMapper.toResponse(saved)).thenReturn(response);

        // Act & Assert
        webTestClient.post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().valueEquals("Content-Security-Policy",
                        "default-src 'self'; frame-ancestors 'self'; form-action 'self'")
                .expectHeader().valueEquals("Strict-Transport-Security", "max-age=31536000;")
                .expectHeader().valueEquals("X-Content-Type-Options", "nosniff")
                .expectHeader().valueEquals("Server", "")
                .expectHeader().valueEquals("Cache-Control", "no-store")
                .expectHeader().valueEquals("Pragma", "no-cache")
                .expectHeader().valueEquals("Referrer-Policy", "strict-origin-when-cross-origin");
    }

}