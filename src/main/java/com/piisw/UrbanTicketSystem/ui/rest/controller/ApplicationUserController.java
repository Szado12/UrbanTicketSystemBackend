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

    private final TicketRepository ticketRepository;

    @Autowired
    public ApplicationUserController(SecurityRepository securityRepository, OAuthRepository oAuthRepository, UserRepository userRepository, TicketRepository ticketRepository) {
        this.securityRepository = securityRepository;
        this.oAuthRepository = oAuthRepository;
        this.userRepository = userRepository;
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
            if (ticket.getStatus().equals(VALID.name())){
                Duration duration = Duration.between(ticket.getValidatedTime(), LocalDateTime.now());
                if (ticket.getType().getMinutesOfValidity() != 0) {
                    if (duration.toMinutes() > ticket.getType().getMinutesOfValidity())
                        ticket.setStatus(INVALID.name());
                } else if (ticket.getType().getDaysOfValidity() == 0) {
                    if (duration.toMinutes() > 90)
                        ticket.setStatus(INVALID.name());
                } else {
                    if (duration.toDays() > ticket.getType().getDaysOfValidity())
                        ticket.setStatus(INVALID.name());
                }
                ticketRepository.save(ticket);
            }
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
        userToUpdate.setPassword(updatedUser.getPassword());
        return new ResponseEntity<>(userRepository.save(userToUpdate), HttpStatus.OK);
    }
}
