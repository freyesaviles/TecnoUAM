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

        String[] segmentos = token.split("\\.");
        char primerCaracter = segmentos[1].charAt(0);
        char caracterAlternativo = primerCaracter == 'a' ? 'b' : 'a';
        segmentos[1] = caracterAlternativo + segmentos[1].substring(1);
        String tokenAlterado = String.join(".", segmentos);

        assertThat(provider.validarToken(tokenAlterado)).isFalse();
    }
}
