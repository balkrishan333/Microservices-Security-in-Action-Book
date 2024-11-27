package com.manning.mss.ch04.sample03.resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
@Configuration
@EnableReactiveMethodSecurity
public class SecurityConfig {

    // @Override
    // public void configure(HttpSecurity http) throws Exception {
    //     http.anonymous().and().
    //             authorizeRequests().antMatchers("/.well-known/**").permitAll().
    //             and().authorizeRequests().antMatchers(HttpMethod.OPTIONS, "/*").permitAll().
    //             anyRequest().authenticated();

    // }

    // @Bean
    // public SecurityFilterChain springSecurityFilterChain(HttpSecurity http) throws Exception {

    //     http
    //     .authorizeHttpRequests(authz -> authz
    //         .requestMatchers("/.well-known/**").permitAll()  // Public endpoint
    //         .requestMatchers(HttpMethod.OPTIONS, "/*").permitAll()  // Allow all OPTIONS requests
    //         .anyRequest().authenticated()  // All other requests need authentication
    //     )
    //     .csrf(csrf -> csrf.disable()); // Optional: Disable CSRF if not needed for APIs

    // return http.build();
    // }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
            .authorizeExchange(authz -> authz
                .pathMatchers("/.well-known/**").permitAll()  // Public endpoint
                .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()  // Allow all OPTIONS requests
                .anyExchange().authenticated()  // All other requests need authentication
            )
            .csrf(csrf -> csrf.disable()) // Optional: Disable CSRF if not needed for APIs
            .build();
    }

}
