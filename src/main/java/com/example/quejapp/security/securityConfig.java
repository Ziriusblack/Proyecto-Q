package com.example.quejapp.security;

import com.example.quejapp.model.Rol;
import com.example.quejapp.services.CustomUserDetailsService;
import com.example.quejapp.util.CustomAccessDeniedHandler;
import com.example.quejapp.util.CustomAuthenticationSuccessHandler;
import jakarta.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

@Configuration
@EnableWebSecurity
public class securityConfig {

    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService userDetailsService;

    public securityConfig(JwtUtils jwtUtils, CustomUserDetailsService userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;}

        public String[] pathsToStaticResources = {
            "/resources/**", "/static/**", "/css/**", "/assets/**",
            "/vendor/**", "/fonts/**", "/static/favicon.ico", "/favicon.ico"
    };

    public String[] pathsToStaticRoutes = {
            "/", "/home", "/signup", "/registro1", "/redirect","/access-denied"
    };
    @Autowired
    private JwtCookieAuthenticationFilter jwtCookieAuthenticationFilter;

    @Autowired
    private CustomAuthenticationSuccessHandler successHandler;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        JwtAuthenticationFilter jwtFilter = new JwtAuthenticationFilter(jwtUtils,userDetailsService);

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .authorizeHttpRequests(auth -> auth
                        // Permitir rutas de login y otras públicas
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/logout").permitAll()
                        .requestMatchers(pathsToStaticRoutes).permitAll()
                        .requestMatchers(pathsToStaticResources).permitAll()

                        .requestMatchers("/user/**").hasAuthority("ROLE_USER")
                        // .requestMatchers("/user/**").hasRole("USER")

                        .requestMatchers("/attendant/**").hasAuthority("ROLE_ADMINISTRATOR")

                       // .requestMatchers("/attendant/**").hasRole(Rol.ADMINISTRATOR.name())
                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/login")

                        .successHandler(successHandler)
                        //.defaultSuccessUrl("/dashboard", true)
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )


                .exceptionHandling(ex -> ex.accessDeniedHandler(new CustomAccessDeniedHandler()))

                .addFilterBefore(jwtCookieAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
               .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}



