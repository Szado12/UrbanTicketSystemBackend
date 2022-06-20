package com.piisw.UrbanTicketSystem;

import com.piisw.UrbanTicketSystem.domain.model.TicketCategory;
import com.piisw.UrbanTicketSystem.domain.model.TicketType;
import com.piisw.UrbanTicketSystem.domain.model.User;
import com.piisw.UrbanTicketSystem.infrastructure.jpa.adapter.JpaTicketTypeService;
import com.piisw.UrbanTicketSystem.infrastructure.jpa.adapter.JpaUserService;
import com.piisw.UrbanTicketSystem.infrastructure.jpa.model.TicketCategoryEntity;
import com.piisw.UrbanTicketSystem.infrastructure.jpa.model.TicketTypeEntity;
import com.piisw.UrbanTicketSystem.infrastructure.jpa.model.UserEntity;
import com.piisw.UrbanTicketSystem.infrastructure.jpa.repository.JpaTicketTypeRepository;
import com.piisw.UrbanTicketSystem.infrastructure.jpa.repository.JpaUserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = {JpaTicketTypeService.class})
public class TicketTypeServiceTests {
    @InjectMocks
    JpaTicketTypeService jpaTicketTypeService;

    @Mock
    JpaTicketTypeRepository jpaTicketTypeRepository;

    @Test
    public void shouldReturnTicketTypeOnSave() {
        TicketType testTicketType = new TicketType();
        testTicketType.setReduced(true);
        testTicketType.setDaysOfValidity(0);
        testTicketType.setMinutesOfValidity(30);
        testTicketType.setPrice(340L);
        testTicketType.setDisplayName("30 minutes ticket");

        TicketTypeEntity testTicketTypeEntity = new TicketTypeEntity();
        testTicketTypeEntity.setReduced(true);
        testTicketTypeEntity.setDaysOfValidity(0);
        testTicketTypeEntity.setMinutesOfValidity(30);
        testTicketTypeEntity.setPrice(340L);
        testTicketTypeEntity.setDisplayName("30 minutes ticket");

        when(jpaTicketTypeRepository.save(any(TicketTypeEntity.class))).thenReturn(testTicketTypeEntity);
        TicketType createdTicketType = jpaTicketTypeService.save(testTicketType);
        assertThat(createdTicketType.getDaysOfValidity()).isSameAs(testTicketType.getDaysOfValidity());
        assertThat(createdTicketType.getMinutesOfValidity()).isSameAs(testTicketType.getMinutesOfValidity());
        assertThat(createdTicketType.getDisplayName()).isSameAs(testTicketType.getDisplayName());
        assertThat(createdTicketType.isReduced()).isSameAs(testTicketType.isReduced());
    }

    @Test
    public void shouldReturnTicketTypeById() {
        TicketType testTicketType = new TicketType();
        testTicketType.setId(0L);
        testTicketType.setReduced(true);
        testTicketType.setDaysOfValidity(0);
        testTicketType.setMinutesOfValidity(30);
        testTicketType.setPrice(340L);
        testTicketType.setDisplayName("30 minutes ticket");

        TicketTypeEntity testTicketTypeEntity = new TicketTypeEntity();
        testTicketTypeEntity.setId(0L);
        testTicketTypeEntity.setReduced(true);
        testTicketTypeEntity.setDaysOfValidity(0);
        testTicketTypeEntity.setMinutesOfValidity(30);
        testTicketTypeEntity.setPrice(340L);
        testTicketTypeEntity.setDisplayName("30 minutes ticket");

        when(jpaTicketTypeRepository.findById(0L)).thenReturn(Optional.of(testTicketTypeEntity));
        TicketType ticketType = jpaTicketTypeService.getById(0L);
        assertThat(ticketType.getDaysOfValidity()).isSameAs(testTicketType.getDaysOfValidity());
        assertThat(ticketType.getMinutesOfValidity()).isSameAs(testTicketType.getMinutesOfValidity());
        assertThat(ticketType.getDisplayName()).isSameAs(testTicketType.getDisplayName());
        assertThat(ticketType.isReduced()).isSameAs(testTicketType.isReduced());
    }

    @Test
    public void shouldReturnAllTicketTypes() {
        TicketTypeEntity testTicketTypeEntity1 = new TicketTypeEntity();
        testTicketTypeEntity1.setId(0L);
        TicketTypeEntity testTicketTypeEntity2 = new TicketTypeEntity();
        testTicketTypeEntity2.setReduced(true);
        testTicketTypeEntity2.setId(1L);
        
        List<TicketTypeEntity> ticketTypesEntitiesList = new ArrayList<TicketTypeEntity>();
        ticketTypesEntitiesList.add(testTicketTypeEntity1);
        ticketTypesEntitiesList.add(testTicketTypeEntity2);

        doReturn(ticketTypesEntitiesList).when(jpaTicketTypeRepository).findAll();
        List<TicketType> returnedTypes = jpaTicketTypeService.getAll();
        assertThat(returnedTypes.get(0).getId()).isSameAs(testTicketTypeEntity1.getId());
        assertThat(returnedTypes.get(1).getId()).isSameAs(testTicketTypeEntity2.getId());
    }
}
