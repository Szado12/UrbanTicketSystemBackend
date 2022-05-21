package com.piisw.UrbanTicketSystem.infrastructure.jpa.repository;

import com.piisw.UrbanTicketSystem.infrastructure.jpa.model.TicketCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaTicketCategoryRepository extends JpaRepository<TicketCategoryEntity, Long> {
}
