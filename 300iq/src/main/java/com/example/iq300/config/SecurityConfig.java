package com.example.iq300.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

@Configuration
@EnableWebSecurity // Spring Security를 활성화합니다.
public class SecurityConfig {

    @Bean // Spring이 관리하는 객체로 등록
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 1. 페이지 접근 권한 설정
            .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                // "/" (메인), "/board/list", "/css/**", "/js/**" 등 정적 파일은 모두 허용
                .requestMatchers("/", "/board/list", "/css/**", "/js/**", "/img/**", "/vendor/**").permitAll() 
                // "/user/signup" (회원가입) 주소는 모두 허용
                .requestMatchers("/user/signup").permitAll()
                // "/h2-console/**" 도 임시로 허용 (개발용)
                .requestMatchers("/h2-console/**").permitAll() 
                // 그 외의 모든 요청은 "인증"(로그인)된 사용자만 접근 가능
                .anyRequest().authenticated() 
            )
            // 2. H2 콘솔 사용을 위한 설정 (개발용)
            .csrf((csrf) -> csrf
                .ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**"))
            )
            .headers((headers) -> headers
                .frameOptions((frameOptions) -> frameOptions.sameOrigin())
            )
            // 3. 로그인 설정
            .formLogin((formLogin) -> formLogin
                .loginPage("/user/login") // 로그인 폼 페이지 주소
                .defaultSuccessUrl("/")
                .permitAll()
            )
            // 4. 로그아웃 설정
            .logout((logout) -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout")) // 로그아웃 주소
                .logoutSuccessUrl("/") // 로그아웃 성공 시 이동할 주소
                .invalidateHttpSession(true) // 세션 무효화
            );
            
        return http.build();
    }

    @Bean
    // 3. 비밀번호 암호화 객체 (BCrypt)
    // 이 객체를 통해 비밀번호를 암호화/비교해야 합니다.
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    // 4. Spring Security의 인증을 담당하는 객체
    // UserSecurityService와 PasswordEncoder를 사용하도록 설정
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}