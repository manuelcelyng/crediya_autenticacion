package co.com.pragma.crediya.api;

import co.com.pragma.crediya.api.dto.CreateUserDTO;
import co.com.pragma.crediya.api.mapper.UserDtoMapper;
import co.com.pragma.crediya.usecase.user.UserUseCase;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Set;

@Component
@RequiredArgsConstructor
@lombok.extern.slf4j.Slf4j
public class UserHandler  {

    private final UserUseCase userUseCase;
    private final UserDtoMapper mapper;
    private final Validator validator;

    public Mono<ServerResponse> listenSaveUser(ServerRequest serverRequest) {
        return serverRequest
                .bodyToMono(CreateUserDTO.class)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Request body is required")))
                .doOnSubscribe(sub -> log.info("[CREATE_USER] Request received"))
                .flatMap(dto -> {
                    Set<ConstraintViolation<CreateUserDTO>> violations = validator.validate(dto);
                    if (!violations.isEmpty()) {
                        log.warn("[CREATE_USER] Validation failed: {} violation(s)", violations.size());
                        return Mono.error(new ConstraintViolationException(violations));
                    }
                    return Mono.just(dto);
                })
                .map(mapper::toModel)
                .flatMap(userUseCase::saveUser)
                .doOnSuccess(u -> log.info("[CREATE_USER] User persisted with id={}", u.getIdNumber()))
                .flatMap(savedUser -> ServerResponse.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(mapper.toResponse(savedUser))
                )
                .doOnError(ex -> log.error("[CREATE_USER] Failure creating user: {}", ex.toString()));
    }

}
