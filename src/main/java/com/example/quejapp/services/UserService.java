package com.example.quejapp.services;

import com.example.quejapp.DTOs.ReportDTO;
import com.example.quejapp.model.Queja;
import com.example.quejapp.model.Rol;
import com.example.quejapp.model.Usuario;
import com.example.quejapp.model.repositories.QuejaRepository;
import com.example.quejapp.model.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private final QuejaRepository repositorioDeQuejas;
    private final UsuarioRepository repositorioDeUsuarios;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            QuejaRepository repositorioQuejas,
            UsuarioRepository repositorioDeUsuarios,
            PasswordEncoder passwordEncoder
    ) {
        this.repositorioDeQuejas = repositorioQuejas;
        this.repositorioDeUsuarios = repositorioDeUsuarios;
        this.passwordEncoder = passwordEncoder;
    }
    public List<ReportDTO> listarQuejas(String nickname) {
        Usuario usuario = repositorioDeUsuarios.findByNickname(nickname);
        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado");
        }

        List<Queja> quejas = repositorioDeQuejas.findByUsuarioIdOrderByFechaDesc(usuario.getId());
        return quejas.stream().map(q ->
                new ReportDTO(
                        q.getId(), // ← Este cambio es clave
                        q.getFecha(),
                        null, // nombres
                        null, // apellidos
                        q.getDescripcion(),
                        q.getTipoQueja(),
                        q.getUbicacion(),
                        q.getFechaRespuesta(),
                        q.getEstado(),
                        q.getRespuesta()
                )

        ).toList();
    }


    public Queja createComplaintForUser(Queja queja) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String nickname = auth.getName();
        System.out.println("🔐 Nickname extraído del token: " + nickname);

        Usuario usuario = repositorioDeUsuarios.findByNickname(nickname);
        if (usuario == null) {
            System.out.println("❌ Usuario no encontrado en la base de datos.");
            throw new RuntimeException("Usuario no encontrado con nickname: " + nickname);
        }

        queja.setEstado(1);
        queja.setUsuarioId(usuario.getId());
        return repositorioDeQuejas.save(queja);
    }


    public ReportDTO obtenerDetalleQueja(String id) {
        Optional<Queja> quejaOpt = repositorioDeQuejas.findById(id);

        if (quejaOpt.isEmpty()) {
            System.out.println("❌ Queja no encontrada con ID: " + id);
            throw new RuntimeException("Queja no encontrada con ID: " + id);
        }

        Queja q = quejaOpt.get();

        // Log de la queja
        System.out.println("📄 Queja encontrada: " + q.getDescripcion());
        System.out.println("👤 Usuario ID asociado: " + q.getUsuarioId());

        Usuario usuario = repositorioDeUsuarios.findById(q.getUsuarioId()).orElse(null);

        // Log del usuario
        if (usuario != null) {
            System.out.println("✅ Usuario encontrado: " + usuario.getNombre() + " " + usuario.getApellido());
        } else {
            System.out.println("⚠️ Usuario no encontrado para ID: " + q.getUsuarioId());
        }

        return new ReportDTO(
                q.getId(), // ahora agregamos el ID
                q.getFecha(),
                usuario != null ? usuario.getNombre() : "Desconocido",
                usuario != null ? usuario.getApellido() : "",
                q.getDescripcion(),
                q.getTipoQueja(),
                q.getUbicacion(),
                q.getFechaRespuesta(),
                q.getEstado(),
                q.getRespuesta()
        );
    }



    public Usuario CreateUser(Usuario formulario) {
        System.out.println("Creating user");
        formulario.setRol(Rol.USER.name());
        formulario.setPassword(passwordEncoder.encode(formulario.getPassword()));
        return repositorioDeUsuarios.save(formulario);
    }

    public String buscarRolDeUsuarioPorNickname(String nickname) {
        Usuario usuario = repositorioDeUsuarios.findByNickname(nickname);
        return usuario.getRol();
    }

    public String returnHomeLinkByRole(Principal session) {
        if (session == null) {
            return "/";
        }
        String rol = buscarRolDeUsuarioPorNickname(session.getName());
        System.out.println("Rol encontrado: " + rol); // Agrega esto
        if (rol.equalsIgnoreCase("ADMINISTRATOR")) {
            return "/attendant/dashboard";
        }

        return "/user/dashboard";
    }

}
