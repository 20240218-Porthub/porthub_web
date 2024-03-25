
package hello.example.porthub.config.auth;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

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
                        .requestMatchers("/mentoring/**").hasRole("MENTO")
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .requestMatchers("/login2").authenticated()
                        .anyRequest().permitAll() //전체 권한 열어놓고 특정 경로들을 요청받음 -> 자잘한 기능도 막힘
                );


        http
                .formLogin((auth) -> auth.loginPage("/login")
                        .defaultSuccessUrl("/main", true)//spring security가 앞단에 로그인 데이타를 넘기면 로그인 처리 진행
                        .permitAll());

        http
                .logout((logout) -> logout
                        .permitAll());


        http
                .csrf((auth) -> auth.disable());// token 잠깐 꺼두기

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

}
