package com.tecnouam.JWT;

import com.tecnouam.Service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final CustomUserDetailsService customUserDetailsService;

    public JwtAuthenticationFilter(JwtTokenProvider tokenProvider, @Lazy CustomUserDetailsService customUserDetailsService) {
        this.tokenProvider = tokenProvider;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String token = obtenerJwtDelRequest(request);

            // Log para ver si el token llega al backend
            if (token != null) {
                System.out.println(" Token recibido en el backend: " + token.substring(0, 15) + "...");
            } else {
                System.out.println("⚠️ No se recibió token en la ruta: " + request.getRequestURI());
            }

            if (StringUtils.hasText(token) && tokenProvider.validarToken(token)) {
                String username = tokenProvider.obtenerUsernameDelToken(token);
                System.out.println(" Token válido. Usuario extraído: " + username);

                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println(" Autenticación establecida en el contexto de seguridad.");
            }
        } catch (Exception ex) {
            System.out.println(" ERROR EN EL FILTRO JWT: " + ex.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private String obtenerJwtDelRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        // Log para ver la cabecera cruda
        if (bearerToken != null) {
            System.out.println(" Cabecera Authorization recibida: " + bearerToken.substring(0, Math.min(bearerToken.length(), 20)) + "...");
        }

        if (StringUtils.hasText(bearerToken) && bearerToken.toLowerCase().startsWith("bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}