package com.tecnouam.Service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class CustomUserDetailsServiceTest {

    @Test
    void conservaLaContrasenaCodificadaDespuesDeBorrarCredencialesDeUnaSesion() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        CustomUserDetailsService service = new CustomUserDetailsService(encoder);

        UserDetails primeraSesion = service.loadUserByUsername("user");
        ((CredentialsContainer) primeraSesion).eraseCredentials();

        UserDetails segundaSesion = service.loadUserByUsername("user");

        assertThat(encoder.matches("user123", segundaSesion.getPassword())).isTrue();
        assertThat(segundaSesion.getAuthorities())
                .extracting(GrantedAuthority::getAuthority)
                .containsExactly("ROLE_USER");
    }
}
