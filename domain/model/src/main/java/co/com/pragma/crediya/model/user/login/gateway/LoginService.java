package co.com.pragma.crediya.model.user.login.gateway;

import co.com.pragma.crediya.model.user.login.AuthTokens;
import reactor.core.publisher.Mono;

public interface LoginService
{

    public Mono<AuthTokens> login(String email, String rawPassword);

}
