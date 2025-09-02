package co.com.pragma.crediya.api;

import co.com.pragma.crediya.api.docs.LoginControllerDocs;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class LoginRouterRest implements LoginControllerDocs {

    @Bean
    public RouterFunction<ServerResponse> routerFunctionLogin(LoginHandler handler) {
        return route(POST("/api/v1/login"), handler::listenLogin)
                .andRoute(GET("/api/v1/ping"), handler::listenPing);
    }

}