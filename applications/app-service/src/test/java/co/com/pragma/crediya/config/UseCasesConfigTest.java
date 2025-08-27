package co.com.pragma.crediya.config;

import co.com.pragma.crediya.model.user.User;
import co.com.pragma.crediya.model.user.gateways.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UseCasesConfigTest {

    @Test
    void testUseCaseBeansExist() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestConfig.class)) {
            String[] beanNames = context.getBeanDefinitionNames();

            boolean useCaseBeanFound = false;
            for (String beanName : beanNames) {
                if (beanName.endsWith("UseCase")) {
                    useCaseBeanFound = true;
                    break;
                }
            }

            assertTrue(useCaseBeanFound, "No beans ending with 'Use Case' were found");
        }
    }

    @Configuration
    @Import(UseCasesConfig.class)
    static class TestConfig {

        // Stub repository to satisfy UseCase constructor dependencies discovered by @ComponentScan
        @Bean
        public UserRepository userRepository() {
            return new UserRepository() {
                @Override
                public Mono<User> saveUser(User user) { return Mono.just(user); }
                @Override
                public Mono<Boolean> existsByCorreoElectronico(String correoElectronico) { return Mono.just(false); }
            };
        }

        @Bean
        public MyUseCase myUseCase() {
            return new MyUseCase();
        }
    }

    static class MyUseCase {
        public String execute() {
            return "MyUseCase Test";
        }
    }
}