package co.com.pragma.crediya.config;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;

class LoggingConfigTest {

    @Test
    void correlationIdWebFilter_shouldAddHeader_whenMissing() {
        LoggingConfig config = new LoggingConfig();
        WebFilter filter = config.correlationIdWebFilter();

        MockServerHttpRequest request = MockServerHttpRequest.get("/test").build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        WebFilterChain chain = e -> Mono.empty();

        filter.filter(exchange, chain).block();

        String cid = exchange.getResponse().getHeaders().getFirst(LoggingConfig.CORRELATION_ID_HEADER);
        assertNotNull(cid);
        assertFalse(cid.isBlank());
    }

    @Test
    void correlationIdWebFilter_shouldPropagateIncomingHeader() {
        LoggingConfig config = new LoggingConfig();
        WebFilter filter = config.correlationIdWebFilter();

        MockServerHttpRequest request = MockServerHttpRequest.get("/test")
                .header(LoggingConfig.CORRELATION_ID_HEADER, "abc-123")
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        WebFilterChain chain = e -> Mono.empty();

        filter.filter(exchange, chain).block();

        String cid = exchange.getResponse().getHeaders().getFirst(LoggingConfig.CORRELATION_ID_HEADER);
        assertEquals("abc-123", cid);
    }
}