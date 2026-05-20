package x10.Clothing.api.config.jwt;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import x10.Clothing.api.common.domain.dto.request.TokenPayload;
import x10.Clothing.api.config.redis.IRedisService;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final IJwtService jwtService;
    private final IRedisService redisService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        // Không có token
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = authHeader.substring(7);

        try {

            // Kiểm tra token có trong blacklist không
            if (redisService.isTokenBlacklisted(token)) {
                log.warn("Attempt to use blacklisted token");
                filterChain.doFilter(request, response);
                return;
            }

            // Validate access token và lấy TokenPayload
            TokenPayload tokenPayload = jwtService.validateAccessToken(token);

            // Kiểm tra xem đã có authentication chưa
            if (tokenPayload.getUsername() != null &&
                    SecurityContextHolder.getContext().getAuthentication() == null) {

                // Tạo Authentication object với role từ token
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                tokenPayload.getUserId(),
                                null,
                                List.of(new SimpleGrantedAuthority("ROLE_" + tokenPayload.getRole()))
                        );



                authentication.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                // Set authentication vào SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } catch (Exception e) {
            log.warn("JWT token validation failed: {}", e.getMessage());
            // Token invalid -> bỏ qua, không throw exception
        }

        filterChain.doFilter(request, response);
    }
}
