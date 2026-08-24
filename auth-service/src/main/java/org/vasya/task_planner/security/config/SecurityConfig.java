package org.vasya.task_planner.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.RequestCacheConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.*;
import org.springframework.security.web.session.DisableEncodeUrlFilter;
import org.vasya.task_planner.config.logging.MdcLoggingFilter;
import org.vasya.task_planner.security.authentication.jwt.JWTAuthFilter;
import org.vasya.task_planner.security.authentication.jwt.JWTLogoutHandler;
import org.vasya.task_planner.security.authentication.rest.RESTAuthenticatedEntryPoint;
import org.vasya.task_planner.security.authentication.rest.RESTJSONAuthFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    @Primary
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    @Primary
    public AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {

        return cfg.getAuthenticationManager();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   MdcLoggingFilter mdcLoggingFilter,
                                                   JWTAuthFilter JWTAuthFilter,
                                                   RESTJSONAuthFilter RESTJSONAuthFilter,
                                                   JWTLogoutHandler jwtLogoutHandler,
                                                   RESTAuthenticatedEntryPoint restAuthenticatedEntryPoint) throws Exception {

        return http
                .csrf(AbstractHttpConfigurer::disable)
//                .cors(cors -> cors.configurationSource(request -> {
//                    var config = new CorsConfiguration();
//                    config.setAllowedOrigins(List.of(nginx));
//                    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//                    config.setAllowedHeaders(List.of(HttpHeaders.CONTENT_TYPE, HttpHeaders.ACCEPT,
//                            HttpHeaders.CONTENT_DISPOSITION, "X-Requested-With", HttpHeaders.AUTHORIZATION));
//                    config.setAllowCredentials(true);
//
//                    var source = new UrlBasedCorsConfigurationSource();
//                    source.registerCorsConfiguration("/**", config);
//
//                    return source;
//                }))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .securityContext(context ->
                        context.securityContextRepository(
                                new NullSecurityContextRepository()
                        )
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST,
                                "/auth/registration",
                                "/auth/login").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers(HttpMethod.GET,
                                "/auth/user", "/tasks").authenticated()
                        .requestMatchers(HttpMethod.POST, "/tasks").authenticated()
                        .requestMatchers(HttpMethod.POST,
                                "/auth/logout").authenticated()
                        .anyRequest().authenticated())
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .addFilterBefore(mdcLoggingFilter, DisableEncodeUrlFilter.class)
                .addFilterBefore(JWTAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(RESTJSONAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .requestCache(RequestCacheConfigurer::disable)
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .addLogoutHandler(jwtLogoutHandler))
                .exceptionHandling(
                        ex -> ex.authenticationEntryPoint(restAuthenticatedEntryPoint)
                )
                .build();
    }
}