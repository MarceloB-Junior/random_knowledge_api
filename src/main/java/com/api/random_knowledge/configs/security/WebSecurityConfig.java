package com.api.random_knowledge.configs.security;

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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST,"users/sign-up").permitAll()
                        .requestMatchers(HttpMethod.POST,"auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST,"auth/refresh-token").hasRole("USER")

                        .requestMatchers(HttpMethod.POST,"categories").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"categories").hasRole("USER")
                        .requestMatchers(HttpMethod.GET,"categories/{id}").hasRole("USER")
                        .requestMatchers(HttpMethod.GET,"categories/{id}/curiosities").permitAll()
                        .requestMatchers(HttpMethod.PUT,"categories/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"categories/{id}").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST,"curiosities").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"curiosities").hasRole("USER")
                        .requestMatchers(HttpMethod.GET,"curiosities/random").permitAll()
                        .requestMatchers(HttpMethod.GET,"curiosities/{id}").hasRole("USER")
                        .requestMatchers(HttpMethod.PUT,"curiosities/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"curiosities/{id}").hasRole("ADMIN")

                        .requestMatchers("/swagger-ui/**","/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration
                                                               authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}