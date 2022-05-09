package com.piisw.UrbanTicketSystem.repository;

import com.piisw.UrbanTicketSystem.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
