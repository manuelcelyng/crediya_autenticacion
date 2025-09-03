package co.com.pragma.crediya.r2dbc;

import co.com.pragma.crediya.model.user.solicitudes.UserLiteView;
import co.com.pragma.crediya.r2dbc.entities.UserEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

// TODO: This file is just an example, you should delete or modify it
public interface MyReactiveRepository extends ReactiveCrudRepository<UserEntity, Long>, ReactiveQueryByExampleExecutor<UserEntity> {

    Mono<Boolean> existsByCorreoElectronico(String correoElectronico);
    Mono<UserEntity> findByCorreoElectronico(String correoElectronico);

    @Query("""
      SELECT 
        nombre,
        salario_base,
        correo_electronico
      FROM usuarios
      WHERE correo_electronico IN (:correosElectronicos)
    """)
    Flux<UserLiteView> findLiteByCorreoElectronicoIn(List<String> correosElectronicos);

}
