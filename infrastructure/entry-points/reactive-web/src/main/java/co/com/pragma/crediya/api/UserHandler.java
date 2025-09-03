package co.com.pragma.crediya.api;

import co.com.pragma.crediya.api.dto.CreateUserDTO;
import co.com.pragma.crediya.api.dto.userexists.RequestUserExistsDTO;
import co.com.pragma.crediya.api.dto.userexists.UserExistsResponseDTO;
import co.com.pragma.crediya.api.dto.usersolicitud.RequestUserBatch;
import co.com.pragma.crediya.api.dto.usersolicitud.ResponseUsersBatch;
import co.com.pragma.crediya.api.dto.usersolicitud.UserLiteDTO;
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
import reactor.util.function.Tuples;

import java.util.Optional;
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
                    Set<ConstraintViolation<CreateUserDTO>> violations =  validator.validate(dto);
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

    // Inter-service: validate if a user exists by email
    public Mono<ServerResponse> listenUserValid(ServerRequest serverRequest){

        return serverRequest
                .bodyToMono(RequestUserExistsDTO.class)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Request body is required")))
                .doOnSubscribe(sub -> log.info("[EXISTS USER] Request received"))
                .flatMap(dto -> {
                    Set<ConstraintViolation<RequestUserExistsDTO>> violations =  validator.validate(dto);
                    if (!violations.isEmpty()) {
                        log.warn("[EXISTS USER] Validation failed: {} violation(s)", violations.size());
                        return Mono.error(new ConstraintViolationException(violations));
                    }

                    return userUseCase.existsByCorreo(dto.email())
                            // Caso: usuario encontrado
                            .flatMap(user -> {
                                log.info("[EXISTS USER] User found by email={}", dto.email());
                                boolean userValid =
                                        user.getDocumentoIdentidad().equals(dto.documentoIdentidad()) &&
                                                user.getCorreoElectronico().email().equals(dto.email());

                                return ServerResponse.ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(new UserExistsResponseDTO(userValid));
                            })
                            // Caso: no existe -> responde explícitamente
                            .switchIfEmpty(
                                    ServerResponse.ok()
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .bodyValue(new UserExistsResponseDTO(false))
                            );




                })
                .doOnError(ex -> log.error("[EXISTS USER] Failure looking user: {}", ex.toString()));
    }



    public Mono<ServerResponse> listenUserSolicitudes(ServerRequest serverRequest) {

        return serverRequest.bodyToMono(RequestUserBatch.class)
                .doOnNext(r -> log.info("[USER_BATCH] Processing request with emails={}", r.emails()))
                .flatMapMany(r -> userUseCase.findLiteByCorreoElectronicoIn(r.emails()))
                .doOnNext(view -> log.info("[USER_BATCH] Found user: nombre={}, salario={}, email={}",
                        view.getNombre(), view.getSalarioBase(), view.getCorreoElectronico()))
                .map(view -> new UserLiteDTO(view.getNombre(), view.getSalarioBase(), view.getCorreoElectronico()))
                .doOnNext(dto -> log.info("[USER_BATCH] Mapped to DTO: {}", dto))
                .collectList()
                .doOnNext(users -> log.info("[USER_BATCH] Collected {} users", users.size()))
                .flatMap(users -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(new ResponseUsersBatch(users))
                );

    }

}
