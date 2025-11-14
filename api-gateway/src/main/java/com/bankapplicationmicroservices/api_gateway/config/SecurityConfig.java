package com.bankapplicationmicroservices.api_gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Value("${jwt.secret}")
    private String secret;


    private Converter<Jwt, Mono<AbstractAuthenticationToken>> jwtAuthConverter() {
        return jwt -> {
            List<String> roles = jwt.getClaimAsStringList("roles");
            if (roles == null) {
                roles = jwt.getClaimAsStringList("authorities");
            }
            if (roles == null) {
                Map<String, Object> realmAccess = jwt.getClaim("realm_access");
                if (realmAccess != null) {
                    Object raRoles = realmAccess.get("roles");
                    if (raRoles instanceof Collection<?>) {
                        roles = ((Collection<?>) raRoles).stream()
                                .filter(String.class::isInstance)
                                .map(String.class::cast)
                                .collect(Collectors.toList());
                    }
                }
            }
            if (roles == null) roles = List.of();

            var authorities = roles.stream()
                    .map(r -> r.startsWith("ROLE_") ? r : "ROLE_" + r)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            return Mono.just(new JwtAuthenticationToken(jwt, authorities, jwt.getSubject()));
        };
    }

    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        var key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        return NimbusReactiveJwtDecoder
                .withSecretKey(key)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.csrf(ServerHttpSecurity.CsrfSpec::disable);
        http.httpBasic(ServerHttpSecurity.HttpBasicSpec::disable);
        http.formLogin(ServerHttpSecurity.FormLoginSpec::disable);

        http.authorizeExchange(ex -> ex
                .pathMatchers("/security-service/**").permitAll()

                .pathMatchers(org.springframework.http.HttpMethod.GET, "/customer-service/customers/all").hasRole("ADMIN")
                .pathMatchers(org.springframework.http.HttpMethod.DELETE, "/customer-service/customers/*/delete").hasRole("ADMIN")
                .pathMatchers("/customer-service/customers/**").hasAnyRole("USER", "ADMIN")

                .pathMatchers(org.springframework.http.HttpMethod.GET, "/bank-service/accounts/all").hasRole("ADMIN")
                .pathMatchers(org.springframework.http.HttpMethod.DELETE, "/bank-service/accounts/*/delete").hasRole("ADMIN")
                .pathMatchers(org.springframework.http.HttpMethod.GET, "/bank-service/accounts/report/topaccounts").hasRole("ADMIN")
                .pathMatchers(org.springframework.http.HttpMethod.GET, "/bank-service/accounts/by-type").hasRole("ADMIN")
                .pathMatchers("/bank-service/accounts/**").hasAnyRole("USER", "ADMIN")

                .pathMatchers(org.springframework.http.HttpMethod.GET, "/transaction-service/trans/all").hasRole("ADMIN")
                .pathMatchers("/transaction-service/trans/**").hasAnyRole("USER", "ADMIN")

                .anyExchange().authenticated()
        );

        http.oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter()))
        );



        return http.build();
    }
}
