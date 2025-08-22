package com.example.quejapp.controller;

import com.example.quejapp.model.Usuario;
import com.example.quejapp.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
public class PublicController {
    private final UserService servicio;
    private final PasswordEncoder passwordEncoder;

    public PublicController(UserService servicio, PasswordEncoder passwordEncoder) {
        this.servicio = servicio;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String login() {
        return "Login";
    }
    @RequestMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("registro", new Usuario());
        return "SignUp";
    }

    @PostMapping("/signup")
    public ResponseEntity<?> saveNewUser (
            @Valid @ModelAttribute("registro") Usuario registro,
            BindingResult bindingResult,
            Model model
    ) {
        System.out.println("Test Creatintin2");
        Usuario nuevoUsuario = servicio.CreateUser(registro);
        System.out.println("Test Creatintin3");
        model.addAttribute("registro", nuevoUsuario);
        System.out.println(nuevoUsuario.getId());
        System.out.println(nuevoUsuario.getNickname());
        return ResponseEntity.status(302).header("Location", "/registro1 ").build();
    }

    @RequestMapping("/access-denied")
    public String error(Principal principal, Model model) {
        model.addAttribute("link",  servicio.returnHomeLinkByRole(principal));
        return "ErrorAccessDenied";
    }

    @RequestMapping("/dashboard")
    public String redireccionarDashboard(Principal principal) {
        return "redirect:" + servicio.returnHomeLinkByRole(principal);
    }

    @PostMapping("/logoutUser")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {

        // 1. Limpiar el SecurityContext
        SecurityContextHolder.clearContext();

        // 2. Invalidar la sesión
        request.getSession().invalidate();

        // 3. Eliminar la cookie del JWT
        Cookie jwtCookie = new Cookie("jwt", null);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(0); // Esto la borra
        response.addCookie(jwtCookie);

        // 4. Redirigir al login o al homepage
        //return "redirect:/login";
        return ResponseEntity.status(302).header("Location", "/registro").build();
    }


    //Testing para mensaje para error 500
    @RequestMapping("/test")
    public String dashboard() {
        throw new RuntimeException("Excepción controlada");
    }
}
