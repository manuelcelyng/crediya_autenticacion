package co.com.pragma.crediya.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "API de gestión de Autenticacion",
        version = "1.0.0",
        description = "Microservicios para la gestión de Autenticación",
        termsOfService = "Ninguno :D",
        contact = @Contact(name = "Manuel Cely", email = "manuelcely75@gmail.com", url = "notengo.com"),
        license = @License(name = "License")),
        servers = {
                @Server(description = "devServer", url = "http://localhost:8080"),
                @Server(description = "testServer", url = "http://localhost:8080"),
        }, // quitar este parentesis :D
        security = @SecurityRequirement(name = "bearerAuth"))
@SecurityScheme(name = "bearerAuth", scheme = "bearer", type = SecuritySchemeType.HTTP, description = "JWT Bearer autenticación", bearerFormat = "JWT")
public class SwaggerConfig {
}
