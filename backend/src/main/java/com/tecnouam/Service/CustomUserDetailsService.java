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

    private final Map<String, UserDetails> usuarios = new HashMap<>();

    public CustomUserDetailsService(PasswordEncoder passwordEncoder) {
        // Usuario estándar de prueba
        usuarios.put("user", User.builder()
                .username("user")
                .password(passwordEncoder.encode("user123"))
                .roles("USER")
                .build());

        // Usuario administrador de prueba
        usuarios.put("admin", User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin123"))
                .roles("ADMIN")
                .build());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (usuarios.containsKey(username)) {
            return usuarios.get(username);
        }
        throw new UsernameNotFoundException("Usuario no encontrado con username: " + username);
    }
}