package com.gymtrack.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import net.devh.boot.grpc.server.security.authentication.GrpcAuthenticationReader;
import net.devh.boot.grpc.server.security.authentication.AnonymousAuthenticationReader;

/**
 * gRPC Security configuration.
 * Provides a GrpcAuthenticationReader bean required by the grpc-server-spring-boot-starter
 * when Spring Security is on the classpath.
 *
 * Uses AnonymousAuthenticationReader to allow unauthenticated gRPC calls.
 * In production, replace with BearerAuthenticationReader for JWT-based auth.
 */
@Configuration
public class GrpcSecurityConfig {

    @Bean
    public GrpcAuthenticationReader grpcAuthenticationReader() {
        return new AnonymousAuthenticationReader(
                "grpc-anonymous",
                "anonymousUser",
                List.of(new SimpleGrantedAuthority("ROLE_ANONYMOUS"))
        );
    }
}
