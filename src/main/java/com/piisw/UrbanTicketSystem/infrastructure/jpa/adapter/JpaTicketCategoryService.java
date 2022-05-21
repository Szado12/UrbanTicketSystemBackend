package com.piisw.UrbanTicketSystem.infrastructure.jpa.adapter;

import com.piisw.UrbanTicketSystem.domain.model.TicketCategory;
import com.piisw.UrbanTicketSystem.domain.model.TicketType;
import com.piisw.UrbanTicketSystem.domain.port.TicketCategoryRepository;
import com.piisw.UrbanTicketSystem.infrastructure.jpa.model.TicketCategoryEntity;
import com.piisw.UrbanTicketSystem.infrastructure.jpa.model.TicketTypeEntity;
import com.piisw.UrbanTicketSystem.infrastructure.jpa.repository.JpaTicketCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JpaTicketCategoryService implements TicketCategoryRepository {
    private JpaTicketCategoryRepository jpaTicketCategoryRepository;

    @Autowired
    public JpaTicketCategoryService(JpaTicketCategoryRepository jpaTicketCategoryRepository) {
        this.jpaTicketCategoryRepository = jpaTicketCategoryRepository;
    }

    @Override
    public List<TicketCategory> getAll() {
        return mapTicketCategoryEntitiesToTicketCategories(jpaTicketCategoryRepository.findAll());
    }

    private List<TicketCategory> mapTicketCategoryEntitiesToTicketCategories(List<TicketCategoryEntity> ticketCategoryEntities) {
        List<TicketCategory> result = new ArrayList<>();
        for (TicketCategoryEntity ticketCategoryEntity : ticketCategoryEntities) {
            result.add(mapTicketCategoryEntityToTicketCategory(ticketCategoryEntity));
        }
        return result;
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
