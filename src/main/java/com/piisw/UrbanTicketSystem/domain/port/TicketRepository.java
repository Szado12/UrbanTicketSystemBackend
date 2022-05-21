package com.piisw.UrbanTicketSystem.domain.port;

import com.piisw.UrbanTicketSystem.domain.model.Ticket;

public interface TicketRepository {
    Ticket save(Ticket ticket);
}
