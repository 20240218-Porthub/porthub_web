
package hello.example.porthub.config.auth;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    final private AuthenticationFailureHandler failureHandler;

    public SecurityConfig(AuthenticationFailureHandler failureHandler) {
        this.failureHandler = failureHandler;
    }

    //회원 암호화 진행
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //특정한 경로의 접근을 제한
        http
                .authorizeHttpRequests((auth) -> auth
//                        .requestMatchers("/mentoring/createmento/**").hasRole("MENTO")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/ports/views/put/**", "/ports/views/report","/order/**","/payment/**","/mentoring/registermento/apply").authenticated()
                        .requestMatchers("/ports/views/delete/**","/ports/views/follow/**", "/ports/views/unfollow/**","/ports/uploads","/ports/create").authenticated()
                        .requestMatchers("/user/chat/**","mentoring/activity").authenticated()
                        .anyRequest().permitAll() //전체 권한 열어놓고 특정 경로들을 요청받음 -> 자잘한 기능도 막힘
                );
        http.formLogin((auth) -> auth
                .loginPage("/login")
                .failureHandler(failureHandler)
                .defaultSuccessUrl("/main", true)
                .permitAll()
        );

        http.logout((logout) -> logout
                .logoutSuccessHandler(logoutSuccessHandler())
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutSuccessUrl("/logout-success")
                .permitAll()
        );

        http.sessionManagement((session) -> session
                .invalidSessionUrl("/session-expired")
                .maximumSessions(1)
                .maxSessionsPreventsLogin(true)
        );

        http.csrf((auth) -> auth.disable()); // CSRF token 잠깐 꺼두기

        return http.build();
    }

    //security로 인해 css 적용 안되는 부분이 있을 경우 해제 시킴

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers(PathRequest
                        .toStaticResources()
                        .atCommonLocations());
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return (request, response, authentication) -> {
            response.sendRedirect("/logout-success");
        };
    }

    @Bean
    public ServletListenerRegistrationBean<HttpSessionEventPublisher> HttpSessionEventPublisher() {
        return new ServletListenerRegistrationBean<HttpSessionEventPublisher>(new HttpSessionEventPublisher());
    }

}
