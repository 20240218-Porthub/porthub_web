package hello.example.porthub.config;


import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class SessionConfig {
    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> containerCustomizer() {
        return factory -> {
            factory.getSession().setTimeout(Duration.ofSeconds(1800)); // 세션 만료 시간을 30분으로 설정
        };
    }
}
