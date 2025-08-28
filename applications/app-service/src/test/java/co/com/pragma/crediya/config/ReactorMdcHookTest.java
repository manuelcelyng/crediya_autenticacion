package co.com.pragma.crediya.config;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class ReactorMdcHookTest {

    private ReactorMdcHook hook;

    @BeforeEach
    void setUp() {
        hook = new ReactorMdcHook();
        hook.setup();
    }

    @AfterEach
    void tearDown() {
        hook.cleanup();
    }

    @Test
    void shouldCopyCorrelationIdFromContextToMdcDuringOnNext() {
        AtomicReference<String> seen = new AtomicReference<>();

        Mono.just("value")
                .contextWrite(ctx -> ctx.put(LoggingConfig.CORRELATION_ID_MDC_KEY, "cid-123"))
                .doOnNext(v -> seen.set(MDC.get(LoggingConfig.CORRELATION_ID_MDC_KEY)))
                .block();

        assertEquals("cid-123", seen.get());
        // Fuera de onNext debe limpiarse
        assertNull(MDC.get(LoggingConfig.CORRELATION_ID_MDC_KEY));
    }
}