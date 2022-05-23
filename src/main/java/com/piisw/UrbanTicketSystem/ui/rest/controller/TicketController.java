package com.piisw.UrbanTicketSystem.ui.rest.controller;

import com.piisw.UrbanTicketSystem.domain.model.Ticket;
import com.piisw.UrbanTicketSystem.domain.model.TicketStatus;
import com.piisw.UrbanTicketSystem.domain.model.User;
import com.piisw.UrbanTicketSystem.domain.port.TicketRepository;
import com.piisw.UrbanTicketSystem.domain.port.TicketTypeRepository;
import com.piisw.UrbanTicketSystem.domain.port.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
public class TicketController {
    private TicketRepository ticketRepository;
    private TicketTypeRepository ticketTypeRepository;
    private UserRepository userRepository;

    @Autowired
    public TicketController(TicketTypeRepository ticketTypeRepository, TicketRepository ticketRepository, UserRepository userRepository) {
        this.ticketTypeRepository = ticketTypeRepository;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/ticket")
    public ResponseEntity<Object> getTicket(@RequestParam long ticketId) {
        return new ResponseEntity<>(ticketRepository.findById(ticketId), HttpStatus.OK);
    }

    @PostMapping("/ticket")
    public ResponseEntity<Object> buyTicket(@RequestAttribute Long id, @RequestParam long ticketTypeId) {
        Ticket ticket = new Ticket();
        ticket.setStatus(TicketStatus.BOUGHT.toString());
        ticket.setBoughtTime(LocalDateTime.now());
        ticket.setType(ticketTypeRepository.getById(ticketTypeId));
        Ticket boughtTicket = ticketRepository.save(ticket);
        User buyingUser = userRepository.findById(id).get();
        return new ResponseEntity<>(userRepository.addTicket(buyingUser, boughtTicket), HttpStatus.CREATED);
    }
}
