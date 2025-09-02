package co.com.pragma.crediya.usecase.user;

import co.com.pragma.crediya.model.user.User;
import co.com.pragma.crediya.model.user.gateways.RolRepository;
import co.com.pragma.crediya.model.user.gateways.UserRepository;
import co.com.pragma.crediya.usecase.exceptions.RolNotFoundException;
import co.com.pragma.crediya.usecase.exceptions.TypeErrors;
import co.com.pragma.crediya.usecase.exceptions.UserAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserUseCase {

    private final UserRepository userRepository;
    private final RolRepository rolRepository;

    public Mono<User> saveUser(User user) {
        return userRepository.existsByCorreoElectronico(user.getCorreoElectronico().email())
                .flatMap(exist ->
                        exist
                                ? Mono.error(new UserAlreadyExistsException(TypeErrors.USER_ALREADY_EXISTS,user.getCorreoElectronico().email()))
                                : rolRepository.findById(user.getRolId()) )
                .switchIfEmpty(Mono.error(new RolNotFoundException(TypeErrors.ROL_NOT_FOUND,  String.format("El rol con id %d no existe", user.getRolId())
                )))
                .then(userRepository.saveUser(user));
    }

    public Mono<User> existsByCorreo(String email) {
        return userRepository.findByCorreoElectronico(email)
                .switchIfEmpty(Mono.empty());
    }
}
