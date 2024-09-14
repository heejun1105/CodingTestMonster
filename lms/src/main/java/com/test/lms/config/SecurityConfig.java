package com.test.lms.config;

import java.io.IOException;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private UserDetailsService userDetailsService;
    private final DataSource dataSource;
    
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, 
                                                        UserDetailsService userDetailsService, 
                                                        BCryptPasswordEncoder passwordEncoder) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder)
                .and()
                .build();
    }

    
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable())  // CSRF 보호를 비활성화합니다. 프로덕션 환경에서는 활성화하는 것이 좋습니다.
            .authorizeHttpRequests(authorizeRequests -> authorizeRequests

                .requestMatchers("/member/signup", "/member/login", "/index", "/css/**", "/js/**", "/", "/api/**").permitAll() 

                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers(new AntPathRequestMatcher("/ai/**")).authenticated()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/member/login")
                .loginProcessingUrl("/api/member/login")
                .successHandler(this::loginSuccessHandler)
                .failureHandler(this::loginFailureHandler)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/api/member/logout")
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.setStatus(HttpStatus.OK.value());
                    response.getWriter().write("{\"message\":\"Logout successful\"}");
                })
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                .maximumSessions(1)
                .expiredUrl("/login?expired")
            )
            .rememberMe(rememberMe -> rememberMe
                    .key("uniqueAndSecretKey")
                    .tokenValiditySeconds(86400) // RemeberMe 7일 설정
                    .userDetailsService(userDetailsService)
                    .tokenRepository(persistentTokenRepository())
            );
        return http.build();
    }

    private void loginSuccessHandler(HttpServletRequest request, HttpServletResponse response, 
                                    Authentication authentication) throws IOException {
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"message\":\"Login successful\", \"username\":\"" 
                                + authentication.getName() + "\"}");
    }

    private void loginFailureHandler(HttpServletRequest request, HttpServletResponse response, 
                                    AuthenticationException exception) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"error\":\"Login failed: " + exception.getMessage() + "\"}");
    }

    @Bean
    public AuthenticationSuccessHandler loginSuccessHandler() {
        return (request, response, authentication) -> {
            System.out.println("로그인 성공");
            response.sendRedirect("/");
        };
    }

    @Bean
    public AuthenticationFailureHandler loginFailureHandler() {
        return (request, response, exception) -> {
            System.out.println("로그인 실패: " + exception.getMessage());
            response.sendRedirect("/member/login?error");
        };
    }
    
    
    // @Bean
    // public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    //     http
    //         .cors((cors) -> cors.and())
    //         .csrf((csrf) -> csrf.disable())
    //         .authorizeHttpRequests(authorizeRequests -> authorizeRequests
    //             .requestMatchers("/member/signup", "/member/login", "/index", "/css/**", "/js/**", "/", "/api/**").permitAll() 
    //             .anyRequest().authenticated()
    //         )
    //         .formLogin(formLogin -> formLogin
    //             .loginPage("/member/login")
    //             .loginProcessingUrl("/member/login")
    //             .permitAll()
    //         )
    //         .logout(logout -> logout
    //             .logoutUrl("/member/logout")
    //             .logoutSuccessUrl("/index") 
    //             .permitAll()
    //         );
    //     return http.build();
    // }
}