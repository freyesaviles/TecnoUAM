package com.tecnouam.JWT;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

class JwtTokenProviderTest {

    private static final String SECRET =
            "test-secret-with-at-least-64-characters-for-hs512-tecnouam-2026-abcdefghijklmnopqrstuvwxyz";

    @Test
    void generaYValidaUnTokenConUsuarioYRol() {
        JwtTokenProvider provider = new JwtTokenProvider(SECRET, 120);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                "admin",
                null,
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );

        String token = provider.generarToken(authentication);

        assertThat(token).isNotBlank();
        assertThat(provider.validarToken(token)).isTrue();
        assertThat(provider.obtenerUsernameDelToken(token)).isEqualTo("admin");
    }

    @Test
    void rechazaUnTokenAlterado() {
        JwtTokenProvider provider = new JwtTokenProvider(SECRET, 120);
        Authentication authentication = new UsernamePasswordAuthenticationToken("user", null);
        String token = provider.generarToken(authentication);

        char ultimoCaracter = token.charAt(token.length() - 1);
        char caracterAlternativo = ultimoCaracter == 'x' ? 'y' : 'x';
        String tokenAlterado = token.substring(0, token.length() - 1) + caracterAlternativo;

        assertThat(provider.validarToken(tokenAlterado)).isFalse();
    }
}
