package com.piisw.UrbanTicketSystem.ui.rest.controller;


import com.piisw.UrbanTicketSystem.domain.port.SecurityRepository;
import com.piisw.UrbanTicketSystem.infrastructure.security.adapter.ApplicationUserService;
import com.piisw.UrbanTicketSystem.domain.model.JwtTokenResponse;
import com.piisw.UrbanTicketSystem.domain.model.UserRole;
import com.piisw.UrbanTicketSystem.infrastructure.security.model.UsernamePasswordAuthRequest;
import com.piisw.UrbanTicketSystem.domain.port.UserRepository;
import com.piisw.UrbanTicketSystem.infrastructure.security.jwt.JwtTokenProvider;
import com.piisw.UrbanTicketSystem.domain.model.User;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class ApplicationUserController {
    private final UserRepository userRepository;
    private final SecurityRepository securityRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final ApplicationUserService applicationUserService;

    @Autowired
    public ApplicationUserController(UserRepository userRepository, SecurityRepository securityRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager, ApplicationUserService applicationUserService) {
        this.userRepository = userRepository;
        this.securityRepository = securityRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.applicationUserService = applicationUserService;
    }

    @PostMapping("/register")
    ResponseEntity<Object> register(@RequestBody User newUser) {
        Optional<User> checkUsername = userRepository.findByEmail(newUser.getEmail());
        if (checkUsername.isPresent())
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        else {
            String unHashedPassword= newUser.getPassword();
            newUser.setPassword(passwordEncoder.encode(unHashedPassword));
            newUser.setActive(true);
            newUser.setRole("CLIENT");
            return new ResponseEntity<>(securityRepository.registerUser(newUser, UserRole.CLIENT), HttpStatus.CREATED);
        }
    }
    }
