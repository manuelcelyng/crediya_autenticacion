package co.com.pragma.crediya.api.docs;

import co.com.pragma.crediya.api.UserHandler;
import co.com.pragma.crediya.api.dto.CreateUserDTO;
import co.com.pragma.crediya.api.dto.ResponseUserDTO;
import co.com.pragma.crediya.api.dto.userexists.UserExistsResponseDTO;
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

public interface UserControllerDocs {

    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/usuarios",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    method = RequestMethod.POST,
                    beanClass = UserHandler.class,
                    beanMethod = "listenSaveUser",
                    operation = @Operation(
                            operationId = "createUser",
                            summary = "Crear usuario",
                            description = "Crea un nuevo usuario",
                            requestBody = @RequestBody(required = true, content = @Content(schema = @Schema(implementation = CreateUserDTO.class))),
                            responses = {
                                    @ApiResponse(responseCode = "201", description = "Usuario creado",
                                            content = @Content(schema = @Schema(implementation = ResponseUserDTO.class))),
                                    @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
                                    @ApiResponse(responseCode = "409", description = "Usuario ya existe")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/usuarios/valid",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    method = RequestMethod.GET,
                    beanClass = UserHandler.class,
                    beanMethod = "listenUserExists",
                    operation = @Operation(
                            operationId = "UserValidByEmailAndId",
                            summary = "Verificar existencia de usuario por email y su documento de identidad",
                            description = "Retorna si existe un usuario y si es valido",
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Valido o no Valido",
                                            content = @Content(schema = @Schema(implementation = UserExistsResponseDTO.class))),
                                    @ApiResponse(responseCode = "400", description = "Parámetros inválidos")
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> routerFunction(UserHandler handler);

}
