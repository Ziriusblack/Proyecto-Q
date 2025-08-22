package com.example.quejapp.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
public class RedirectController {

    @GetMapping("/redirect")
    public void redirectByRole(HttpServletResponse response, Authentication authentication) throws IOException {
        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("");

        if ("ROLE_ADMINISTRATOR".equals(role)) {
            response.sendRedirect("/attendant/dashboard");
        } else if ("ROLE_USER".equals(role)) {
            response.sendRedirect("/dashboard");
        } else {
            response.sendRedirect("/access-denied");
        }
    }
}
