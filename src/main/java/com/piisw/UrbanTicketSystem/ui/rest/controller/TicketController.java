package com.piisw.UrbanTicketSystem.ui.rest.controller;

import com.piisw.UrbanTicketSystem.domain.model.Ticket;
import com.piisw.UrbanTicketSystem.domain.model.TicketStatus;
import com.piisw.UrbanTicketSystem.domain.model.User;
import com.piisw.UrbanTicketSystem.domain.model.request.TicketDetails;
import com.piisw.UrbanTicketSystem.domain.model.request.TicketValidityResponse;
import com.piisw.UrbanTicketSystem.domain.model.request.TicketsRequest;
import com.piisw.UrbanTicketSystem.domain.port.TicketRepository;
import com.piisw.UrbanTicketSystem.domain.port.TicketTypeRepository;
import com.piisw.UrbanTicketSystem.domain.port.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.*;

import static com.piisw.UrbanTicketSystem.domain.model.TicketStatus.INVALID;
import static com.piisw.UrbanTicketSystem.domain.model.TicketStatus.VALID;

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
    public ResponseEntity<Object> getTicket(@RequestParam String ticketUuid) {
        return new ResponseEntity<>(ticketRepository.findByUuid(ticketUuid), HttpStatus.OK);
    }

    @PostMapping("/ticket")
    public ResponseEntity<Object> buyTicket(@RequestAttribute Long id, @RequestParam long ticketTypeId) {
        return new ResponseEntity<>(userRepository.addTicket(id, ticketTypeId), HttpStatus.CREATED);
    }

    @PostMapping("/tickets")
    public ResponseEntity<Object> buyTickets(@RequestAttribute Long id, @RequestBody TicketsRequest ticketsRequest) {
        List<Long> ticketTypeIds = ticketsRequest.getTicketTypeIds();
        List<Long> ticketTypeCounts = ticketsRequest.getTicketTypeCounts();
        if (ticketTypeIds.size() != ticketTypeCounts.size())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(userRepository.addTickets(id, ticketTypeIds, ticketTypeCounts), HttpStatus.CREATED);
    }

    @PutMapping("/ticket/validate")
    public ResponseEntity<Object> validateTicket(@RequestBody TicketDetails ticketDetails) {
        try {
            String ticketUuid = ticketDetails.getTicketUuid();
            int validatedInBus = ticketDetails.getValidatedInBus();
            Optional<Ticket> validatedTicket = ticketRepository.validateTicket(ticketUuid, validatedInBus);
            if (validatedTicket.isPresent())
                return new ResponseEntity<>(validatedTicket.get(), HttpStatus.OK);
            else
                return new ResponseEntity<>("Ticket already validated", HttpStatus.BAD_REQUEST);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("No value present", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/ticket/check")
    public ResponseEntity<Object> checkTicketValidity(@RequestBody TicketDetails ticketDetails) {
        String ticketUuid = ticketDetails.getTicketUuid();
        int validatedInBus = ticketDetails.getValidatedInBus();
        return new ResponseEntity<>(ticketRepository.checkTicketValidity(ticketUuid, validatedInBus), HttpStatus.OK);
    }
}
