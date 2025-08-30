package co.com.pragma.crediya.usecase.login;

import co.com.pragma.crediya.model.user.login.AuthTokens;
import co.com.pragma.crediya.model.user.login.gateway.LoginService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class LoginUseCase {

    private final LoginService loginService;

    public Mono<AuthTokens> login(String email, String rawPassword){
        return loginService.login(email, rawPassword);
    }

}
