package com.piisw.UrbanTicketSystem.infrastructure.jpa.repository;

import com.piisw.UrbanTicketSystem.infrastructure.jpa.model.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaTicketRepository  extends JpaRepository<TicketEntity, Long> {
}
