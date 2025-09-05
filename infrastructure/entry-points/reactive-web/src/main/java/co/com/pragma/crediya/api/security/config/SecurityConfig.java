package co.com.pragma.crediya.api.security.config;


import co.com.pragma.crediya.api.security.JwtAuthenticationConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import reactor.core.publisher.Mono;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableWebFluxSecurity
public class SecurityConfig {

    @Value( "${app.publicPaths}")
    private List<String> publicPaths;


    @Bean
    SecurityWebFilterChain filterChain(ServerHttpSecurity http, JwtAuthenticationConverter jwtConverter) {
        // manager tonto: el converter ya crea el Authentication válido
        ReactiveAuthenticationManager manager = authentication -> Mono.just(authentication);

        AuthenticationWebFilter jwtFilter = new AuthenticationWebFilter(manager);
        jwtFilter.setServerAuthenticationConverter(jwtConverter);
        jwtFilter.setSecurityContextRepository(NoOpServerSecurityContextRepository.getInstance());

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .authorizeExchange(ex -> {
                    publicPaths.forEach(path -> ex.pathMatchers(path).permitAll());
                    ex.pathMatchers(HttpMethod.POST, "/api/v1/login").permitAll()
                            .pathMatchers(HttpMethod.GET, "/api/v1/ping").permitAll()
                        // reglas de negocio:
                            .pathMatchers(HttpMethod.POST, "/api/v1/usuarios/solicitud").hasAnyAuthority("ADMIN", "ASESOR")
                            .pathMatchers(HttpMethod.POST, "/api/v1/usuarios").hasAnyAuthority("ADMIN", "ASESOR")
                            .pathMatchers(HttpMethod.POST, "/api/v1/usuarios/validar").hasAuthority("CLIENTE")
                            .anyExchange().authenticated();
                })
                .addFilterAt(jwtFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){ // Para encriptar la contraseña
        return new BCryptPasswordEncoder();
    }


}
