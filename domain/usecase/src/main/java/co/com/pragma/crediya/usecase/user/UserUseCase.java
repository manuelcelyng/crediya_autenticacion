package co.com.pragma.crediya.usecase.user;

import co.com.pragma.crediya.model.user.User;
import co.com.pragma.crediya.model.user.gateways.UserRepository;
import co.com.pragma.crediya.usecase.user.exceptions.UserAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserUseCase {

    private final UserRepository userRepository;

    public Mono<User> saveUser(User user) {
        return userRepository.existsByCorreoElectronico(user.getCorreoElectronico().email())
                .flatMap(exists ->
                    exists
                            ? Mono.error(new UserAlreadyExistsException(user.getCorreoElectronico().email()))
                            : userRepository.saveUser(user)
                );

    }
}
