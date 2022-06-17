package com.piisw.UrbanTicketSystem.infrastructure.jpa.adapter;

import com.piisw.UrbanTicketSystem.domain.model.*;
import com.piisw.UrbanTicketSystem.domain.port.TicketRepository;
import com.piisw.UrbanTicketSystem.domain.port.TicketTypeRepository;
import com.piisw.UrbanTicketSystem.domain.port.UserRepository;
import com.piisw.UrbanTicketSystem.infrastructure.jpa.model.TicketCategoryEntity;
import com.piisw.UrbanTicketSystem.infrastructure.jpa.model.TicketEntity;
import com.piisw.UrbanTicketSystem.infrastructure.jpa.model.TicketTypeEntity;
import com.piisw.UrbanTicketSystem.infrastructure.jpa.model.UserEntity;
import com.piisw.UrbanTicketSystem.infrastructure.jpa.repository.JpaUserRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class JpaUserService implements UserRepository {
    private final JpaUserRepository userRepository;

    private final TicketRepository ticketRepository;

    @Autowired
    public JpaUserService(JpaUserRepository userRepository, TicketRepository ticketRepository) {
        this.userRepository = userRepository;
        this.ticketRepository = ticketRepository;
    }

    @SneakyThrows
    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(mapEntityToUser(userRepository.findByUsername(username).orElse(null)));
    }

    @SneakyThrows
    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(mapEntityToUser(userRepository.findById(id).orElse(null)));
    }

    @Override
    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public User updateUserData(Long id, User updatedUser) {
        User userToUpdate = findById(id).get();
        userToUpdate.setName(updatedUser.getName());
        userToUpdate.setSurname(updatedUser.getSurname());
        return save(userToUpdate);
    }

    @Override
    public User save(User user) {
        return mapEntityToUser(userRepository.save(mapUserToEntity(user)));
    }

    @Override
    public Ticket addTicket(Long userId, Long ticketTypeId) {
        Ticket boughtTicket = ticketRepository.buyTicket(ticketTypeId);
        User user = findById(userId).get();
        user.getTickets().add(boughtTicket);
        userRepository.save(mapUserToEntity(user));
        return boughtTicket;
    }

    @Override
    public List<Ticket> addTickets(Long userId, List<Long> ticketTypeIds, List<Long> ticketTypeCounts) {
        List<Ticket> boughtTickets = new ArrayList<>();
        for (int i = 0; i < ticketTypeIds.size(); i++) {
            for (int j = 0; j < ticketTypeCounts.get(i); j++) {
                boughtTickets.add(addTicket(userId, ticketTypeIds.get(i)));
            }
        }
        return boughtTickets;
    }

    private User mapEntityToUser(UserEntity userEntity) {
        if (userEntity == null)
            return null;
        return User.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .name(userEntity.getName())
                .surname(userEntity.getSurname())
                .password(userEntity.getPassword())
                .role(userEntity.getRole())
                .active(userEntity.isActive())
                .tickets(mapTicketEntitiesToTickets(userEntity.getTickets()))
                .build();
    }

    private UserEntity mapUserToEntity(User user) {
        return UserEntity.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .surname(user.getSurname())
                .password(user.getPassword())
                .role(user.getRole())
                .active(user.isActive())
                .tickets(mapTicketsToTicketEntities(user.getTickets()))
                .build();
    }

    private List<Ticket> mapTicketEntitiesToTickets(List<TicketEntity> ticketEntityList) {
        List<Ticket> result = new ArrayList<>();
        for (TicketEntity ticketEntity : ticketEntityList) {
            result.add(mapTicketEntityToTicket(ticketEntity));
        }
        return result;
    }

    private List<TicketEntity> mapTicketsToTicketEntities(List<Ticket> ticketList) {
        List<TicketEntity> result = new ArrayList<>();
        for (Ticket ticket : ticketList) {
            result.add(mapTicketToTicketEntity(ticket));
        }
        return result;
    }

    private Ticket mapTicketEntityToTicket(TicketEntity ticketEntity) {
        if (ticketEntity == null)
            return null;
        return Ticket.builder()
                .id(ticketEntity.getId())
                .uuid(ticketEntity.getUuid())
                .boughtTime(ticketEntity.getBoughtTime())
                .validatedTime(ticketEntity.getValidatedTime())
                .validatedInBus(ticketEntity.getValidatedInBus())
                .status(ticketEntity.getTicketStatus())
                .type(mapTicketTypeEntityToTicketType(ticketEntity.getType()))
                .build();
    }

    private TicketEntity mapTicketToTicketEntity(Ticket ticket) {
        if (ticket == null)
            return null;
        return TicketEntity.builder()
                .id(ticket.getId())
                .uuid(ticket.getUuid())
                .boughtTime(ticket.getBoughtTime())
                .validatedTime(ticket.getValidatedTime())
                .validatedInBus(ticket.getValidatedInBus())
                .ticketStatus(ticket.getStatus())
                .type(mapTicketTypeToTicketTypeEntity(ticket.getType()))
                .build();
    }

    public TicketType mapTicketTypeEntityToTicketType(TicketTypeEntity ticketTypeEntity) {
        if (ticketTypeEntity == null)
            return null;
        return TicketType.builder()
                .id(ticketTypeEntity.getId())
                .price(ticketTypeEntity.getPrice())
                .category(mapTicketCategoryEntityToTicketCategory(ticketTypeEntity.getCategory()))
                .minutesOfValidity(ticketTypeEntity.getMinutesOfValidity())
                .daysOfValidity(ticketTypeEntity.getDaysOfValidity())
                .reduced(ticketTypeEntity.isReduced())
                .displayName(ticketTypeEntity.getDisplayName())
                .build();
    }

    public TicketTypeEntity mapTicketTypeToTicketTypeEntity(TicketType ticketType) {
        if (ticketType == null)
            return null;
        return TicketTypeEntity.builder()
                .id(ticketType.getId())
                .price(ticketType.getPrice())
                .category(mapTicketCategoryToTicketCategoryEntity(ticketType.getCategory()))
                .minutesOfValidity(ticketType.getMinutesOfValidity())
                .daysOfValidity(ticketType.getDaysOfValidity())
                .reduced(ticketType.isReduced())
                .displayName(ticketType.getDisplayName())
                .build();
    }

    private TicketCategory mapTicketCategoryEntityToTicketCategory(TicketCategoryEntity ticketCategoryEntity) {
        if (ticketCategoryEntity == null)
            return null;
        return TicketCategory.builder()
                .id(ticketCategoryEntity.getId())
                .name(ticketCategoryEntity.getName())
                .build();
    }

    private TicketCategoryEntity mapTicketCategoryToTicketCategoryEntity(TicketCategory ticketCategory) {
        if (ticketCategory == null)
            return null;
        return TicketCategoryEntity.builder()
                .id(ticketCategory.getId())
                .name(ticketCategory.getName())
                .build();
    }
}
