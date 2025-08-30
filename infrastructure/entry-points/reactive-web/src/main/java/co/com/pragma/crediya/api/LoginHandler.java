package co.com.pragma.crediya.api;

import co.com.pragma.crediya.api.dto.RequestLoginDTO;
import co.com.pragma.crediya.api.dto.ResponseLoginDTO;
import co.com.pragma.crediya.usecase.login.LoginUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class LoginHandler {

    private final LoginUseCase loginUseCase;

    public Mono<ServerResponse> listenLogin(ServerRequest serverRequest) {
        // Devolver un TokenReponse en el serverResponse :D
        return serverRequest.bodyToMono(RequestLoginDTO.class)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Request body is required")))
                .doOnSubscribe(sub -> log.info("[LOGIN_USER] Request received"))
                .flatMap(req -> loginUseCase.login(req.email(), req.password()))
                .flatMap(t -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(new ResponseLoginDTO(t.accessToken(),"Bearer", t.ttlMinutes())));
    }


    public Mono<ServerResponse> listenRefreshToken(ServerRequest serverRequest) {
        // Recibe un toke refresh que se envia al useCare
        // Devuelve un TokenResponse nuevo :D refreshhh
        return null;
    }

}