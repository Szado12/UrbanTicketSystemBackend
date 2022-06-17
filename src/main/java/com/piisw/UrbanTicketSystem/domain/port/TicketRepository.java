package com.piisw.UrbanTicketSystem.domain.port;

import com.piisw.UrbanTicketSystem.domain.model.Ticket;
import com.piisw.UrbanTicketSystem.domain.model.request.TicketValidityResponse;

import java.util.Optional;
import java.util.UUID;

public interface TicketRepository {
    Ticket findById(long id);
    Ticket findByUuid(String uuid);
    Ticket save(Ticket ticket);
    Ticket buyTicket(Long ticketTypeId);
    Ticket updateValidity(Ticket ticket);
    Optional<Ticket> validateTicket(String ticketUuid, int validatedInBus);
    TicketValidityResponse checkTicketValidity(String ticketUuid, int validatedInBus);
}
