package com.piisw.UrbanTicketSystem.domain.port;

import com.piisw.UrbanTicketSystem.domain.model.TicketType;

import java.util.List;

public interface TicketTypeRepository {
    public TicketType getById(long id);
    public List<TicketType> getAll();
    public TicketType save(TicketType ticketType);
}
