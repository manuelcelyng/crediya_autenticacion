package co.com.pragma.crediya.model.user.gateways;

import co.com.pragma.crediya.model.user.Email;
import co.com.pragma.crediya.model.user.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository {
    Mono<User> saveUser(User user);
    Mono<Boolean> existsByCorreoElectronico(String correoElectronico);
}