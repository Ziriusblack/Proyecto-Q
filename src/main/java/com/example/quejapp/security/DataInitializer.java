package com.example.quejapp.security;

import com.example.quejapp.model.Usuario;
import com.example.quejapp.model.repositories.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.Calendar;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initAdminUser(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            String nickname = "admin";

            Usuario existente = usuarioRepository.findByNickname(nickname);
            if (existente == null) {
                Usuario admin = new Usuario();
                admin.setNombre("Admin");
                admin.setApellido("Principal");
                admin.setNickname(nickname);
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRol("ADMINISTRATOR"); // Como tu campo es String
                admin.setGenero(1);

                // Fecha de nacimiento como java.util.Date
                Calendar cal = Calendar.getInstance();
                cal.set(1990, Calendar.JANUARY, 1);
                admin.setFechaNacimiento(cal.getTime());

                usuarioRepository.save(admin);
                System.out.println("✅ Usuario administrador creado.");
            } else {
                System.out.println("ℹ️ Usuario administrador ya existe.");
            }
        };
    }
}
