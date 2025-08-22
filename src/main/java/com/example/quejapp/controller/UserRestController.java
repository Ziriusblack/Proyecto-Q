package com.example.quejapp.controller;

import com.example.quejapp.model.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserRestController {

    @GetMapping("/info")
    public ResponseEntity<?> userInfo(Authentication authentication) {
        String nickname = authentication.getName();
        return ResponseEntity.ok("Usuario autenticado: " + nickname);
    }
    @GetMapping("/me")
    public ResponseEntity<?> getUserInfo(Authentication authentication) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        return ResponseEntity.ok(usuario);
    }
}
