package com.piisw.UrbanTicketSystem.ui.rest.controller;


import com.piisw.UrbanTicketSystem.domain.model.User;
import com.piisw.UrbanTicketSystem.domain.model.UserRole;
import com.piisw.UrbanTicketSystem.domain.port.OAuthRepository;
import com.piisw.UrbanTicketSystem.domain.port.SecurityRepository;
import com.piisw.UrbanTicketSystem.domain.port.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ApplicationUserController {
    private final SecurityRepository securityRepository;
    private final OAuthRepository oAuthRepository;
    private final UserRepository userRepository;


    @Autowired
    public ApplicationUserController(SecurityRepository securityRepository, OAuthRepository oAuthRepository, UserRepository userRepository) {
        this.securityRepository = securityRepository;
        this.oAuthRepository = oAuthRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    ResponseEntity<Object> register(@RequestBody User newUser) {
        return new ResponseEntity<>(securityRepository.registerUser(newUser, UserRole.CLIENT), HttpStatus.CREATED);
    }

    @PostMapping("/facebook/login")
    public  ResponseEntity<?> facebookAuth(@RequestBody HttpServletRequest request) {
        return ResponseEntity.ok(oAuthRepository.authorizeUser(request));
    }
}
