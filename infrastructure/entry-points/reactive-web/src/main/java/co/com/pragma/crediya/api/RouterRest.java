package co.com.pragma.crediya.api;

import co.com.pragma.crediya.api.docs.UserControllerDocs;
import co.com.pragma.crediya.api.dto.CreateUserDTO;
import co.com.pragma.crediya.api.dto.ResponseUserDTO;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest implements UserControllerDocs {


    @Bean
    public RouterFunction<ServerResponse> routerFunction(UserHandler handler) {
        return route(POST("/api/v1/usuarios"), handler::listenSaveUser);
    }

}
