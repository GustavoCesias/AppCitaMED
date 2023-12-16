package com.cesi.citamed.controller;

import com.cesi.citamed.controller.request.CreateUserDTO;
import com.cesi.citamed.model.Erole;
import com.cesi.citamed.model.RoleEntity;
import com.cesi.citamed.model.UserEntity;
import com.cesi.citamed.repositories.UserRepository;
import com.cesi.citamed.security.jwt.JwtUtils;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@Controller
public class PrincipalController {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/home")
    public String home(){
        return "home";
    }

    @GetMapping("/signup")
    @PreAuthorize("permitAll()")
    public String showUserForm(Model model) {
        // Add an empty CreateUserDTO object to the model for Thymeleaf binding
        model.addAttribute("createUserDTO", new CreateUserDTO());
        return "signup";
    }

    @PostMapping("/signup")
    public String createUser(@Valid @ModelAttribute CreateUserDTO createUserDTO, BindingResult result) {
        if (result.hasErrors()) {
            return "home";
        }

        RoleEntity userRole = RoleEntity.builder()
                .name(Erole.USER)
                .build();

        Set<RoleEntity> roles = new HashSet<>();
        roles.add(userRole);

        UserEntity userEntity = UserEntity.builder()
                .name(createUserDTO.getName())
                .lastname(createUserDTO.getLastname())
                .email(createUserDTO.getEmail())
                .password(passwordEncoder.encode(createUserDTO.getPassword()))
                .roles(roles)
                .build();

        userRepository.save(userEntity);

        return "redirect:/home";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String processLogin() {
        return "home"; // Redirigir a la página de éxito después de la autenticación exitosa
    }



}
