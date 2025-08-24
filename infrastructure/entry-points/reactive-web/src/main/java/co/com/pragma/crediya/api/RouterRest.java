package co.com.pragma.crediya.api;

import co.com.pragma.crediya.usecase.user.exceptions.BusinessException;
import co.com.pragma.crediya.usecase.user.exceptions.UserAlreadyExistsException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {
    @Bean
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(POST("/api/v1/usuarios"), handler::listenSaveUser)
                .filter((req, next) -> next.handle(req)
                        .onErrorResume(UserAlreadyExistsException.class, ex ->
                                ServerResponse.status(HttpStatus.CONFLICT)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(Map.of(
                                                "code", ex.getCode(),
                                                "message", ex.getMessage(),
                                                "email", ex.getEmail()
                                        ))
                        )
                        .onErrorResume(BusinessException.class, ex ->
                                ServerResponse.status(HttpStatus.BAD_REQUEST)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(Map.of(
                                                "code", ex.getCode(),
                                                "message", ex.getMessage()
                                        ))
                        )
                        .onErrorResume(ConstraintViolationException.class, ex ->
                                ServerResponse.status(HttpStatus.BAD_REQUEST)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(Map.of(
                                                "code", "VALIDATION_ERROR",
                                                "message", "Datos de entrada inválidos",
                                                "details", ex.getConstraintViolations().stream()
                                                        .map(v -> Map.of(
                                                                "field", v.getPropertyPath().toString(),
                                                                "message", v.getMessage()
                                                        ))
                                                        .collect(Collectors.toList())
                                        ))
                        )
                        .onErrorResume(IllegalArgumentException.class, ex ->
                                ServerResponse.status(HttpStatus.BAD_REQUEST)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(Map.of(
                                                "code", "INVALID_ARGUMENT",
                                                "message", ex.getMessage()
                                        ))
                        )
                        .onErrorResume(Throwable.class, ex ->
                                ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(Map.of(
                                                "code", "INTERNAL_ERROR",
                                                "message", "Ocurrió un error inesperado"
                                        ))
                        )
                );

    }
}
