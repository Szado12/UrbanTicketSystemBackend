package com.piisw.UrbanTicketSystem.domain.port;

import com.piisw.UrbanTicketSystem.domain.model.TicketCategory;

import java.util.List;

public interface TicketCategoryRepository {
    List<TicketCategory> getAll();
}
