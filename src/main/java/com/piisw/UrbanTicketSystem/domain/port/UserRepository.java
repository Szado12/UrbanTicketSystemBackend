package com.piisw.UrbanTicketSystem.domain.port;

import com.piisw.UrbanTicketSystem.domain.model.Ticket;
import com.piisw.UrbanTicketSystem.domain.model.TicketType;
import com.piisw.UrbanTicketSystem.domain.model.User;
import com.piisw.UrbanTicketSystem.domain.model.UserRole;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    User save(User user);
    Ticket addTicket(User user, Ticket ticket);
}
