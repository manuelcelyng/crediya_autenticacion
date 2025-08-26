package co.com.pragma.crediya.api.errors;

import co.com.pragma.crediya.shared.errors.ErrorDetail;
import co.com.pragma.crediya.shared.errors.ErrorResponse;
import co.com.pragma.crediya.usecase.user.exceptions.BusinessException;
import co.com.pragma.crediya.usecase.user.exceptions.UserAlreadyExistsException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
@lombok.extern.slf4j.Slf4j
public class GlobalErrorHandlerConfig {

    @Bean
    public DefaultErrorAttributes defaultErrorAttributes() {
        return new DefaultErrorAttributes();
    }

    @Bean
    @Order(-2)
    public ErrorWebExceptionHandler globalExceptionHandler(DefaultErrorAttributes errorAttributes,
                                                           ApplicationContext applicationContext) {
        WebProperties.Resources resources = new WebProperties.Resources();
        return new GlobalErrorWebExceptionHandler(errorAttributes, resources, applicationContext);
    }

    static class GlobalErrorWebExceptionHandler extends org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler {

        GlobalErrorWebExceptionHandler(DefaultErrorAttributes g, WebProperties.Resources r, ApplicationContext c) {
            super(g, r, c);
            super.setMessageReaders(ServerCodecConfigurer.create().getReaders());
            super.setMessageWriters(ServerCodecConfigurer.create().getWriters());
        }

        @Override
        protected RouterFunction<ServerResponse> getRoutingFunction(final ErrorAttributes errorAttributes) {
            return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
        }

        private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
            Throwable ex = getError(request);

            String correlationId = request.headers().firstHeader("X-Correlation-Id");

            HttpStatus status;
            ErrorResponse payload;

            log.error("[ERROR] {} on path={} correlationId={}", ex.getClass().getSimpleName(), request.path(), correlationId, ex);

            if (ex instanceof UserAlreadyExistsException uae) {
                status = HttpStatus.CONFLICT;
                payload = new ErrorResponse(
                        uae.getCode(),
                        uae.getEmail(),
                        status.value(),
                        request.path(),
                        java.time.Instant.now(),
                        correlationId,
                        null
                );
            } else if (ex instanceof BusinessException be) {
                status = HttpStatus.BAD_REQUEST;
                payload = ErrorResponse.of(
                        be.getCode(),
                        be.getMessage(),
                        status.value(),
                        request.path(),
                        null,
                        correlationId
                );
            } else if (ex instanceof ConstraintViolationException cve) { // el validation del jakarta
                status = HttpStatus.BAD_REQUEST;
                List<ErrorDetail> details = cve.getConstraintViolations().stream()
                        .map(v -> new ErrorDetail(v.getPropertyPath().toString(), v.getMessage()))
                        .collect(Collectors.toList());
                payload = ErrorResponse.of(
                        "VALIDATION_ERROR",
                        "Datos de entrada inválidos",
                        status.value(),
                        request.path(),
                        details,
                        correlationId
                );
            } else if (ex instanceof IllegalArgumentException iae) {
                status = HttpStatus.BAD_REQUEST;
                payload = ErrorResponse.of(
                        "INVALID_ARGUMENT",
                        iae.getMessage(),
                        status.value(),
                        request.path(),
                        null,
                        correlationId
                );
            } else {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
                payload = ErrorResponse.of(
                        "INTERNAL_ERROR",
                        "Ocurrió un error inesperado",
                        status.value(),
                        request.path(),
                        null,
                        correlationId
                );
            }

            return ServerResponse.status(status)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(payload));
        }
    }
}
