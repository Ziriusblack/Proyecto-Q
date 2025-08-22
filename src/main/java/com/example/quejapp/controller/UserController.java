package com.example.quejapp.controller;

import com.example.quejapp.DTOs.ReportDTO;
import com.example.quejapp.model.Queja;
import com.example.quejapp.services.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService servicio;

    public UserController(UserService servicio) {
        this.servicio = servicio;
    }

    @ModelAttribute("quejas")
    public List<ReportDTO> listarQuejas() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String nickname = authentication.getName();
        return servicio.listarQuejas(nickname);
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        String nickname = SecurityContextHolder.getContext().getAuthentication().getName();
        List<ReportDTO> quejas = servicio.listarQuejas(nickname);
        model.addAttribute("quejas", quejas);
        return "DashboardUser";
    }


    @GetMapping("/complaint")
    public String crearQueja(Model model) {
        model.addAttribute("queja", new Queja());
        return "QuejaFormulario";
    }

    @PostMapping("/complaint")
    public String guardarQueja(
            @Valid @ModelAttribute("queja") Queja queja,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "QuejaFormulario";
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String nickname = authentication.getName();

        Queja nuevaQueja = servicio.createComplaintForUser(queja);
        model.addAttribute("queja", nuevaQueja);
        return "QuejaCompletada";
    }


    @GetMapping("/complaint/details/{id}")
    public String verDetalleQueja(@PathVariable String id, Model model) {
        ReportDTO detalle = servicio.obtenerDetalleQueja(id);
        if (detalle == null) {
            return "redirect:/dashboard?error=notfound";
        }
        model.addAttribute("reporte", detalle);
        return "quejadetalles";
    }


}
