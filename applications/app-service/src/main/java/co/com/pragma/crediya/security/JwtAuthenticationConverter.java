package co.com.pragma.crediya.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationConverter implements ServerAuthenticationConverter {


    private final JwtService jwt;

    public JwtAuthenticationConverter(JwtService jwt) {
        this.jwt = jwt;
    }


    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        String header = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith("Bearer ")) return Mono.empty();

        String token = header.substring(7);
        return Mono.fromCallable(() -> jwt.parseAndValidate(token))
                .map(Jws::getPayload)
                .map(this::toAuthentication)
                .onErrorResume(e -> Mono.empty()); // token inválido → sin Authentication → 401 más adelante
    }


    private Authentication toAuthentication(Claims claims) {
        String userId = claims.getSubject(); // al crearlo coloque sub = userId en el JwtService , por eso lo pongo


        var authorities = extractRolesFromClaims(claims).stream()
                .map(SimpleGrantedAuthority::new)
                .toList();

        var authentication = new UsernamePasswordAuthenticationToken(userId, "N/A", authorities);
        authentication.setDetails(claims); // opcional: dejas todos los claims en details
        return authentication;
    }

    private Collection<String> extractRolesFromClaims(Claims claims) {
        Object rawRoles = claims.get("roles");
        if(rawRoles==null) return Collections.emptyList();
        else if(rawRoles instanceof Collection<?>) return ((Collection<?>) rawRoles).stream().map(Objects::toString).collect(Collectors.toSet());
        else {return Set.of(rawRoles.toString());}

    }

}
