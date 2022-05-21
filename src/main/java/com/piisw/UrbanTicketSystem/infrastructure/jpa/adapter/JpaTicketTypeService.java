package com.piisw.UrbanTicketSystem.infrastructure.jpa.adapter;

import com.piisw.UrbanTicketSystem.domain.model.TicketType;
import com.piisw.UrbanTicketSystem.domain.model.User;
import com.piisw.UrbanTicketSystem.domain.port.TicketTypeRepository;
import com.piisw.UrbanTicketSystem.infrastructure.jpa.model.TicketTypeEntity;
import com.piisw.UrbanTicketSystem.infrastructure.jpa.repository.JpaTicketTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JpaTicketTypeService implements TicketTypeRepository {
    private JpaTicketTypeRepository jpaTicketTypeRepository;

    @Autowired
    public JpaTicketTypeService(JpaTicketTypeRepository jpaTicketTypeRepository) {
        this.jpaTicketTypeRepository = jpaTicketTypeRepository;
    }

    @Override
    public TicketType getById(long id) {
        return mapTicketTypeEntityToTicketType(jpaTicketTypeRepository.findById(id).get());
    }

    @Override
    public List<TicketType> getAll() {
        return mapTicketTypesEntitiesToTicketTypes(jpaTicketTypeRepository.findAll());
    }

    @Override
    public TicketType save(TicketType ticketType) {
        return mapTicketTypeEntityToTicketType(jpaTicketTypeRepository.save(mapTicketTypeToTicketTypeEntity(ticketType)));
    }

    private List<TicketType> mapTicketTypesEntitiesToTicketTypes(List<TicketTypeEntity> ticketTypeEntities) {
        List<TicketType> result = new ArrayList<>();
        for (TicketTypeEntity ticketTypeEntity : ticketTypeEntities) {
            result.add(mapTicketTypeEntityToTicketType(ticketTypeEntity));
        }
        return result;
    }

    private TicketType mapTicketTypeEntityToTicketType(TicketTypeEntity ticketTypeEntity) {
        if (ticketTypeEntity == null)
            return null;
        return TicketType.builder()
                .id(ticketTypeEntity.getId())
                .price(ticketTypeEntity.getPrice())
                .category(ticketTypeEntity.getCategory())
                .minutesOfValidity(ticketTypeEntity.getMinutesOfValidity())
                .daysOfValidity(ticketTypeEntity.getDaysOfValidity())
                .build();
    }

    public TicketTypeEntity mapTicketTypeToTicketTypeEntity(TicketType ticketType) {
        if (ticketType == null)
            return null;
        return TicketTypeEntity.builder()
                .id(ticketType.getId())
                .price(ticketType.getPrice())
                .category(ticketType.getCategory())
                .minutesOfValidity(ticketType.getMinutesOfValidity())
                .daysOfValidity(ticketType.getDaysOfValidity())
                .build();
    }
}
