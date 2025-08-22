package com.example.quejapp.util;

import com.example.quejapp.model.Rol;
import com.example.quejapp.security.JwtUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;


/*@Component
public class CustomAuthenticationSuccesHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String redirectURL = request.getContextPath();

        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_"+ Rol.ADMINISTRATOR.name()))) {
            redirectURL = "/attendant/dashboard";
        } else if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_"+ Rol.USER.name()))) {
            redirectURL = "/user/dashboard";
        }

        response.sendRedirect(redirectURL);
    }
}*/


@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtils jwtUtils;

    public CustomAuthenticationSuccessHandler(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        String username = authentication.getName();
        String authority = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("ROLE_USER");

        System.out.println("✅ Usuario autenticado: " + username);
        System.out.println("✅ Rol autenticado: " + authority);

        // Genera token con rol sin el prefijo "ROLE_"
        String roleForToken = authority.replace("ROLE_", "");
        String token = jwtUtils.generateJwtToken(username, roleForToken);

        // Guarda token en cookie
        Cookie jwtCookie = new Cookie("jwt", token);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(86400); // 1 día
        response.addCookie(jwtCookie);

        // Establece autenticación en el contexto de seguridad (opcional)
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Redirecciona según el rol
        if ("ROLE_ADMINISTRATOR".equals(authority)) {
            response.sendRedirect("/attendant/dashboard");
        } else if ("ROLE_USER".equals(authority)) {
            response.sendRedirect("/dashboard");
        } else {
            response.sendRedirect("/access-denied");
        }
    }
}

