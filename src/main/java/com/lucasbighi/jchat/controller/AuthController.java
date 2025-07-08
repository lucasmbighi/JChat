package com.lucasbighi.jchat.controller;

import com.lucasbighi.jchat.dto.AuthRequest;
import com.lucasbighi.jchat.dto.AuthResponse;
import com.lucasbighi.jchat.model.User;
import com.lucasbighi.jchat.repository.UserRepository;
import com.lucasbighi.jchat.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired private UserRepository userRepo;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private JwtService jwtService;
    @Autowired private UserDetailsService userDetailsService;

    @GetMapping("/check-email")
    public ResponseEntity<?> checkEmail(@RequestParam String email) {
        if (userRepo.existsByEmail(email)) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Email is already used"));
        }
        return ResponseEntity.ok(Map.of("available", true));
    }

    @GetMapping("/check-username")
    public ResponseEntity<?> checkUsername(@RequestParam String username) {
        if (userRepo.existsByUsername(username)) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Username is already used"));
        }
        return ResponseEntity.ok(Map.of("available", true));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        String email = user.getEmail();
        checkEmail(email);

        String username = user.getUsername();
        checkUsername(username);

        User newUser = new User();
        newUser.setEmail(email);
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(newUser);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = (UserDetails) authentication.getPrincipal();
        var token = jwtService.generateToken(user.getUsername());
        var response = new AuthResponse(token);
        return ResponseEntity.ok(response);
    }
}
