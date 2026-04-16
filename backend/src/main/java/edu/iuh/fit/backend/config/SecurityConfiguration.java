/*
 * @ (#) .java    1.0
 * Copyright (c)  IUH. All rights reserved.
 */
package edu.iuh.fit.backend.config;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.util.Base64;
import edu.iuh.fit.backend.util.SecurityUtil;
import jakarta.servlet.DispatcherType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/*
 * @description
 * @author: Huu Thai
 * @date:
 * @version: 1.0
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfiguration {
    @Value("${thai.jwt.base64-secret}")
    private String jwtKey;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   CustomAuthenticationEntryPoint customAuthenticationEntryPoint) throws Exception {
        String[] whiteList = {
                "/", "/api/v1/auth/register",
                "/api/v1/auth/login", "/api/v1/auth/refresh",
                "/storage/**", "/api/v1/email/**",
        };
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(whiteList).permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/companies").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/jobs").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/skills").permitAll()
                        .anyRequest()
                        .authenticated())
//                Dung de goi ra CORS filter
                .cors(Customizer.withDefaults())
//              Dung de them filter BearerTokenAuthentication de decode
                .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults())
//                        Dùng để handle lỗi 401 khi token sai hoặc token hết hạn
                        .authenticationEntryPoint(customAuthenticationEntryPoint))
//              Dùng để handle:
//              - 401 khi chưa có token hoặc không gửi token
//              - 403 khi có token hợp lệ nhưng không đủ quyền truy cập
//                .exceptionHandling(
//                        exceptions -> exceptions
//                                .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint()) //401
//                                .accessDeniedHandler(new BearerTokenAccessDeniedHandler())) //403
                .formLogin(f -> f.disable())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))//403
        ;
        return http.build();
    }
    private SecretKey getSecretKey() {
        byte[] keyBytes = Base64.from(jwtKey).decode();
        return new SecretKeySpec(keyBytes, 0, keyBytes.length,
                SecurityUtil.JWT_ALGORITHM.getName());
    }
//    Dung de tao token
    @Bean
    public JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(getSecretKey()));
    }
//    Dung de giai mai token
    @Bean
    public JwtDecoder jwtDecoder() {
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(
                getSecretKey()).macAlgorithm(SecurityUtil.JWT_ALGORITHM).build();
        return token -> {
            try {
                return jwtDecoder.decode(token);
            } catch (Exception e) {
                System.out.println(">>> JWT error: " + e.getMessage());
                throw e;
            }
        };
    }
//    Dung de phan quyen
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix("");
        grantedAuthoritiesConverter.setAuthoritiesClaimName("permission ");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }
}

