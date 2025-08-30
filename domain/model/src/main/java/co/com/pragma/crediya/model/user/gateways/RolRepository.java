package co.com.pragma.crediya.model.user.gateways;


import co.com.pragma.crediya.model.user.Rol;
import reactor.core.publisher.Mono;

public interface RolRepository {

    //Mono<Role> findByName(String name);
    Mono<Rol> findById(Long id);
}
