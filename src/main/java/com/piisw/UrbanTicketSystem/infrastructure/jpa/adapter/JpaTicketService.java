package com.piisw.UrbanTicketSystem.infrastructure.jpa.adapter;

import com.piisw.UrbanTicketSystem.domain.model.Ticket;
import com.piisw.UrbanTicketSystem.domain.model.TicketCategory;
import com.piisw.UrbanTicketSystem.domain.model.TicketStatus;
import com.piisw.UrbanTicketSystem.domain.model.TicketType;
import com.piisw.UrbanTicketSystem.domain.model.request.TicketValidityResponse;
import com.piisw.UrbanTicketSystem.domain.port.TicketRepository;
import com.piisw.UrbanTicketSystem.domain.port.TicketTypeRepository;
import com.piisw.UrbanTicketSystem.infrastructure.jpa.model.TicketCategoryEntity;
import com.piisw.UrbanTicketSystem.infrastructure.jpa.model.TicketEntity;
import com.piisw.UrbanTicketSystem.infrastructure.jpa.model.TicketTypeEntity;
import com.piisw.UrbanTicketSystem.infrastructure.jpa.repository.JpaTicketRepository;
import com.piisw.UrbanTicketSystem.infrastructure.jpa.repository.JpaTicketTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.piisw.UrbanTicketSystem.domain.model.TicketStatus.INVALID;
import static com.piisw.UrbanTicketSystem.domain.model.TicketStatus.VALID;

@Service
public class JpaTicketService implements TicketRepository {
    private JpaTicketRepository jpaTicketRepository;

    private TicketTypeRepository ticketTypeRepository;

    @Autowired
    public JpaTicketService(JpaTicketRepository jpaTicketRepository) {
        this.jpaTicketRepository = jpaTicketRepository;
    }

    @Override
    public Ticket findById(long id) {
        Ticket ticket = mapTicketEntityToTicket(jpaTicketRepository.findById(id).get());
        updateValidity(ticket);
        return save(ticket);
    }

    @Override
    public Ticket findByUuid(String uuid) {
        Ticket ticket = mapTicketEntityToTicket(jpaTicketRepository.findByUuid(uuid).get());
        updateValidity(ticket);
        return save(ticket);
    }

    @Override
    public Ticket save(Ticket ticket) {
        return mapTicketEntityToTicket(jpaTicketRepository.save(mapTicketToTicketEntity(ticket)));
    }

    @Override
    public Ticket buyTicket(Long ticketTypeId) {
        Ticket ticket = new Ticket();
        ticket.setUuid(UUID.randomUUID().toString().substring(0,8));
        ticket.setStatus(TicketStatus.BOUGHT.toString());
        ticket.setBoughtTime(LocalDateTime.now());
        ticket.setType(ticketTypeRepository.getById(ticketTypeId));
        return save(ticket);
    }

    @Override
    public Ticket updateValidity(Ticket ticket) {
        if (ticket.getStatus().equals(VALID.name())){
            Duration duration = Duration.between(ticket.getValidatedTime(), LocalDateTime.now());
            if (ticket.getType().getMinutesOfValidity() != 0) {
                if (duration.toMinutes() > ticket.getType().getMinutesOfValidity())
                    ticket.setStatus(INVALID.name());
            } else if (ticket.getType().getDaysOfValidity() == 0) {
                if (duration.toMinutes() > 90)
                    ticket.setStatus(INVALID.name());
            } else {
                if (duration.toDays() > ticket.getType().getDaysOfValidity())
                    ticket.setStatus(INVALID.name());
            }
        }
        return save(ticket);
    }

    @Override
    public Optional<Ticket> validateTicket(String ticketUuid, int validatedInBus) {
        Ticket ticketToValidate = findByUuid(ticketUuid);
        if (ticketToValidate.getStatus().equals(VALID.name()))
            return Optional.empty();
        ticketToValidate.setValidatedInBus(validatedInBus);
        ticketToValidate.setStatus(VALID.name());
        ticketToValidate.setValidatedTime(LocalDateTime.now());
        return Optional.ofNullable(save(ticketToValidate));
    }

    @Override
    public TicketValidityResponse checkTicketValidity(String ticketUuid, int validatedInBus) {
        TicketValidityResponse ticketValidityResponse = new TicketValidityResponse(false, false);
        Ticket ticket = findByUuid(ticketUuid);
        ticketValidityResponse.setReduced(ticket.getType().isReduced());
        updateValidity(ticket);
        if (ticket.getStatus().equals(VALID.name())) {
            ticketValidityResponse.setValid(true);
            if (ticket.getType().getCategory().getName().equals("SINGLE_TICKET") && validatedInBus != ticket.getValidatedInBus())
                ticketValidityResponse.setValid(false);
        }
        return ticketValidityResponse;
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
