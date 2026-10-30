package com.tecnouam.Service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final Map<String, String> contrasenasCodificadas = new HashMap<>();

    public CustomUserDetailsService(PasswordEncoder passwordEncoder) {
        contrasenasCodificadas.put("user", passwordEncoder.encode("user123"));
        contrasenasCodificadas.put("admin", passwordEncoder.encode("admin123"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String contrasenaCodificada = contrasenasCodificadas.get(username);
        if (contrasenaCodificada != null) {
            String rol = "admin".equals(username) ? "ADMIN" : "USER";

            // Se devuelve una instancia nueva porque Spring Security puede
            // borrar las credenciales del UserDetails autenticado.
            return User.builder()
                    .username(username)
                    .password(contrasenaCodificada)
                    .roles(rol)
                    .build();
        }
        throw new UsernameNotFoundException("Usuario no encontrado con username: " + username);
    }
}
