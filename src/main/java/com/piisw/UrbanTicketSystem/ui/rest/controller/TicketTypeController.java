package com.piisw.UrbanTicketSystem.ui.rest.controller;

import com.piisw.UrbanTicketSystem.domain.model.TicketType;
import com.piisw.UrbanTicketSystem.domain.port.TicketTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TicketTypeController {
    private TicketTypeRepository ticketTypeRepository;

    @Autowired
    public TicketTypeController(TicketTypeRepository ticketTypeRepository) {
        this.ticketTypeRepository = ticketTypeRepository;
    }

    @GetMapping("/tickettypes")
    public ResponseEntity<Object> getAllTicketTypes() {
        return new ResponseEntity<>(ticketTypeRepository.getAll(), HttpStatus.OK);
    }

    @PostMapping("/tickettypes")
    public ResponseEntity<Object> getAllTicketTypes(@RequestBody TicketType ticketType) {
        return new ResponseEntity<>(ticketTypeRepository.save(ticketType), HttpStatus.OK);
    }
}
