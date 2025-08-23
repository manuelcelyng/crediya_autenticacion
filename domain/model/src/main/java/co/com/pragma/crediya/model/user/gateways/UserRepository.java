package co.com.pragma.crediya.model.user.gateways;

import co.com.pragma.crediya.model.user.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository {
    Mono<User> saveUser(User user);
    Flux<User> getAllUsers();
    Mono<User> getUserByIdNumber(Long Number);
    Mono<User> editUser(User user);
    Mono<Void> deleteUser(Long idNumber);
}
