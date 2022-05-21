package com.piisw.UrbanTicketSystem.infrastructure.jpa.repository;

import com.piisw.UrbanTicketSystem.infrastructure.jpa.model.TicketTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaTicketTypeRepository  extends JpaRepository<TicketTypeEntity, Long> {
}
