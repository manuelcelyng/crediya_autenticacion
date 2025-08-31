package co.com.pragma.crediya.security;

import co.com.pragma.crediya.model.user.User;
import co.com.pragma.crediya.model.user.gateways.RolRepository;
import co.com.pragma.crediya.model.user.gateways.UserRepository;
import co.com.pragma.crediya.model.user.login.AuthTokens;
import co.com.pragma.crediya.model.user.login.gateway.LoginService;
import co.com.pragma.crediya.usecase.exceptions.InvalidCredentials;
import co.com.pragma.crediya.usecase.exceptions.RolNotFoundException;
import co.com.pragma.crediya.usecase.exceptions.TypeErrors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Set;

@Service
public class LoginServiceImpl implements LoginService {
    private static final Logger log = LoggerFactory.getLogger(LoginServiceImpl.class);
    private final UserRepository users;
    private final RolRepository roles;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    public LoginServiceImpl(UserRepository users, RolRepository roles, PasswordEncoder encoder, JwtService jwt) {
        this.users = users;
        this.roles = roles;
        this.encoder = encoder;
        this.jwt = jwt;
    }

    public Mono<AuthTokens> login(String email, String password) {
        return users.findByCorreoElectronico(email)
                .switchIfEmpty(Mono.error(new InvalidCredentials(TypeErrors.INVALID_CREDENTIALS)))
                .flatMap(u -> validatePassword(u, password))
                .zipWhen(u -> roles.findById(u.getRolId())
                        .switchIfEmpty(Mono.error(new RolNotFoundException(TypeErrors.ROL_NOT_FOUND,  String.valueOf(u.getRolId())
                        )))
                        .doOnSuccess(rol -> log.info(" [LOGIN-OK]  email={} rol={}", email, rol.getNombre()))
                )
                .map(tuple -> {
                    var user = tuple.getT1(); // Cojo el usuario
                    var rol = tuple.getT2();  // Cojo el rol
                    String token = jwt.generate(
                            String.valueOf(user.getIdNumber()),
                            user.getCorreoElectronico().email(),
                            Set.of(rol.getNombre())  // En el token guardamos Set of Roles :D, pero la APP solo soporta un rol por usuario
                    );

                    log.info(" [LOGIN-OK]  email={} roles={}", email, rol.getNombre());
                    return new AuthTokens(token, jwt.expiresAt(), jwt.getttlMinutes());

                })
                .doOnError(e -> log.warn(" [LOGIN-FAIL] email={} err={}", email, e.getMessage()));

    }

    private Mono<User> validatePassword(User u, String raw) {
        //if (!u.enabled()) return Mono.error(new InvalidCredentials("Usuario deshabilitado")); Posible funcionalidad :D
        if (!encoder.matches(raw, u.getPassword())) return Mono.error(new InvalidCredentials(TypeErrors.INVALID_CREDENTIALS));
        return Mono.just(u);
    }
}
