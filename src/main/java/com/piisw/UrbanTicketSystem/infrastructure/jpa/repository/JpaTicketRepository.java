package com.piisw.UrbanTicketSystem.infrastructure.jpa.repository;

import com.piisw.UrbanTicketSystem.infrastructure.jpa.model.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface JpaTicketRepository  extends JpaRepository<TicketEntity, Long> {
    Optional<TicketEntity> findByUuid(String uuid);
}
