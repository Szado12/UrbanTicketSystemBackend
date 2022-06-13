package com.piisw.UrbanTicketSystem.ui.rest.controller;


import com.piisw.UrbanTicketSystem.domain.model.Ticket;
import com.piisw.UrbanTicketSystem.domain.model.User;
import com.piisw.UrbanTicketSystem.domain.model.UserRole;
import com.piisw.UrbanTicketSystem.domain.port.OAuthRepository;
import com.piisw.UrbanTicketSystem.domain.port.SecurityRepository;
import com.piisw.UrbanTicketSystem.domain.port.TicketRepository;
import com.piisw.UrbanTicketSystem.domain.port.UserRepository;
import com.piisw.UrbanTicketSystem.domain.model.security.FacebookLoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;

import static com.piisw.UrbanTicketSystem.domain.model.TicketStatus.INVALID;
import static com.piisw.UrbanTicketSystem.domain.model.TicketStatus.VALID;

@RestController
public class ApplicationUserController {
    private final SecurityRepository securityRepository;
    private final OAuthRepository oAuthRepository;
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final TicketRepository ticketRepository;

    @Autowired
    public ApplicationUserController(SecurityRepository securityRepository, OAuthRepository oAuthRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, TicketRepository ticketRepository) {
        this.securityRepository = securityRepository;
        this.oAuthRepository = oAuthRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.ticketRepository = ticketRepository;
    }

    @PostMapping("/register")
    ResponseEntity<Object> register(@RequestBody User newUser) {
        return new ResponseEntity<>(securityRepository.registerUser(newUser, UserRole.CLIENT), HttpStatus.CREATED);
    }

    @PostMapping("/facebook/login")
    public  ResponseEntity<?> facebookAuth(@RequestBody FacebookLoginRequest request) {
        return ResponseEntity.ok(oAuthRepository.authorizeUser(request));
    }

    @GetMapping("/profile")
    public ResponseEntity<Object> getUserProfile(@RequestAttribute Long id) {
        User user = userRepository.findById(id).get();
        for (Ticket ticket: user.getTickets()) {
            ticketRepository.updateValidity(ticket);
        }
        return new ResponseEntity<>(userRepository.findById(id), HttpStatus.OK);
    }

    @PutMapping("/profile/data")
    public ResponseEntity<Object> updateUserData(@RequestAttribute Long id, @RequestBody User updatedUser) {
        User userToUpdate = userRepository.findById(id).get();
        userToUpdate.setName(updatedUser.getName());
        userToUpdate.setSurname(updatedUser.getSurname());
        return new ResponseEntity<>(userRepository.save(userToUpdate), HttpStatus.OK);
    }

    @PutMapping("/profile/password")
    public ResponseEntity<Object> updateUserPassword(@RequestAttribute Long id, @RequestBody User updatedUser) {
        User userToUpdate = userRepository.findById(id).get();
        userToUpdate.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        return new ResponseEntity<>(userRepository.save(userToUpdate), HttpStatus.OK);
    }
}
