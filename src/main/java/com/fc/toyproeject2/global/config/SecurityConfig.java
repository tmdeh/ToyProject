package com.fc.toyproeject2.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fc.toyproeject2.global.security.filter.JwtRequestFilter;
import com.fc.toyproeject2.global.security.filter.LoginFilter;
import com.fc.toyproeject2.global.util.JwtUtil;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    private final Map<String, String> excludeUrls = Map.of(
        "/sign-up", HttpMethod.POST.name(),
        "/login", HttpMethod.OPTIONS.name(),
        "/error", HttpMethod.OPTIONS.name()
    );

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   AuthenticationConfiguration authenticationConfiguration) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(configurer -> configurer.sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);

        http
            .authorizeHttpRequests(matcher -> matcher
                .requestMatchers("/sign-up/**","/login", "/error").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/trip").permitAll()
                .anyRequest().authenticated());
        http
            .addFilterAfter(new JwtRequestFilter(jwtUtil, objectMapper, excludeUrls), UsernamePasswordAuthenticationFilter.class)
            .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, objectMapper), UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
            throws Exception {
        return configuration.getAuthenticationManager();
    }

}
