package com.piisw.UrbanTicketSystem.domain.port;

import com.piisw.UrbanTicketSystem.domain.model.Ticket;

public interface TicketRepository {
    Ticket findById(long id);
    Ticket save(Ticket ticket);
}
