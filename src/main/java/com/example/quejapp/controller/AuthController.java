package com.example.quejapp.controller;

import com.example.quejapp.DTOs.LoginRequest;
import com.example.quejapp.security.JwtUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        System.out.println(loginRequest.getNickname());
        System.out.println(loginRequest.getPassword());
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getNickname(),
                            loginRequest.getPassword()
                    )
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            String role = userDetails.getAuthorities().stream()
                    .findFirst()
                    .map(GrantedAuthority::getAuthority)
                    .orElse("USER")  // valor por defecto
                    .replace("ROLE_", "");

            String jwt = jwtUtils.generateJwtToken(userDetails.getUsername(), role);

//            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//            String jwt = jwtUtils.generateJwtToken(userDetails.getUsername());

            // 🔐 Crear cookie
            ResponseCookie jwtCookie = ResponseCookie.from("jwt", jwt)
                    .httpOnly(true)
                    .secure(false) // cambia a true si usas HTTPS
                    .path("/")
                    .maxAge(24 * 60 * 60) // 1 día
                    .sameSite("Strict")
                    .build();



            // ⬅️ Retornar cookie + body opcional
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                    .body("Login correcto");


    }catch (Exception e)

    {
        System.out.println("Error");
        System.out.println(e);
        return ResponseEntity.badRequest().body(e.getMessage());
    }



    }
}
