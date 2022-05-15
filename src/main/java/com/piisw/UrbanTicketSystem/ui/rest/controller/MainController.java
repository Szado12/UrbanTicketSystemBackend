package com.piisw.UrbanTicketSystem.ui.rest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
    @GetMapping("/healthcheck")
    String healthCheck() {
        return "OK";
    }

    // Authentication tests

    @GetMapping("/home")
    String home() {
        return "No authentication needed";
    }

    @GetMapping("/userPanel")
    String userPanel() {
        return "Authenticated to user panel";
    }

    @GetMapping("/workerPanel")
    String receptionistPanel() {
        return "Authenticated to worker panel";
    }

    @GetMapping("/adminPanel")
    String adminPanel() {
        return "Authenticated to admin panel";
    }
}
