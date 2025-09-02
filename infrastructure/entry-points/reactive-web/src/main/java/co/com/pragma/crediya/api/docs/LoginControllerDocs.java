package co.com.pragma.crediya.api.docs;

import co.com.pragma.crediya.api.LoginHandler;
import co.com.pragma.crediya.api.dto.RequestLoginDTO;
import co.com.pragma.crediya.api.dto.ResponseLoginDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

public interface LoginControllerDocs {
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/login",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    method = RequestMethod.POST,
                    beanClass = LoginHandler.class,
                    beanMethod = "listenLogin",
                    operation = @Operation(
                            operationId = "login",
                            summary = "Login",
                            description = "Recibe credenciales, valida y devuelve un token JWT",
                            requestBody = @RequestBody(required = true, content = @Content(schema = @Schema(implementation = RequestLoginDTO.class))),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Autenticación exitosa",
                                            content = @Content(schema = @Schema(implementation = ResponseLoginDTO.class))),
                                    @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
                                    @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> routerFunctionLogin(LoginHandler handler);
}
