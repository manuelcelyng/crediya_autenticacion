package co.com.pragma.crediya.model.user.gateways;

import co.com.pragma.crediya.model.user.Email;
import co.com.pragma.crediya.model.user.User;
import co.com.pragma.crediya.model.user.solicitudes.UserLiteView;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface UserRepository {
    Mono<User> saveUser(User user);
    Mono<Boolean> existsByCorreoElectronico(String correoElectronico);
    Mono<User> findByCorreoElectronico(String correoElectronico);

    // Debido a los pocos datos, usamos una proyeccion en la base de datos y somos más eficientes :D salu2
    Flux<UserLiteView> findLiteByCorreoElectronicoIn(List<String> correosElectronicos);
}