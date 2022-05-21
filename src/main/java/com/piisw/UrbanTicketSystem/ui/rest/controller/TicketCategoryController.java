package com.piisw.UrbanTicketSystem.ui.rest.controller;

import com.piisw.UrbanTicketSystem.domain.model.TicketType;
import com.piisw.UrbanTicketSystem.domain.port.TicketCategoryRepository;
import com.piisw.UrbanTicketSystem.domain.port.TicketTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TicketCategoryController {
    private TicketCategoryRepository ticketCategoryRepository;

    @Autowired
    public TicketCategoryController(TicketCategoryRepository ticketCategoryRepository) {
        this.ticketCategoryRepository = ticketCategoryRepository;
    }

    @GetMapping("/ticketcategories")
    public ResponseEntity<Object> getAllTicketCategories() {
        return new ResponseEntity<>(ticketCategoryRepository.getAll(), HttpStatus.OK);
    }
}
