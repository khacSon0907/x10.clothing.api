package x10.Clothing.api.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import x10.Clothing.api.config.jwt.JwtAuthenticationFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @Value("${app.frontend-url:https://polo-man.vercel.app}")
    private String frontendUrl;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler
    ) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.oAuth2LoginSuccessHandler = oAuth2LoginSuccessHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of(
                "http://localhost:5173",
                "https://polo-man.vercel.app"
        ));

        config.setAllowedMethods(List.of(
                "GET",
                "POST",
                "PUT",
                "DELETE",
                "PATCH",
                "OPTIONS"
        ));

        config.setAllowedHeaders(List.of(
                "Authorization",
                "Content-Type",
                "Accept",
                "Origin",
                "X-Requested-With"
        ));

        config.setExposedHeaders(List.of(
                "Authorization",
                "Content-Type",
                "Set-Cookie"
        ));

        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())

                // OAuth2 Google cần session tạm để lưu state khi redirect qua Google
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )

                .authorizeHttpRequests(auth -> auth

                        // Cho phép preflight request của CORS
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Swagger / OpenAPI
                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // Google OAuth2 endpoints của Spring Security
                        .requestMatchers(
                                "/oauth2/**",
                                "/login/oauth2/**"
                        ).permitAll()

                        // Auth public APIs
                        .requestMatchers(
                                "/api/auth/login",
                                "/api/auth/register",
                                "/api/auth/verify-otp",
                                "/api/auth/forgot-password",
                                "/api/auth/verify-forgot-password-otp",
                                "/api/auth/reset-password",
                                "/api/auth/refresh"
                        ).permitAll()

                        // Categories public read
                        .requestMatchers(HttpMethod.GET, "/api/categories/**")
                        .permitAll()

                        // Categories create/update/delete chỉ ADMIN
                        .requestMatchers(HttpMethod.POST, "/api/categories")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/categories/**")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/categories/**")
                        .hasRole("ADMIN")

                        // Products public read
                        .requestMatchers(HttpMethod.GET, "/api/products/**")
                        .permitAll()

                        // Products create/update/delete chỉ ADMIN
                        .requestMatchers(HttpMethod.POST, "/api/products")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/products/**")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/products/**")
                        .hasRole("ADMIN")

                        // Các API auth còn lại cần login
                        .requestMatchers("/api/auth/**")
                        .authenticated()

                        // User API cần login
                        .requestMatchers(HttpMethod.GET, "/api/users/me")
                        .authenticated()

                        .requestMatchers(HttpMethod.PUT, "/api/users/me")
                        .authenticated()

                        // Chỉ ADMIN xem toàn bộ user
                        .requestMatchers(HttpMethod.GET, "/api/users")
                        .hasRole("ADMIN")

                        // Những request còn lại cần login
                        .anyRequest()
                        .authenticated()
                )

                // Bật Google OAuth2 Login
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(oAuth2LoginSuccessHandler)
                        .failureUrl(getFrontendUrl() + "/login?error=oauth2")
                )

                // JWT filter cho các API dùng Bearer token
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    private String getFrontendUrl() {
        if (StringUtils.hasText(frontendUrl)) {
            return frontendUrl;
        }

        return "https://polo-man.vercel.app";
    }
}