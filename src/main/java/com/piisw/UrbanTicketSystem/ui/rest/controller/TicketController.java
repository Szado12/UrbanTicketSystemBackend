package com.piisw.UrbanTicketSystem.ui.rest.controller;

import com.piisw.UrbanTicketSystem.domain.model.Ticket;
import com.piisw.UrbanTicketSystem.domain.model.TicketStatus;
import com.piisw.UrbanTicketSystem.domain.model.User;
import com.piisw.UrbanTicketSystem.domain.model.request.TicketDetails;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    public ResponseEntity<Object> getTicket(@RequestBody TicketDetails ticketDetails) {
        Ticket ticket = ticketRepository.findByUuid(ticketDetails.getTicketUuid());
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
        }
        return new ResponseEntity<>(ticketRepository.save(ticket), HttpStatus.OK);
    }

    @PostMapping("/ticket")
    public ResponseEntity<Object> buyTicket(@RequestAttribute Long id, @RequestParam long ticketTypeId) {
        Ticket ticket = new Ticket();
        ticket.setUuid(UUID.randomUUID().toString().substring(0,8));
        ticket.setStatus(TicketStatus.BOUGHT.toString());
        ticket.setBoughtTime(LocalDateTime.now());
        ticket.setType(ticketTypeRepository.getById(ticketTypeId));
        Ticket boughtTicket = ticketRepository.save(ticket);
        User buyingUser = userRepository.findById(id).get();
        return new ResponseEntity<>(userRepository.addTicket(buyingUser, boughtTicket), HttpStatus.CREATED);
    }

    @PostMapping("/tickets")
    public ResponseEntity<Object> buyTicket(@RequestAttribute Long id, @RequestBody TicketsRequest ticketsRequest) {
        List<Long> ticketTypeIds = ticketsRequest.getTicketTypeIds();
        List<Long> ticketTypeCounts = ticketsRequest.getTicketTypeCounts();
        if (ticketTypeIds.size() != ticketTypeCounts.size())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        List<Ticket> boughtTickets = new ArrayList<>();
        for (int i = 0; i < ticketTypeIds.size(); i++) {
            for (int j = 0; j < ticketTypeCounts.get(i); j++) {
                Ticket ticket = new Ticket();
                ticket.setUuid(UUID.randomUUID().toString().substring(0,8));
                ticket.setStatus(TicketStatus.BOUGHT.toString());
                ticket.setBoughtTime(LocalDateTime.now());
                ticket.setType(ticketTypeRepository.getById(ticketTypeIds.get(i)));
                Ticket boughtTicket = ticketRepository.save(ticket);
                User buyingUser = userRepository.findById(id).get();
                boughtTickets.add(userRepository.addTicket(buyingUser, boughtTicket));
            }
        }
        return new ResponseEntity<>(boughtTickets, HttpStatus.CREATED);
    }

    @PutMapping("/ticket/validate")
    public ResponseEntity<Object> validateTicket(@RequestBody TicketDetails ticketDetails) {
        Ticket ticketToValidate = ticketRepository.findByUuid(ticketDetails.getTicketUuid());
        if (ticketToValidate.getStatus().equals(VALID.name()))
            throw new IllegalArgumentException("Ticket already validated");
        ticketToValidate.setValidatedInBus(ticketDetails.getValidatedInBus());
        ticketToValidate.setStatus(VALID.name());
        ticketToValidate.setValidatedTime(LocalDateTime.now());
        return new ResponseEntity<>(ticketRepository.save(ticketToValidate), HttpStatus.OK);
    }
}
