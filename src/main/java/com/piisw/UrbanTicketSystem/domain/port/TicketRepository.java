package com.piisw.UrbanTicketSystem.domain.port;

import com.piisw.UrbanTicketSystem.domain.model.Ticket;

import java.util.UUID;

public interface TicketRepository {
    Ticket findById(long id);
    Ticket findByUuid(String uuid);
    Ticket save(Ticket ticket);
    Ticket updateValidity(Ticket ticket);
}
